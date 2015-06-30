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
package com.voxelplugineering.voxelsniper.core;

import java.io.IOException;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.service.Builder;
import com.voxelplugineering.voxelsniper.api.service.InitHook;
import com.voxelplugineering.voxelsniper.api.service.InitPhase;
import com.voxelplugineering.voxelsniper.api.service.PostInit;
import com.voxelplugineering.voxelsniper.api.service.PreStop;
import com.voxelplugineering.voxelsniper.api.service.alias.GlobalAliasHandler;
import com.voxelplugineering.voxelsniper.api.service.command.CommandHandler;
import com.voxelplugineering.voxelsniper.api.service.config.Configuration;
import com.voxelplugineering.voxelsniper.api.service.config.ConfigurationContainer;
import com.voxelplugineering.voxelsniper.api.service.event.EventBus;
import com.voxelplugineering.voxelsniper.api.service.permission.PermissionProxy;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceFactory;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceProvider;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceReader;
import com.voxelplugineering.voxelsniper.api.service.platform.PlatformProxy;
import com.voxelplugineering.voxelsniper.api.service.platform.TrivialPlatformProxy;
import com.voxelplugineering.voxelsniper.api.service.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.api.service.scheduler.Scheduler;
import com.voxelplugineering.voxelsniper.api.service.text.TextFormatParser;
import com.voxelplugineering.voxelsniper.api.world.queue.OfflineUndoHandler;
import com.voxelplugineering.voxelsniper.brush.CommonBrushManager;
import com.voxelplugineering.voxelsniper.brush.GlobalBrushManager;
import com.voxelplugineering.voxelsniper.core.commands.AliasCommand;
import com.voxelplugineering.voxelsniper.core.commands.BrushCommand;
import com.voxelplugineering.voxelsniper.core.commands.HelpCommand;
import com.voxelplugineering.voxelsniper.core.commands.MaskMaterialCommand;
import com.voxelplugineering.voxelsniper.core.commands.MaterialCommand;
import com.voxelplugineering.voxelsniper.core.commands.RedoCommand;
import com.voxelplugineering.voxelsniper.core.commands.ResetCommand;
import com.voxelplugineering.voxelsniper.core.commands.UndoCommand;
import com.voxelplugineering.voxelsniper.core.commands.VSCommand;
import com.voxelplugineering.voxelsniper.core.config.BaseConfiguration;
import com.voxelplugineering.voxelsniper.core.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.core.event.handler.CommonEventHandler;
import com.voxelplugineering.voxelsniper.core.service.BrushManagerService;
import com.voxelplugineering.voxelsniper.core.service.CommandHandlerService;
import com.voxelplugineering.voxelsniper.core.service.ConfigurationService;
import com.voxelplugineering.voxelsniper.core.service.DataSourceFactoryService;
import com.voxelplugineering.voxelsniper.core.service.GlobalAliasService;
import com.voxelplugineering.voxelsniper.core.service.OfflineUndoHandlerService;
import com.voxelplugineering.voxelsniper.core.service.alias.CommonAliasHandler;
import com.voxelplugineering.voxelsniper.core.service.alias.SimpleAliasOwner;
import com.voxelplugineering.voxelsniper.core.service.eventbus.AsyncEventBus;
import com.voxelplugineering.voxelsniper.core.service.permissions.TrivialPermissionProxy;
import com.voxelplugineering.voxelsniper.core.service.persistence.FileDataSource;
import com.voxelplugineering.voxelsniper.core.service.persistence.JsonDataSourceReader;
import com.voxelplugineering.voxelsniper.core.service.persistence.MemoryContainer;
import com.voxelplugineering.voxelsniper.core.service.persistence.StandardOutDataSource;
import com.voxelplugineering.voxelsniper.core.util.AnnotationHelper;
import com.voxelplugineering.voxelsniper.core.util.Context;
import com.voxelplugineering.voxelsniper.core.util.defaults.DefaultAliasBuilder;
import com.voxelplugineering.voxelsniper.core.world.queue.ChangeQueueTask;

/**
 * The core service provider.
 */
@SuppressWarnings("javadoc")
public class CoreServiceProvider
{

    @Builder(target = DataSourceFactory.class, priority = -1000, initPhase = InitPhase.EARLY)
    public DataSourceFactory buildPersistence(Context context)
    {
        return new DataSourceFactoryService(context);
    }

    @Builder(target = Configuration.class, priority = 1000)
    public Configuration buildConfig(Context context)
    {
        return new ConfigurationService(context);
    }

    @Builder(target = TextFormatParser.class, priority = 0)
    public TextFormatParser buildFormatProxy(Context context)
    {
        return new TextFormatParser.TrivialTextFormatParser(context);
    }

    @Builder(target = EventBus.class, priority = 2000)
    public EventBus buildEventBus(Context context)
    {
        return new AsyncEventBus(context);
    }

    @Builder(target = GlobalAliasHandler.class, priority = 5000)
    public GlobalAliasHandler buildAliasRegistry(Context context)
    {
        PlatformProxy platform = context.getRequired(PlatformProxy.class);
        Configuration conf = context.getRequired(Configuration.class);

        DataSourceReader aliases = platform.getRootDataSourceProvider().getWithReader("aliases.json", JsonDataSourceReader.class).get();
        boolean caseSensitive = conf.get("caseSensitiveAliases", boolean.class).or(false);

        SimpleAliasOwner owner = new SimpleAliasOwner(aliases);
        return new GlobalAliasService(context, new CommonAliasHandler(owner, caseSensitive));
    }

    @InitHook(target = EventBus.class)
    public void initEventBus(Context context, EventBus service)
    {
        service.register(new CommonEventHandler(context));
    }

    @InitHook(target = DataSourceFactory.class)
    public void initPersistence(Context context, DataSourceFactory factory)
    {
        factory.register("stdout", StandardOutDataSource.class, StandardOutDataSource.BUILDER);
        factory.register("file", FileDataSource.class, FileDataSource.BUILDER);
        factory.register("json", JsonDataSourceReader.class, JsonDataSourceReader.getBuilder(factory));
    }

    @InitHook(target = Configuration.class)
    public void initConfig(Context context, Configuration configuration)
    {
        configuration.registerContainer(BaseConfiguration.class);
        configuration.registerContainer(VoxelSniperConfiguration.class);
    }

    @InitHook(target = GlobalAliasHandler.class)
    public void initAlias(Context context, GlobalAliasHandler service)
    {
        service.registerTarget("brush");
        service.registerTarget("material");

        DefaultAliasBuilder.loadDefaultAliases(service);
    }

    @Builder(target = GlobalBrushManager.class, priority = 7500)
    public GlobalBrushManager getGlobalBrushManager(Context context)
    {
        return new BrushManagerService(context, new CommonBrushManager());
    }

    @Builder(target = CommandHandler.class, priority = 10000)
    public CommandHandler getCommandHandler(Context context)
    {
        return new CommandHandlerService(context);
    }

    @InitHook(target = CommandHandler.class)
    public void registerCommands(Context context, CommandHandler cmd)
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
    }

    @Builder(target = OfflineUndoHandler.class, priority = 13000)
    public OfflineUndoHandler getOfflineUndo(Context context)
    {
        return new OfflineUndoHandlerService(context);
    }

    @Builder(target = PlatformProxy.class, priority = 4000)
    public PlatformProxy getTrivialPlatform(Context context)
    {
        return new TrivialPlatformProxy(context);
    }

    @Builder(target = PermissionProxy.class, priority = 7000)
    public PermissionProxy getPermissionsProxy(Context context)
    {
        return new TrivialPermissionProxy(context);
    }

    /**
     * Post init
     */
    @PostInit
    public void post(Context context)
    {
        PlatformProxy proxy = context.getRequired(PlatformProxy.class);
        Configuration configuration = context.getRequired(Configuration.class);
        PlayerRegistry<?> players = context.getRequired(PlayerRegistry.class);

        Optional<DataSourceReader> oconfig = proxy.getConfigDataSource();
        DataSourceReader config = null;

        if (!oconfig.isPresent())
        {
            DataSourceProvider provider = proxy.getRootDataSourceProvider();
            DataContainer args = new MemoryContainer("");
            DataSourceReader reader = provider.getWithReader("VoxelSniperConfiguration.json", JsonDataSourceReader.class, args).orNull();
            if (reader != null)
            {
                ((JsonDataSourceReader) reader).setPrettyOutput(true);
                config = reader;
            }
        } else
        {
            config = oconfig.get();
        }
        if (config != null)
        {
            try
            {
                if (config.exists())
                {
                    GunsmithLogger.getLogger().info("Loading config from " + config.getName().or("an unknown source"));
                    DataContainer values = config.read();
                    Optional<ConfigurationContainer> container = configuration.getContainer("VoxelSniperConfiguration");
                    if (container.isPresent())
                    {
                        container.get().fromContainer(values);
                        configuration.refreshContainer("VoxelSniperConfiguration");
                    } else
                    {
                        GunsmithLogger.getLogger().warn("Could not find config container for VoxelSniperConfiguration");
                    }
                } else
                {
                    Optional<ConfigurationContainer> container = configuration.getContainer("VoxelSniperConfiguration");
                    if (container.isPresent())
                    {
                        GunsmithLogger.getLogger().info("Saving config to " + config.getName().or("an unknown source"));
                        config.write(container.get());
                    }
                }
            } catch (IOException e)
            {
                GunsmithLogger.getLogger().error(e, "Error loading configuration values.");
            }
        } else
        {
            GunsmithLogger.getLogger().warn("Could not find the configuration data source, and no fallback could be created.");
        }
        /*Runnable aliasTask = new AliasSaveTask();

        String aliasSource = configuration.get("aliasDataSource", String.class).or("json");
        Optional<DataContainer> aliasSourceArgs = configuration.get("aliasDataSourceArgs", DataContainer.class);
        DataContainer args = null;
        if (!aliasSourceArgs.isPresent())
        {
            args = new MemoryContainer("");
        } else
        {
            args = aliasSourceArgs.get();
        }

        Optional<DataSource> aliassave = Gunsmith.getPersistence().build(aliasSource, args);
        DataSourceReader aliassource = null;

        if (!oconfig.isPresent())
        {
            DataContainer aliasargs = new MemoryContainer("");
            config =
                    (DataSourceReader) Gunsmith.getPlatformProxy().getRootDataSourceProvider()
                            .getWithReader("VoxelSniperConfiguration.json", JsonDataSourceReader.class, args);
        } else
        {
            config = (DataSourceReader) oconfig.get();
        }
        try
        {
            if (config.exists())
            {
                DataContainer values = config.read();
                Optional<AbstractConfigurationContainer> container = configuration.getContainer("VoxelSniperConfiguration");
                if (container.isPresent())
                {
                    container.get().fromContainer(values);
                }
            } else
            {

                Optional<AbstractConfigurationContainer> container = configuration.getContainer("VoxelSniperConfiguration");
                if (container.isPresent())
                {
                    config.write(container.get());
                }
            }
        } catch (IOException e)
        {
            Gunsmith.getLogger().error(e, "Error loading configuration values.");
        }

        File globalAliases = new File(platformProxy.getDataFolder(), "aliases.json");
        JsonDataSource data = new JsonDataSource(globalAliases);
        if (globalAliases.exists())
        {
            try
            {
                data.read(globalAliasRegistries);
            } catch (IOException e)
            {
                getLogger().error(e, "Error loading global aliases");
            }
        } else
        {
            DefaultAliasBuilder.loadDefaultAliases(globalAliasRegistries);
            aliasTask.addDirty(globalAliasRegistries);
        }*/
        Optional<Scheduler> sched = context.get(Scheduler.class);
        if (sched.isPresent())
        {
            //Gunsmith.getScheduler().startSynchronousTask(aliasTask, configuration.get("aliasInterval", int.class).or(30000));
            sched.get().startSynchronousTask(new ChangeQueueTask(players, configuration),
                    configuration.get("changeInterval", int.class).or(100));
        }
    }

    /**
     * Stop hook
     */
    @PreStop
    public void onStop(Context context)
    {
        /*
         * TODO persistence File globalAliases = new
         * File(platformProxy.getDataFolder(), "aliases.json"); JsonDataSource
         * data = new JsonDataSource(globalAliases); try { if
         * (!globalAliases.exists()) { globalAliases.createNewFile(); }
         * data.write(getGlobalAliasHandler()); } catch (IOException e) {
         * getLogger().error(e, "Error saving global aliases"); } // save all
         * player's personal aliases for (Player player :
         * sniperRegistry.getAllPlayers()) { File playerFolder = new
         * File(Gunsmith.platformProxy.getDataFolder(), "players/" +
         * player.getUniqueId().toString()); File aliases = new
         * File(playerFolder, "aliases.json"); JsonDataSource playerData = new
         * JsonDataSource(aliases); try { if (aliases.exists()) {
         * aliases.createNewFile(); }
         * playerData.write(player.getPersonalAliasHandler()); } catch
         * (IOException e) { Gunsmith.getLogger().error(e,
         * "Error saving player aliases!"); } }
         */
        Optional<Scheduler> sched = context.get(Scheduler.class);
        if (sched.isPresent())
        {
            sched.get().stopAllTasks();
        }
        AnnotationHelper.clean();
    }

}
