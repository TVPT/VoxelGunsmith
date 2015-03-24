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

import java.io.IOException;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.alias.AliasHandlerService;
import com.voxelplugineering.voxelsniper.alias.CommonAliasHandler;
import com.voxelplugineering.voxelsniper.api.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.api.alias.AliasOwner;
import com.voxelplugineering.voxelsniper.api.config.AbstractConfigurationContainer;
import com.voxelplugineering.voxelsniper.api.config.Configuration;
import com.voxelplugineering.voxelsniper.api.event.bus.EventBus;
import com.voxelplugineering.voxelsniper.api.platform.PlatformProxy;
import com.voxelplugineering.voxelsniper.api.platform.TrivialPlatformProxy;
import com.voxelplugineering.voxelsniper.api.service.Service;
import com.voxelplugineering.voxelsniper.api.service.ServiceManager;
import com.voxelplugineering.voxelsniper.api.service.ServiceProvider;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceFactory;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceProvider;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceReader;
import com.voxelplugineering.voxelsniper.api.service.scheduler.Scheduler;
import com.voxelplugineering.voxelsniper.api.util.text.TextFormatProxy;
import com.voxelplugineering.voxelsniper.brushes.BrushManagerService;
import com.voxelplugineering.voxelsniper.brushes.CommonBrushManager;
import com.voxelplugineering.voxelsniper.command.CommandHandler;
import com.voxelplugineering.voxelsniper.commands.AliasCommand;
import com.voxelplugineering.voxelsniper.commands.BrushCommand;
import com.voxelplugineering.voxelsniper.commands.HelpCommand;
import com.voxelplugineering.voxelsniper.commands.MaskMaterialCommand;
import com.voxelplugineering.voxelsniper.commands.MaterialCommand;
import com.voxelplugineering.voxelsniper.commands.RedoCommand;
import com.voxelplugineering.voxelsniper.commands.ResetCommand;
import com.voxelplugineering.voxelsniper.commands.UndoCommand;
import com.voxelplugineering.voxelsniper.commands.VSCommand;
import com.voxelplugineering.voxelsniper.config.BaseConfiguration;
import com.voxelplugineering.voxelsniper.config.ConfigurationService;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.event.bus.AsyncEventBus;
import com.voxelplugineering.voxelsniper.event.bus.EventBusService;
import com.voxelplugineering.voxelsniper.event.handler.CommonEventHandler;
import com.voxelplugineering.voxelsniper.logging.CommonLoggingDistributor;
import com.voxelplugineering.voxelsniper.registry.vsl.ArgumentParsers;
import com.voxelplugineering.voxelsniper.service.persistence.DataSourceFactoryService;
import com.voxelplugineering.voxelsniper.service.persistence.FileDataSource;
import com.voxelplugineering.voxelsniper.service.persistence.JsonDataSourceReader;
import com.voxelplugineering.voxelsniper.service.persistence.MemoryContainer;
import com.voxelplugineering.voxelsniper.service.persistence.StandardOutDataSource;
import com.voxelplugineering.voxelsniper.util.AnnotationHelper;
import com.voxelplugineering.voxelsniper.util.defaults.DefaultAliasBuilder;
import com.voxelplugineering.voxelsniper.world.queue.CommonOfflineUndoHandler;

/**
 * The core service provider.
 */
public class CoreServiceProvider extends ServiceProvider
{

    /**
     * Creates the {@link CoreServiceProvider}.
     */
    public CoreServiceProvider()
    {
        super(ServiceProvider.Type.CORE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerNewServices(ServiceManager manager)
    {
        manager.registerService("logger");
        manager.registerService("persistence");
        manager.registerService("config");
        manager.registerService("formatProxy");
        manager.registerService("eventBus");
        manager.registerService("aliasRegistry");
        manager.registerService("platformProxy");
        manager.registerService("permissionProxy");
        manager.registerService("materialRegistry");
        manager.registerService("worldRegistry");
        manager.registerService("playerRegistry");
        manager.registerService("biomeRegistry");
        manager.registerService("globalBrushManager");
        manager.registerService("commandHandler");
        manager.registerService("scheduler");
        manager.registerService("offlineUndoHandler");
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder("logger")
    public Service buildLogger()
    {
        return new CommonLoggingDistributor();
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder("persistence")
    public Service buildPersistence()
    {
        return new DataSourceFactoryService();
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder("config")
    public Service buildConfig()
    {
        return new ConfigurationService();
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder("formatProxy")
    public Service buildFormatProxy()
    {
        return new TextFormatProxy.TrivialTextFormatProxy();
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder("eventBus")
    public Service buildEventBus()
    {
        return new EventBusService(new AsyncEventBus());
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder("aliasRegistry")
    public Service buildAliasRegistry()
    {
        return new AliasHandlerService(new CommonAliasHandler(new AliasOwner.GunsmithAliasOwner()));
    }

    /**
     * Init hook
     * 
     * @param service The service
     */
    @InitHook("eventBus")
    public void initEventBus(Service service)
    {
        ((EventBus) service).register(new CommonEventHandler());
    }

    /**
     * Init hook
     * 
     * @param service The service
     */
    @InitHook("persistence")
    public void initPersistence(Service service)
    {
        DataSourceFactory factory = (DataSourceFactory) service;
        factory.register("stdout", StandardOutDataSource.class, StandardOutDataSource.BUILDER);
        factory.register("file", FileDataSource.class, FileDataSource.BUILDER);
        factory.register("json", JsonDataSourceReader.class, JsonDataSourceReader.BUILDER);
    }

    /**
     * Init hook
     * 
     * @param service The service
     */
    @InitHook("config")
    public void initConfig(Service service)
    {
        Configuration configuration = (Configuration) service;
        configuration.registerContainer(BaseConfiguration.class);
        configuration.registerContainer(VoxelSniperConfiguration.class);
    }

    /**
     * Init hook
     * 
     * @param service The service
     */
    @InitHook("aliasRegistry")
    public void initAlias(Service service)
    {
        ((AliasHandler) service).registerTarget("brush");
        ((AliasHandler) service).registerTarget("material");

        DefaultAliasBuilder.loadDefaultAliases(((AliasHandler) service));
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder("globalBrushManager")
    public Service getGlobalBrushManager()
    {
        return new BrushManagerService(new CommonBrushManager());
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder("commandHandler")
    public Service getCommandHandler()
    {
        return new CommandHandler();
    }

    /**
     * Init hook
     * 
     * @param service The service
     */
    @InitHook("commandHandler")
    public void registerCommands(Service service)
    {
        CommandHandler cmd = (CommandHandler) service;

        cmd.registerCommand(new BrushCommand());
        cmd.registerCommand(new MaterialCommand());
        cmd.registerCommand(new MaskMaterialCommand());
        cmd.registerCommand(new VSCommand());
        cmd.registerCommand(new AliasCommand());
        cmd.registerCommand(new HelpCommand());
        cmd.registerCommand(new ResetCommand());
        cmd.registerCommand(new UndoCommand());
        cmd.registerCommand(new RedoCommand());
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder("offlineUndoHandler")
    public Service getOfflineUndo()
    {
        return new CommonOfflineUndoHandler();
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder("platformProxy")
    public Service getTrivialPlatform()
    {
        return new TrivialPlatformProxy();
    }

    /**
     * Post init
     */
    @PostInit
    public void post()
    {
        ArgumentParsers.init();

        PlatformProxy proxy = Gunsmith.getPlatformProxy();
        Configuration configuration = Gunsmith.getConfiguration();
        Optional<DataSourceReader> oconfig = proxy.getConfigDataSource();
        DataSourceReader config = null;

        if (!oconfig.isPresent())
        {
            DataSourceProvider provider = Gunsmith.getPlatformProxy().getRootDataSourceProvider();
            DataContainer args = new MemoryContainer("");
            DataSourceReader reader = provider.getWithReader("VoxelSniperConfiguration.json", JsonDataSourceReader.class, args).orNull();
            if (reader != null)
            {
                ((JsonDataSourceReader) reader).setPrettyOutput(true);
                config = reader;
            }
        } else
        {
            config = (DataSourceReader) oconfig.get();
        }
        if (config != null)
        {
            try
            {
                if (config.exists())
                {
                    Gunsmith.getLogger().info("Loading config from " + config.getName().or("an unknown source"));
                    DataContainer values = config.read();
                    Optional<AbstractConfigurationContainer> container = configuration.getContainer("VoxelSniperConfiguration");
                    if (container.isPresent())
                    {
                        container.get().fromContainer(values);
                        configuration.refreshContainer("VoxelSniperConfiguration");
                    } else
                    {
                        Gunsmith.getLogger().warn("Could not find config container for VoxelSniperConfiguration");
                    }
                } else
                {

                    Optional<AbstractConfigurationContainer> container = configuration.getContainer("VoxelSniperConfiguration");
                    if (container.isPresent())
                    {
                        Gunsmith.getLogger().info("Saving config to " + config.getName().or("an unknown source"));
                        config.write(container.get());
                    }
                }
            } catch (IOException e)
            {
                Gunsmith.getLogger().error(e, "Error loading configuration values.");
            }
        } else
        {
            Gunsmith.getLogger().warn("Could not find the configuration data source, and no fallback could be created.");
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
        }

        if (Gunsmith.getScheduler() != null)
        {
            Gunsmith.getScheduler().startSynchronousTask(aliasTask, configuration.get("aliasInterval", int.class).or(30000));
            Gunsmith.getScheduler().startSynchronousTask(new ChangeQueueTask(), configuration.get("changeInterval", int.class).or(100));
        }*/
    }

    /**
     * Stop hook
     */
    @PreStop
    public void onStop()
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

        Scheduler scheduler = Gunsmith.getScheduler();
        if (scheduler != null)
        {
            scheduler.stopAllTasks();
        }
        AnnotationHelper.clean();
    }

}
