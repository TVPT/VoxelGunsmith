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
import com.voxelplugineering.voxelsniper.api.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.api.alias.AliasOwner;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.commands.CommandHandler;
import com.voxelplugineering.voxelsniper.api.config.AbstractConfigurationContainer;
import com.voxelplugineering.voxelsniper.api.config.Configuration;
import com.voxelplugineering.voxelsniper.api.event.bus.EventBus;
import com.voxelplugineering.voxelsniper.api.logging.LoggingDistributor;
import com.voxelplugineering.voxelsniper.api.permissions.PermissionProxy;
import com.voxelplugineering.voxelsniper.api.platform.PlatformProxy;
import com.voxelplugineering.voxelsniper.api.platform.TrivialPlatformProxy;
import com.voxelplugineering.voxelsniper.api.registry.BiomeRegistry;
import com.voxelplugineering.voxelsniper.api.registry.MaterialRegistry;
import com.voxelplugineering.voxelsniper.api.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.api.registry.WorldRegistry;
import com.voxelplugineering.voxelsniper.api.service.Service;
import com.voxelplugineering.voxelsniper.api.service.ServiceManager;
import com.voxelplugineering.voxelsniper.api.service.ServiceProvider;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceFactory;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceProvider;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceReader;
import com.voxelplugineering.voxelsniper.api.service.scheduler.Scheduler;
import com.voxelplugineering.voxelsniper.api.util.text.TextFormatParser;
import com.voxelplugineering.voxelsniper.api.world.queue.OfflineUndoHandler;
import com.voxelplugineering.voxelsniper.core.brushes.ArgumentParsers;
import com.voxelplugineering.voxelsniper.core.brushes.CommonBrushManager;
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
import com.voxelplugineering.voxelsniper.core.service.AliasHandlerService;
import com.voxelplugineering.voxelsniper.core.service.BrushManagerService;
import com.voxelplugineering.voxelsniper.core.service.CommandHandlerService;
import com.voxelplugineering.voxelsniper.core.service.ConfigurationService;
import com.voxelplugineering.voxelsniper.core.service.DataSourceFactoryService;
import com.voxelplugineering.voxelsniper.core.service.EventBusService;
import com.voxelplugineering.voxelsniper.core.service.LoggingDistributorService;
import com.voxelplugineering.voxelsniper.core.service.OfflineUndoHandlerService;
import com.voxelplugineering.voxelsniper.core.service.alias.CommonAliasHandler;
import com.voxelplugineering.voxelsniper.core.service.eventbus.AsyncEventBus;
import com.voxelplugineering.voxelsniper.core.service.persistence.FileDataSource;
import com.voxelplugineering.voxelsniper.core.service.persistence.JsonDataSourceReader;
import com.voxelplugineering.voxelsniper.core.service.persistence.MemoryContainer;
import com.voxelplugineering.voxelsniper.core.service.persistence.StandardOutDataSource;
import com.voxelplugineering.voxelsniper.core.util.AnnotationHelper;
import com.voxelplugineering.voxelsniper.core.util.defaults.DefaultAliasBuilder;
import com.voxelplugineering.voxelsniper.core.world.queue.ChangeQueueTask;

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

    @Override
    public void registerNewServices(ServiceManager manager)
    {
        manager.registerService(LoggingDistributor.class);
        manager.registerService(DataSourceFactory.class);
        manager.registerService(Configuration.class);
        manager.registerService(TextFormatParser.class);
        manager.registerService(EventBus.class);
        manager.registerService(AliasHandler.class);
        manager.registerService(PlatformProxy.class);
        manager.registerService(PermissionProxy.class);
        manager.registerService(MaterialRegistry.class);
        manager.registerService(WorldRegistry.class);
        manager.registerService(PlayerRegistry.class);
        manager.registerService(BiomeRegistry.class);
        manager.registerService(BrushManager.class);
        manager.registerService(CommandHandler.class);
        manager.registerService(Scheduler.class);
        manager.registerService(OfflineUndoHandler.class);
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder(LoggingDistributor.class)
    public LoggingDistributor buildLogger()
    {
        return new LoggingDistributorService();
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder(DataSourceFactory.class)
    public DataSourceFactory buildPersistence()
    {
        return new DataSourceFactoryService();
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder(Configuration.class)
    public Configuration buildConfig()
    {
        return new ConfigurationService();
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder(TextFormatParser.class)
    public TextFormatParser buildFormatProxy()
    {
        return new TextFormatParser.TrivialTextFormatParser();
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder(EventBus.class)
    public EventBus buildEventBus()
    {
        return new EventBusService(new AsyncEventBus());
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder(AliasHandler.class)
    public AliasHandler buildAliasRegistry()
    {
        return new AliasHandlerService(new CommonAliasHandler(new AliasOwner.GunsmithAliasOwner()));
    }

    /**
     * Init hook
     * 
     * @param service The service
     */
    @InitHook(EventBus.class)
    public void initEventBus(EventBus service)
    {
        service.register(new CommonEventHandler());
    }

    /**
     * Init hook
     * 
     * @param factory The service
     */
    @InitHook(DataSourceFactory.class)
    public void initPersistence(DataSourceFactory factory)
    {
        factory.register("stdout", StandardOutDataSource.class, StandardOutDataSource.BUILDER);
        factory.register("file", FileDataSource.class, FileDataSource.BUILDER);
        factory.register("json", JsonDataSourceReader.class, JsonDataSourceReader.BUILDER);
    }

    /**
     * Init hook
     * 
     * @param configuration The service
     */
    @InitHook(Configuration.class)
    public void initConfig(Configuration configuration)
    {
        configuration.registerContainer(BaseConfiguration.class);
        configuration.registerContainer(VoxelSniperConfiguration.class);
    }

    /**
     * Init hook
     * 
     * @param service The service
     */
    @InitHook(AliasHandler.class)
    public void initAlias(AliasHandler service)
    {
        service.registerTarget("brush");
        service.registerTarget("material");

        DefaultAliasBuilder.loadDefaultAliases(service);
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder(BrushManager.class)
    public BrushManager getGlobalBrushManager()
    {
        return new BrushManagerService(new CommonBrushManager());
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder(CommandHandler.class)
    public CommandHandler getCommandHandler()
    {
        return new CommandHandlerService();
    }

    /**
     * Init hook
     * 
     * @param cmd The service
     */
    @InitHook(CommandHandler.class)
    public void registerCommands(CommandHandler cmd)
    {
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
    @Builder(OfflineUndoHandler.class)
    public Service getOfflineUndo()
    {
        return new OfflineUndoHandlerService();
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder(PlatformProxy.class)
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
            config = oconfig.get();
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
        }*/

        if (Gunsmith.getServiceManager().hasService(Scheduler.class))
        {
            //Gunsmith.getScheduler().startSynchronousTask(aliasTask, configuration.get("aliasInterval", int.class).or(30000));
            Gunsmith.getScheduler().startSynchronousTask(new ChangeQueueTask(), configuration.get("changeInterval", int.class).or(100));
        }
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

        if (Gunsmith.getServiceManager().hasService(Scheduler.class))
        {
            Gunsmith.getScheduler().stopAllTasks();
        }
        AnnotationHelper.clean();
    }

}
