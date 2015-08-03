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
package com.voxelplugineering.voxelsniper;

import com.voxelplugineering.voxelsniper.brush.BrushInfo;
import com.voxelplugineering.voxelsniper.brush.CommonBrushManager;
import com.voxelplugineering.voxelsniper.brush.GlobalBrushManager;
import com.voxelplugineering.voxelsniper.commands.AliasCommand;
import com.voxelplugineering.voxelsniper.commands.BrushCommand;
import com.voxelplugineering.voxelsniper.commands.HelpCommand;
import com.voxelplugineering.voxelsniper.commands.MaskMaterialCommand;
import com.voxelplugineering.voxelsniper.commands.MaterialCommand;
import com.voxelplugineering.voxelsniper.commands.ParameterCommand;
import com.voxelplugineering.voxelsniper.commands.RedoCommand;
import com.voxelplugineering.voxelsniper.commands.ResetCommand;
import com.voxelplugineering.voxelsniper.commands.UndoCommand;
import com.voxelplugineering.voxelsniper.commands.VSCommand;
import com.voxelplugineering.voxelsniper.config.BaseConfiguration;
import com.voxelplugineering.voxelsniper.event.handler.CommonEventHandler;
import com.voxelplugineering.voxelsniper.service.AnnotationScanner;
import com.voxelplugineering.voxelsniper.service.BrushManagerService;
import com.voxelplugineering.voxelsniper.service.Builder;
import com.voxelplugineering.voxelsniper.service.CommandHandlerService;
import com.voxelplugineering.voxelsniper.service.GlobalAliasService;
import com.voxelplugineering.voxelsniper.service.InitHook;
import com.voxelplugineering.voxelsniper.service.OfflineUndoHandlerService;
import com.voxelplugineering.voxelsniper.service.PostInit;
import com.voxelplugineering.voxelsniper.service.PreStop;
import com.voxelplugineering.voxelsniper.service.ServicePriorities;
import com.voxelplugineering.voxelsniper.service.alias.CommonAliasHandler;
import com.voxelplugineering.voxelsniper.service.alias.GlobalAliasHandler;
import com.voxelplugineering.voxelsniper.service.alias.SimpleAliasOwner;
import com.voxelplugineering.voxelsniper.service.command.CommandHandler;
import com.voxelplugineering.voxelsniper.service.config.Configuration;
import com.voxelplugineering.voxelsniper.service.config.ConfigurationContainer;
import com.voxelplugineering.voxelsniper.service.config.ConfigurationService;
import com.voxelplugineering.voxelsniper.service.eventbus.AsyncEventBus;
import com.voxelplugineering.voxelsniper.service.eventbus.EventBus;
import com.voxelplugineering.voxelsniper.service.meta.AnnotationScannerService;
import com.voxelplugineering.voxelsniper.service.permission.PermissionProxy;
import com.voxelplugineering.voxelsniper.service.permission.TrivialPermissionProxy;
import com.voxelplugineering.voxelsniper.service.platform.PlatformProxy;
import com.voxelplugineering.voxelsniper.service.platform.TrivialPlatformProxy;
import com.voxelplugineering.voxelsniper.service.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.service.scheduler.Scheduler;
import com.voxelplugineering.voxelsniper.service.text.TextFormatParser;
import com.voxelplugineering.voxelsniper.util.AnnotationHelper;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.DataTranslator;
import com.voxelplugineering.voxelsniper.util.defaults.DefaultAliasBuilder;
import com.voxelplugineering.voxelsniper.world.queue.ChangeQueueTask;
import com.voxelplugineering.voxelsniper.world.queue.OfflineUndoHandler;

import com.google.common.base.Optional;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;

/**
 * The core service provider.
 */
@SuppressWarnings({ "checkstyle:javadocmethod", "javadoc", "static-method" })
public class CoreServiceProvider
{

    public CoreServiceProvider()
    {

    }

    @Builder(target = AnnotationScanner.class, priority = ServicePriorities.ANNOTATION_SCANNER_PRIORITY)
    public final AnnotationScanner buildAnnotationScanner(Context context)
    {
        return new AnnotationScannerService(context);
    }

    @Builder(target = Configuration.class, priority = ServicePriorities.CONFIGURATION_PRIORITY)
    public final Configuration buildConfig(Context context)
    {
        return new ConfigurationService(context);
    }

    @Builder(target = TextFormatParser.class, priority = ServicePriorities.TEXT_FORMAT_PRIORITY)
    public final TextFormatParser buildFormatProxy(Context context)
    {
        return new TextFormatParser.TrivialTextFormatParser(context);
    }

    @Builder(target = EventBus.class, priority = ServicePriorities.EVENT_BUS_PRIORITY)
    public final EventBus buildEventBus(Context context)
    {
        return new AsyncEventBus(context);
    }

    @Builder(target = GlobalAliasHandler.class, priority = ServicePriorities.GLOBAL_ALIAS_HANDLER_PRIORITY)
    public final GlobalAliasHandler buildAliasRegistry(Context context)
    {
        PlatformProxy platform = context.getRequired(PlatformProxy.class);

        SimpleAliasOwner owner = new SimpleAliasOwner(new File(platform.getRoot(), "aliases.json"));
        return new GlobalAliasService(context, new CommonAliasHandler(owner));
    }

    @InitHook(target = EventBus.class)
    public final void initEventBus(Context context, EventBus service)
    {
        service.register(new CommonEventHandler(context));
    }

    @InitHook(target = Configuration.class)
    public final void initConfig(Context context, Configuration configuration)
    {
        context.getRequired(AnnotationScanner.class).register(ConfigurationContainer.class, (ConfigurationService) configuration);
    }

    @InitHook(target = GlobalAliasHandler.class)
    public final void initAlias(Context context, GlobalAliasHandler service)
    {
        service.registerTarget("brush");
        service.registerTarget("material");

        DefaultAliasBuilder.loadDefaultAliases(service);
    }

    @Builder(target = GlobalBrushManager.class, priority = ServicePriorities.GLOBAL_BRUSH_MANAGER_PRIORITY)
    public final GlobalBrushManager getGlobalBrushManager(Context context)
    {
        return new BrushManagerService(context, new CommonBrushManager());
    }

    @InitHook(target = GlobalBrushManager.class)
    public final void findBrushes(Context context, GlobalBrushManager gbm)
    {
        context.getRequired(AnnotationScanner.class).register(BrushInfo.class, gbm);
    }

    @Builder(target = CommandHandler.class, priority = ServicePriorities.COMMAND_HANDLER_PRIORITY)
    public final CommandHandler getCommandHandler(Context context)
    {
        return new CommandHandlerService(context);
    }

    @InitHook(target = CommandHandler.class)
    public final void registerCommands(Context context, CommandHandler cmd)
    {
        cmd.registerCommand(new BrushCommand(context));
        cmd.registerCommand(new MaterialCommand(context));
        cmd.registerCommand(new MaskMaterialCommand(context));
        cmd.registerCommand(new VSCommand(context));
        cmd.registerCommand(new AliasCommand(context));
        cmd.registerCommand(new HelpCommand(context));
        cmd.registerCommand(new ResetCommand(context));
        cmd.registerCommand(new UndoCommand(context));
        cmd.registerCommand(new RedoCommand(context));
        cmd.registerCommand(new ParameterCommand(context));
    }

    @Builder(target = OfflineUndoHandler.class, priority = ServicePriorities.UNDO_HANDLER_PRIORITY)
    public final OfflineUndoHandler getOfflineUndo(Context context)
    {
        return new OfflineUndoHandlerService(context);
    }

    @Builder(target = PlatformProxy.class, priority = ServicePriorities.PLATFORM_PROXY_PRIORITY)
    public final PlatformProxy getTrivialPlatform(Context context)
    {
        return new TrivialPlatformProxy(context);
    }

    @Builder(target = PermissionProxy.class, priority = ServicePriorities.PERMISSION_PROXY_PRIORITY)
    public final PermissionProxy getPermissionsProxy(Context context)
    {
        return new TrivialPermissionProxy(context);
    }

    /**
     * Post init.
     */
    @PostInit
    public final void post(Context context)
    {
        DataTranslator.initialize(context);

        context.getRequired(AnnotationScanner.class).scanClassPath((URLClassLoader) Gunsmith.getClassLoader());

        PlayerRegistry<?> players = context.getRequired(PlayerRegistry.class);

        Configuration conf = context.getRequired(Configuration.class);
        PlatformProxy platform = context.getRequired(PlatformProxy.class);
        try
        {
            conf.initialize(platform.getRoot(), false);
        } catch (IOException e)
        {
            GunsmithLogger.getLogger().error(e, "Error initializing configuration files.");
        }

        Optional<Scheduler> sched = context.get(Scheduler.class);
        if (sched.isPresent())
        {
            sched.get().startSynchronousTask(new ChangeQueueTask(players), BaseConfiguration.changeInterval);
        }
    }

    /**
     * Stop hook.
     */
    @PreStop
    public final void onStop(Context context)
    {
        Optional<Scheduler> sched = context.get(Scheduler.class);
        if (sched.isPresent())
        {
            sched.get().stopAllTasks();
        }
        AnnotationHelper.clean();
    }

}
