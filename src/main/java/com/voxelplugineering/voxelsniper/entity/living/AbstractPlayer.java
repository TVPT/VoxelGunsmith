/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 The Voxel Plugineering Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.voxelplugineering.voxelsniper.entity.living;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.thevoxelbox.vsl.VariableScope;
import com.thevoxelbox.vsl.api.IVariableScope;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.brushes.BrushNodeGraph;
import com.voxelplugineering.voxelsniper.brushes.CommonBrushManager;
import com.voxelplugineering.voxelsniper.registry.WeakWrapper;
import com.voxelplugineering.voxelsniper.world.queue.ChangeQueue;

/**
 * An abstract player.
 * 
 * @param <T> The underlying player type
 */
public abstract class AbstractPlayer<T> extends WeakWrapper<T> implements Player
{
    /**
     * This users specific brush manager.
     */
    private BrushManager personalBrushManager;
    /**
     * The currently selected brush.
     */
    private BrushNodeGraph currentBrush;
    /**
     * The brush settings specific to this player.
     */
    private IVariableScope brushVariables;
    /**
     * The history buffer for this player. Contains an ordered stack of inverse change queues.
     */
    private Deque<ChangeQueue> history;
    /**
     * The queue of pending {@link ChangeQueue}s waiting to be processed.
     */
    private Queue<ChangeQueue> pending;
    private AliasHandler personalAliasHandler;
    private Map<String, String> arguments;

    /**
     * Creates a new CommonPlayer with a weak reference to the player. TODO add constructor for receiving custom parent brush manager
     * 
     * @param player the player object
     */
    protected AbstractPlayer(T player)
    {
        super(player);
        //TODO: have player inherit brushes from group rather than the global brush manager always.
        this.personalBrushManager = new CommonBrushManager(Gunsmith.getGlobalBrushManager());
        this.brushVariables = new VariableScope();
        this.brushVariables.setCaseSensitive(false);
        this.arguments = Maps.newHashMap();
        this.history = Queues.newArrayDeque();
        this.pending = new LinkedList<ChangeQueue>();
        this.personalAliasHandler = new AliasHandler(this, Gunsmith.getGlobalAliasHandler());
        resetSettings();
    }

    /**
     * {@inheritDoc}
     */
    public BrushManager getPersonalBrushManager()
    {
        return this.personalBrushManager;
    }

    /**
     * {@inheritDoc}
     */
    public void setCurrentBrush(BrushNodeGraph brush)
    {
        this.currentBrush = brush;
    }

    /**
     * {@inheritDoc}
     */
    public BrushNodeGraph getCurrentBrush()
    {
        return this.currentBrush;
    }

    /**
     * {@inheritDoc}
     */
    public IVariableScope getBrushSettings()
    {
        return this.brushVariables;
    }

    /**
     * {@inheritDoc}
     */
    public void resetSettings()
    {
        this.arguments.clear();
        BrushNodeGraph start = null;
        BrushNodeGraph last = null;
        Pattern pattern = Pattern.compile("([\\S&&[^\\{]]+) ?(?:(\\{[^\\}]*\\}))?");
        Matcher match = pattern.matcher(prep(Gunsmith.getConfiguration().get("defaultBrush").get().toString()));
        while (match.find())
        {
            String brushName = match.group(1);
            BrushNodeGraph brush = getPersonalBrushManager().getBrush(brushName).orNull();
            if (brush == null)
            {
                getPersonalBrushManager().loadBrush(brushName);
                brush = getPersonalBrushManager().getBrush(brushName).orNull();
                if (brush == null)
                {
                    sendMessage("Could not find a brush part named " + brushName);
                    break;
                }
            }
            if (start == null)
            {
                start = brush;
                last = brush;
            } else
            {
                last.chain(brush);
                last = brush;
            }
        }
        setCurrentBrush(start);
        sendMessage("Brush set to " + Gunsmith.getConfiguration().get("defaultBrush").get().toString());
        this.brushVariables.set("brushSize", Gunsmith.getConfiguration().get("defaultBrushSize").get());
        sendMessage("Your brush size was changed to " + Gunsmith.getConfiguration().get("defaultBrushSize").get().toString());
        Material material =
                getWorld()
                        .getMaterialRegistry()
                        .getMaterial(
                                Gunsmith.getConfiguration().get("defaultBrushMaterial").or(getWorld().getMaterialRegistry().getAirMaterial())
                                        .toString()).get();
        sendMessage("Set material to " + material.getName());
        getBrushSettings().set("setMaterial", material);
    }

    private String prep(String s)
    {
        s = s.trim();
        while (s.startsWith("{"))
        {
            int index = s.indexOf("}");
            if (index == -1)
            {
                s = s.substring(1);
            }
            s = s.substring(index + 1).trim();
        }
        return s;
    }

    /**
     * {@inheritDoc}
     */
    public void addHistory(ChangeQueue changeQueue)
    {
        checkNotNull(changeQueue, "ChangeQueue cannot be null");
        this.history.addFirst(changeQueue);
        while (this.history.size() > 20)
        {
            this.history.removeLast();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void undoHistory(int n)
    {
        while (n > 0 && !this.history.isEmpty())
        {
            n--;
            ChangeQueue next = this.history.removeFirst();
            this.pending.add(next);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPendingChanges()
    {
        return this.pending.size() != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ChangeQueue> getNextPendingChange()
    {
        return (this.pending.isEmpty() ? Optional.<ChangeQueue>absent() : Optional.<ChangeQueue>of(this.pending.peek()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPending(ChangeQueue queue)
    {
        checkNotNull(queue, "ChangeQueue cannot be null");
        this.pending.add(queue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearNextPending()
    {
        if (!this.pending.isEmpty() && this.pending.peek().isFinished())
        {
            this.pending.remove();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AliasHandler getPersonalAliasHandler()
    {
        return this.personalAliasHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getBrushArguments()
    {
        return Collections.unmodifiableMap(this.arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBrushArgument(String brush, String arg)
    {
        if (arg == null || arg.isEmpty())
        {
            return;//Note: no illegal argument exception as arg is allowed to be null
        }
        this.arguments.put(brush, arg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getAliasFile()
    {
        File aliases = new File(Gunsmith.getDataFolder(), "players/" + getName() + "/aliases.json");
        return aliases;
    }

}
