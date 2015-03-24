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

import java.util.LinkedList;
import java.util.Queue;

import com.google.common.base.Optional;
import com.thevoxelbox.vsl.api.variables.VariableScope;
import com.thevoxelbox.vsl.variables.ParentedVariableScope;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.alias.CommonAliasHandler;
import com.voxelplugineering.voxelsniper.api.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceReader;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.api.world.queue.UndoQueue;
import com.voxelplugineering.voxelsniper.brushes.BrushChain;
import com.voxelplugineering.voxelsniper.brushes.CommonBrushManager;
import com.voxelplugineering.voxelsniper.entity.AbstractEntity;
import com.voxelplugineering.voxelsniper.util.BrushParsing;
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
    protected AbstractPlayer(T player, BrushManager parentBrushManager)
    {
        super(player);
        this.personalBrushManager = new CommonBrushManager(parentBrushManager);
        this.brushVariables = new ParentedVariableScope();
        this.brushVariables.setCaseSensitive(false);
        this.pending = new LinkedList<ChangeQueue>();
        this.personalAliasHandler = new CommonAliasHandler(this, Gunsmith.getGlobalAliasHandler());
        this.personalAliasHandler.init();
        this.history = new CommonUndoQueue(this);
        try
        {
            resetSettings();
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Error setting up default player settings.");
            // we catch exceptions here so that an issue while resetting
            // settings cannot stop the object from initializing.
        }
    }

    /**
     * Creates a new {@link AbstractPlayer} with a weak reference to the player.
     * The player will use the global brush manager as its parent brush manager.
     * 
     * @param player the player object
     */
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
    public BrushManager getBrushManager()
    {
        return this.personalBrushManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBrushManager(BrushManager manager)
    {
        this.personalBrushManager = manager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentBrush(BrushChain brush)
    {
        this.currentBrush = brush;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BrushChain getCurrentBrush()
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
        this.brushVariables.clear();
        String brush = Gunsmith.getConfiguration().get("defaultBrush", String.class).or("voxel material");
        Optional<BrushChain> current = BrushParsing.parse(brush, this.personalBrushManager, this.personalAliasHandler.getRegistry("brush").orNull());
        if (current.isPresent())
        {
            setCurrentBrush(current.get());
            sendMessage("Brush set to " + Gunsmith.getConfiguration().get("defaultBrush").get().toString());
        }
        double size = Gunsmith.getConfiguration().get("defaultBrushSize", Double.class).or(5.0);
        this.brushVariables.set("brushSize", size);
        sendMessage("Your brush size was changed to " + size);
        Optional<String> materialName = Gunsmith.getConfiguration().get("defaultBrushMaterial", String.class);
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
        return Optional.fromNullable(this.pending.peek());
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
    public DataSourceReader getAliasSource()
    {
        return null; // TODO persistence
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AliasHandler getAliasHandler()
    {
        return this.personalAliasHandler;
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
