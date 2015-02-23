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
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.thevoxelbox.vsl.api.variables.VariableScope;
import com.thevoxelbox.vsl.variables.ParentedVariableScope;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.api.world.queue.UndoQueue;
import com.voxelplugineering.voxelsniper.brushes.BrushNodeGraph;
import com.voxelplugineering.voxelsniper.brushes.CommonBrushManager;
import com.voxelplugineering.voxelsniper.entity.AbstractEntity;
import com.voxelplugineering.voxelsniper.world.queue.ChangeQueue;
import com.voxelplugineering.voxelsniper.world.queue.CommonUndoQueue;

/**
 * An abstract player.
 * 
 * @param <T> The underlying player type
 */
public abstract class AbstractPlayer<T> extends AbstractEntity<T> implements Player
{

    private BrushManager personalBrushManager;
    private BrushNodeGraph currentBrush;
    private VariableScope brushVariables;
    private Queue<ChangeQueue> pending;
    private AliasHandler personalAliasHandler;
    private Map<String, String> arguments;
    private UndoQueue history;

    /**
     * Creates a new CommonPlayer with a weak reference to the player.
     * 
     * @param player the player object
     * @param parentBrushManager The parent brush manager
     */
    protected AbstractPlayer(T player, BrushManager parentBrushManager)
    {
        super(player);
        this.personalBrushManager = new CommonBrushManager(parentBrushManager);
        this.brushVariables = new ParentedVariableScope();
        this.brushVariables.setCaseSensitive(false);
        this.arguments = Maps.newHashMap();
        this.pending = new LinkedList<ChangeQueue>();
        this.personalAliasHandler = new AliasHandler(this, Gunsmith.getGlobalAliasHandler());
        this.history = new CommonUndoQueue(this);
        resetSettings();
    }

    protected AbstractPlayer(T player)
    {
        this(player, Gunsmith.getGlobalBrushManager());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlayer()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(String format, Object... args)
    {
        sendMessage(String.format(format, args));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BrushManager getPersonalBrushManager()
    {
        return this.personalBrushManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentBrush(BrushNodeGraph brush)
    {
        this.currentBrush = brush;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BrushNodeGraph getCurrentBrush()
    {
        return this.currentBrush;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VariableScope getBrushSettings()
    {
        return this.brushVariables;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetSettings()
    {
        this.arguments.clear();
        this.brushVariables.clear();
        BrushNodeGraph start = null;
        BrushNodeGraph last = null;
        Pattern pattern = Pattern.compile("([\\S&&[^\\{]]+) ?(?:(\\{[^\\}]*\\}))?");
        Matcher match = pattern.matcher(prep(Gunsmith.getConfiguration().get("defaultBrush").get().toString()));
        while (match.find())
        {
            String brushName = match.group(1);
            Optional<BrushNodeGraph> brush = getPersonalBrushManager().getBrush(brushName);
            if (!brush.isPresent())
            {
                getPersonalBrushManager().loadBrush(brushName);
                brush = getPersonalBrushManager().getBrush(brushName);
                if (!brush.isPresent())
                {
                    sendMessage("Could not find a brush part named " + brushName);
                    break;
                }
            }
            if (start == null)
            {
                start = brush.get();
                last = brush.get();
            } else
            {
                last.chain(brush.get());
                last = brush.get();
            }
        }
        setCurrentBrush(start);
        sendMessage("Brush set to " + Gunsmith.getConfiguration().get("defaultBrush").get().toString());
        this.brushVariables.set("brushSize", Gunsmith.getConfiguration().get("defaultBrushSize", Double.class).or(5.0));
        sendMessage("Your brush size was changed to " + Gunsmith.getConfiguration().get("defaultBrushSize").get().toString());
        Optional<String> materialName = Gunsmith.getConfiguration().get("defaultBrushMaterial", String.class);
        Material mat = getWorld().getMaterialRegistry().getAirMaterial();
        if(materialName.isPresent())
        {
            Optional<Material> material = getWorld().getMaterialRegistry().getMaterial(materialName.get());
            if(material.isPresent())
            {
                mat = material.get();
            }
        }
        sendMessage("Set material to " + mat.getName());
        getBrushSettings().set("setMaterial", mat);
        getBrushSettings().set("maskmaterial", mat);
    }

    private static String prep(String s)
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
    @Override
    public void undoHistory(int n)
    {
        int c = this.history.undo(n);
        this.sendMessage("%d changes undone.", c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void redoHistory(int n)
    {
        int c = this.history.redo(n);
        this.sendMessage("%d changes re-applied.", c);
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
        queue.reset();
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
            return;// Note: no illegal argument exception as arg is allowed to
                   // be null
        }
        this.arguments.put(brush, arg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getAliasFile()
    {
        File aliases = new File(Gunsmith.getDataFolder(), "players/" + getUniqueId().toString() + "/aliases.json");
        return aliases;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UndoQueue getUndoHistory()
    {
        return this.history;
    }

}
