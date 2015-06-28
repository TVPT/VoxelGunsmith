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
package com.voxelplugineering.voxelsniper.core.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.Queue;

import com.google.common.base.Optional;
import com.thevoxelbox.vsl.api.variables.VariableScope;
import com.thevoxelbox.vsl.variables.ParentedVariableScope;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.brushes.GlobalBrushManager;
import com.voxelplugineering.voxelsniper.api.entity.Player;
import com.voxelplugineering.voxelsniper.api.service.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.api.service.alias.GlobalAliasHandler;
import com.voxelplugineering.voxelsniper.api.service.config.Configuration;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceReader;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.api.world.queue.UndoQueue;
import com.voxelplugineering.voxelsniper.core.GunsmithLogger;
import com.voxelplugineering.voxelsniper.core.brushes.BrushChain;
import com.voxelplugineering.voxelsniper.core.brushes.CommonBrushManager;
import com.voxelplugineering.voxelsniper.core.service.alias.CommonAliasHandler;
import com.voxelplugineering.voxelsniper.core.util.BrushParsing;
import com.voxelplugineering.voxelsniper.core.util.Context;
import com.voxelplugineering.voxelsniper.core.world.queue.ChangeQueue;
import com.voxelplugineering.voxelsniper.core.world.queue.CommonUndoQueue;

/**
 * An abstract player.
 * 
 * @param <T> The underlying player type
 */
public abstract class AbstractPlayer<T> extends AbstractEntity<T> implements Player
{

    private final Configuration conf;

    private BrushManager personalBrushManager;
    private BrushChain currentBrush;
    private VariableScope brushVariables;
    private Queue<ChangeQueue> pending;
    private AliasHandler personalAliasHandler;
    private UndoQueue history;

    /**
     * Creates a new {@link AbstractPlayer} with a weak reference to the player.
     * 
     * @param player the player object
     * @param parentBrushManager The parent brush manager
     */
    protected AbstractPlayer(T player, BrushManager parentBrushManager, Context context)
    {
        super(player);
        this.conf = context.getRequired(Configuration.class);
        this.personalBrushManager = new CommonBrushManager(parentBrushManager);
        this.brushVariables = new ParentedVariableScope();
        this.brushVariables.setCaseSensitive(false);
        this.pending = new LinkedList<ChangeQueue>();
        boolean caseSensitiveAliases = this.conf.get("caseSensitiveAliases", boolean.class).or(false);
        this.personalAliasHandler = new CommonAliasHandler(this, context.getRequired(GlobalAliasHandler.class), caseSensitiveAliases);
        this.history = new CommonUndoQueue(this);
    }

    protected AbstractPlayer(T player, Context context)
    {
        super(player);
        this.conf = context.getRequired(Configuration.class);
        this.personalBrushManager = new CommonBrushManager(context.getRequired(GlobalBrushManager.class));
        this.brushVariables = new ParentedVariableScope();
        this.brushVariables.setCaseSensitive(false);
        this.pending = new LinkedList<ChangeQueue>();
        boolean caseSensitiveAliases = this.conf.get("caseSensitiveAliases", boolean.class).or(false);
        this.personalAliasHandler = new CommonAliasHandler(this, context.getRequired(GlobalAliasHandler.class), caseSensitiveAliases);
        this.history = new CommonUndoQueue(this);
    }

    public void init()
    {
        try
        {
            resetSettings();
        } catch (Exception e)
        {
            GunsmithLogger.getLogger().error(e, "Error setting up default player settings.");
        }
    }

    @Override
    public boolean isPlayer()
    {
        return true;
    }

    @Override
    public void sendMessage(String format, Object... args)
    {
        sendMessage(String.format(format, args));
    }

    @Override
    public BrushManager getBrushManager()
    {
        return this.personalBrushManager;
    }

    @Override
    public void setBrushManager(BrushManager manager)
    {
        this.personalBrushManager = manager;
    }

    @Override
    public void setCurrentBrush(BrushChain brush)
    {
        this.currentBrush = brush;
    }

    @Override
    public BrushChain getCurrentBrush()
    {
        return this.currentBrush;
    }

    @Override
    public VariableScope getBrushSettings()
    {
        return this.brushVariables;
    }

    @Override
    public void resetSettings()
    {
        this.brushVariables.clear();
        String brush = this.conf.get("defaultBrush", String.class).or("voxel material");
        Optional<BrushChain> current = BrushParsing.parse(brush, this.personalBrushManager, this.personalAliasHandler.getRegistry("brush").orNull());
        if (current.isPresent())
        {
            setCurrentBrush(current.get());
            sendMessage("Brush set to " + this.conf.get("defaultBrush").get().toString());
        }
        double size = this.conf.get("defaultBrushSize", Double.class).or(5.0);
        this.brushVariables.set("brushSize", size);
        sendMessage("Your brush size was changed to " + size);
        Optional<String> materialName = this.conf.get("defaultBrushMaterial", String.class);
        Material mat = getWorld().getMaterialRegistry().getAirMaterial();
        if (materialName.isPresent())
        {
            Optional<Material> material = getWorld().getMaterialRegistry().getMaterial(materialName.get());
            if (material.isPresent())
            {
                mat = material.get();
            }
        }
        sendMessage("Set material to " + mat.getName());
        getBrushSettings().set("setMaterial", mat);
        getBrushSettings().set("maskmaterial", mat);
    }

    @Override
    public void undoHistory(int n)
    {
        int c = this.history.undo(n);
        this.sendMessage("%d changes undone.", c);
    }

    @Override
    public void redoHistory(int n)
    {
        int c = this.history.redo(n);
        this.sendMessage("%d changes re-applied.", c);
    }

    @Override
    public boolean hasPendingChanges()
    {
        return this.pending.size() != 0;
    }

    @Override
    public Optional<ChangeQueue> getNextPendingChange()
    {
        return Optional.fromNullable(this.pending.peek());
    }

    @Override
    public void addPending(ChangeQueue queue)
    {
        checkNotNull(queue, "ChangeQueue cannot be null");
        queue.reset();
        this.pending.add(queue);
    }

    @Override
    public void clearNextPending()
    {
        if (!this.pending.isEmpty() && this.pending.peek().isFinished())
        {
            this.pending.remove();
        }
    }

    @Override
    public DataSourceReader getAliasSource()
    {
        return null; // TODO persistence
    }

    @Override
    public AliasHandler getAliasHandler()
    {
        return this.personalAliasHandler;
    }

    @Override
    public UndoQueue getUndoHistory()
    {
        return this.history;
    }

}
