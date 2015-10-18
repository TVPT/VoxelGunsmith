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
package com.voxelplugineering.voxelsniper.service.command;


import com.voxelplugineering.voxelsniper.commands.Command;
import com.voxelplugineering.voxelsniper.service.Service;

import java.util.Optional;

/**
 * Represents a handler for command execution and registration.
 */
public interface CommandHandler extends Service
{

    /**
     * Gets this handler's {@link CommandRegistrar} if it has one.
     * 
     * @return The command registrar
     */
    Optional<CommandRegistrar> getRegistrar();

    /**
     * Sets this handler's {@link CommandRegistrar}.
     * 
     * @param registrar The new command registrar
     */
    void setRegistrar(CommandRegistrar registrar);

    /**
     * Registers the given command with this handler, and its {@link CommandRegistrar} if it has
     * one.
     * 
     * @param cmd The command to register
     */
    void registerCommand(Command cmd);

    /**
     * Executes the given command from the given sender.
     * 
     * @param sender The command sender
     * @param fullCommand The full command string including args
     */
    void onCommand(CommandSender sender, String fullCommand);

    /**
     * Executes the given command with arguments from the given sender.
     * 
     * @param sender The command sender
     * @param command The command name
     * @param args The command arguments
     */
    void onCommand(CommandSender sender, String command, String[] args);

}
