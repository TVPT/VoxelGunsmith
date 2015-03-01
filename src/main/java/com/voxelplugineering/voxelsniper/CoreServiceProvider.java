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

import com.voxelplugineering.voxelsniper.alias.AliasHandlerService;
import com.voxelplugineering.voxelsniper.alias.AliasSaveTask;
import com.voxelplugineering.voxelsniper.alias.CommonAliasHandler;
import com.voxelplugineering.voxelsniper.api.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.api.alias.AliasOwner;
import com.voxelplugineering.voxelsniper.api.config.Configuration;
import com.voxelplugineering.voxelsniper.api.event.bus.EventBus;
import com.voxelplugineering.voxelsniper.api.service.Service;
import com.voxelplugineering.voxelsniper.api.service.ServiceManager;
import com.voxelplugineering.voxelsniper.api.service.ServiceProvider;
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
import com.voxelplugineering.voxelsniper.config.ConfigurationManager;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.event.bus.AsyncEventBus;
import com.voxelplugineering.voxelsniper.event.bus.EventBusService;
import com.voxelplugineering.voxelsniper.event.handler.CommonEventHandler;
import com.voxelplugineering.voxelsniper.logging.CommonLoggingDistributor;
import com.voxelplugineering.voxelsniper.registry.vsl.ArgumentParsers;
import com.voxelplugineering.voxelsniper.util.AnnotationHelper;
import com.voxelplugineering.voxelsniper.util.defaults.DefaultAliasBuilder;
import com.voxelplugineering.voxelsniper.world.queue.ChangeQueueTask;
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
    @Builder("config")
    public Service buildConfig()
    {
        return new ConfigurationManager();
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
     * Post init
     */
    @PostInit
    public void post()
    {
        ArgumentParsers.init();

        /* TODO persistence
         * 
        File config = new File(platformProxy.getDataFolder(), "VoxelSniperConfiguration.json");
        if (config.exists())
        {
            try
            {
                JsonConfigurationLoader.load(config, configuration, "VoxelSniperConfiguration");
            } catch (IllegalAccessException e)
            {
                getLogger().error(e, "Error loading configuration");
            } catch (IOException e)
            {
                getLogger().error(e, "Error loading configuration");
            }
        } else
        {
            try
            {
                getLogger().info("Creating new user configuration file: " + config.getAbsolutePath());
                JsonConfigurationLoader.save(config, configuration, "VoxelSniperConfiguration");
            } catch (InstantiationException e)
            {
                getLogger().error(e, "Error saving configuration");
            } catch (IllegalAccessException e)
            {
                getLogger().error(e, "Error saving configuration");
            } catch (IOException e)
            {
                getLogger().error(e, "Error saving configuration");
            }
        }*/

        Runnable aliasTask = new AliasSaveTask();

        /* TODO persistence
         * 
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
        if (Gunsmith.getScheduler() != null)
        {
            Gunsmith.getScheduler().startSynchronousTask(aliasTask, 30000);//TODO config for period

            Gunsmith.getScheduler().startSynchronousTask(new ChangeQueueTask(), 100);
        }
    }

    /**
     * Stop hook
     */
    @PreStop
    public void onStop()
    {
        /* TODO persistence
         * 
        File globalAliases = new File(platformProxy.getDataFolder(), "aliases.json");
        JsonDataSource data = new JsonDataSource(globalAliases);

        try
        {
            if (!globalAliases.exists())
            {
                globalAliases.createNewFile();
            }
            data.write(getGlobalAliasHandler());
        } catch (IOException e)
        {
            getLogger().error(e, "Error saving global aliases");
        }

        // save all player's personal aliases
        for (Player player : sniperRegistry.getAllPlayers())
        {
            File playerFolder = new File(Gunsmith.platformProxy.getDataFolder(), "players/" + player.getUniqueId().toString());
            File aliases = new File(playerFolder, "aliases.json");
            JsonDataSource playerData = new JsonDataSource(aliases);

            try
            {
                if (aliases.exists())
                {
                    aliases.createNewFile();
                }
                playerData.write(player.getPersonalAliasHandler());
            } catch (IOException e)
            {
                Gunsmith.getLogger().error(e, "Error saving player aliases!");
            }
        }*/

        Scheduler scheduler = Gunsmith.getScheduler();
        if (scheduler != null)
        {
            scheduler.stopAllTasks();
        }
        AnnotationHelper.clean();
    }

}
