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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import com.voxelplugineering.voxelsniper.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.alias.AliasSaveTask;
import com.voxelplugineering.voxelsniper.api.alias.AliasOwner;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.config.Configuration;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.event.bus.EventBus;
import com.voxelplugineering.voxelsniper.api.logging.Logger;
import com.voxelplugineering.voxelsniper.api.logging.LoggingDistributor;
import com.voxelplugineering.voxelsniper.api.permissions.PermissionProxy;
import com.voxelplugineering.voxelsniper.api.platform.PlatformProvider;
import com.voxelplugineering.voxelsniper.api.platform.PlatformProxy;
import com.voxelplugineering.voxelsniper.api.registry.BiomeRegistry;
import com.voxelplugineering.voxelsniper.api.registry.MaterialRegistry;
import com.voxelplugineering.voxelsniper.api.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.api.registry.WorldRegistry;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceProvider;
import com.voxelplugineering.voxelsniper.api.service.scheduler.Scheduler;
import com.voxelplugineering.voxelsniper.api.util.text.TextFormatProxy;
import com.voxelplugineering.voxelsniper.api.world.queue.OfflineUndoHandler;
import com.voxelplugineering.voxelsniper.command.CommandHandler;
import com.voxelplugineering.voxelsniper.config.BaseConfiguration;
import com.voxelplugineering.voxelsniper.config.ConfigurationManager;
import com.voxelplugineering.voxelsniper.config.JsonConfigurationLoader;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.event.bus.AsyncEventBus;
import com.voxelplugineering.voxelsniper.event.handler.CommonEventHandler;
import com.voxelplugineering.voxelsniper.logging.CommonLoggingDistributor;
import com.voxelplugineering.voxelsniper.registry.vsl.ArgumentParsers;
import com.voxelplugineering.voxelsniper.service.persistence.JsonDataSource;
import com.voxelplugineering.voxelsniper.util.AnnotationHelper;
import com.voxelplugineering.voxelsniper.util.defaults.DefaultAliasBuilder;
import com.voxelplugineering.voxelsniper.world.queue.ChangeQueueTask;
import com.voxelplugineering.voxelsniper.world.queue.CommonOfflineUndoHandler;

/**
 * The Core of VoxelGunsmith, provides access to handlers and validates
 * initialization is done completely and correctly.
 */
public final class Gunsmith_legacy
{

    private static BrushManager globalBrushManager;
    private static DataSourceProvider defaultBrushLoader;
    private static CommandHandler commandHandler;
    private static CommonEventHandler defaultEventHandler;
    private static EventBus eventBus;
    static Configuration configuration;
    static LoggingDistributor logDistributor;
    private static AliasHandler globalAliasRegistries;
    private static ExecutorService eventBusExecutor;
    private static PlatformProxy platformProxy;
    private static Thread mainThread;
    private static ClassLoader classloader;
    private static MaterialRegistry<?> defaultMaterialRegistry;
    private static WorldRegistry<?> worldRegistry;
    private static PermissionProxy permissionsProxy;
    private static PlayerRegistry<?> sniperRegistry;
    private static Scheduler schedulerProxy;
    private static BiomeRegistry<?> biomeRegistry;
    private static File dataFolder;
    private static AliasSaveTask aliasTask;
    private static TextFormatProxy formatProxy;
    private static OfflineUndoHandler offlineUndo;

    /**
     * The initialization state of Gunsmith.
     */
    private static boolean isPluginEnabled = false;

    /**
     * Gets the instance of the implementation of IPlatformProxy.
     * 
     * @return The instance of the implementation of IPlatformProxy
     */
    public static PlatformProxy getPlatformProxy()
    {
        return platformProxy;
    }

    /**
     * Returns the initialization state of Gunsmith.
     * 
     * @return the initialization state
     */
    public static boolean isEnabled()
    {
        return isPluginEnabled;
    }

    /**
     * Gets the instance of the implementation of IBrushLoader.
     * 
     * @return The instance of the implementation of IBrushLoader
     */
    public static DataSourceProvider getDefaultBrushLoader()
    {
        return defaultBrushLoader;
    }

    /**
     * Gets the instance of the implementation of IBrushLoader.
     * 
     * @return The instance of the implementation of IBrushLoader
     */
    public static BrushManager getGlobalBrushManager()
    {
        return globalBrushManager;
    }

    /**
     * Gets the instance of the implementation of EventBus.
     * 
     * @return The instance of the implementation of EventBus
     */
    public static EventBus getEventBus()
    {
        return eventBus;
    }

    /**
     * Gets the instance of the implementation of CommandHandler.
     * 
     * @return The instance of the implementation of CommandHandler
     */
    public static CommandHandler getCommandHandler()
    {
        return commandHandler;
    }

    /**
     * Gets the instance of the implementation of CommonEventHandler.
     * 
     * @return The instance of the implementation of CommonEventHandler
     */
    public static CommonEventHandler getDefaultEventHandler()
    {
        return defaultEventHandler;
    }

    /**
     * Gets the instance of the implementation of IConfiguration.
     * 
     * @return The instance of the implementation of IConfiguration
     */
    public static Configuration getConfiguration()
    {
        if (configuration == null)
        {
            DefaultSetupProxy.setupConfiguration();
        }
        return configuration;
    }

    /**
     * Returns the global logger for Gunsmith.
     * 
     * @return the logger
     */
    public static Logger getLogger()
    {
        if (logDistributor == null)
        {
            DefaultSetupProxy.setupLogger();
        }
        return logDistributor;
    }

    /**
     * Returns the logging distribution manager for Gunsmith.
     * 
     * @return the logging distributor
     */
    public static LoggingDistributor getLoggingDistributor()
    {
        return logDistributor;
    }

    /**
     * Returns the global alias handler.
     * 
     * @return the global alias handler
     */
    public static AliasHandler getGlobalAliasHandler()
    {
        return globalAliasRegistries;
    }

    /**
     * Returns the main execution thread.
     * 
     * @return The main thread
     */
    public static Thread getMainThread()
    {
        return mainThread;
    }

    /**
     * Returns the base folder for VoxelSniper data
     * 
     * @return The data folder
     */
    public static File getDataFolder()
    {
        return dataFolder;
    }

    /**
     * The classloader Gunsmith was loaded with.
     * 
     * @return The classloader
     */
    public static ClassLoader getClassLoader()
    {
        return classloader;
    }

    /**
     * Returns the default material registry.
     * 
     * @param <T> The material type
     * @return The default material registry
     */
    @SuppressWarnings("unchecked")
    public static <T> MaterialRegistry<T> getDefaultMaterialRegistry()
    {
        return (MaterialRegistry<T>) defaultMaterialRegistry;
    }

    /**
     * Returns the world registry.
     * 
     * @param <T> The world type
     * @return The world registry
     */
    @SuppressWarnings("unchecked")
    public static <T> WorldRegistry<T> getWorldRegistry()
    {
        return (WorldRegistry<T>) worldRegistry;
    }

    /**
     * Returns the permissions proxy.
     * 
     * @return The permissions proxy
     */
    public static PermissionProxy getPermissionsProxy()
    {
        return permissionsProxy;
    }

    /**
     * Returns the player registry.
     * 
     * @param <T> The player type
     * @return The player registry
     */
    @SuppressWarnings("unchecked")
    public static <T> PlayerRegistry<T> getPlayerRegistry()
    {
        return (PlayerRegistry<T>) sniperRegistry;
    }

    /**
     * Returns the scheduler proxy.
     * 
     * @return The scheduler
     */
    public static Scheduler getScheduler()
    {
        return schedulerProxy;
    }

    /**
     * Returns the biome registry.
     * 
     * @param <T> The biome type
     * @return The biome registry
     */
    @SuppressWarnings("unchecked")
    public static <T> BiomeRegistry<T> getBiomeRegistry()
    {
        return (BiomeRegistry<T>) biomeRegistry;
    }

    /**
     * Gets the alias save handler.
     * 
     * @return The alias save handler
     */
    public static AliasSaveTask getAliasSaveHandler()
    {
        return aliasTask;
    }

    /**
     * Gets the text format proxy.
     * 
     * @return The format proxy
     */
    public static TextFormatProxy getTextFormatProxy()
    {
        if (formatProxy == null)
        {
            formatProxy = new TextFormatProxy.TrivialTextFormatProxy();
        }
        return formatProxy;
    }

    /**
     * Gets the {@link OfflineUndoHandler}. This handles the undo queues for all
     * disconnected players until invalidated according to the individual
     * handler's policy.
     * 
     * @return The undo handler
     */
    public static OfflineUndoHandler getOfflineUndoHandler()
    {
        return offlineUndo;
    }

    /**
     * Should be called at the start of the initialization process. Sets up
     * default states before the specific implementation registers its
     * overrides.
     * 
     * @param base The root data folder for Gunsmith's data
     * @param provider The platform's provider
     */
    public static void beginInit(File base, PlatformProvider provider)
    {
        check();
        dataFolder = base;
        // Create standard log distributor
        DefaultSetupProxy.setupLogger();
        // System.setProperty("voxel.log.location", base.getAbsolutePath());
        // File config = new File(base.getAbsolutePath(), "log4j2.xml");
        // System.out.println("log4j config: " + config.getAbsolutePath());
        // System.setProperty("log4j.configurationFile",
        // config.getAbsolutePath());
        // logDistributor.registerLogger(new
        // Log4jLogger(LogManager.getLogger(Gunsmith.class)), "log4j");

        // TODO setup log4j logging properly

        getLogger().info(
                "Starting Gunsmith initialization process. ("
                        + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(new Date(System.currentTimeMillis())) + ")");

        // The Text Format Proxy needs to be set early so values loaded to
        // configuration from the default set are correctly converted.
        formatProxy = provider.getFormatProxy();
        getLogger().info("Set format proxy to " + formatProxy.getClass().getName());

        // Create the eventBus for all Gunsmith events
        eventBus = new AsyncEventBus();

        defaultEventHandler = new CommonEventHandler();
        eventBus.register(defaultEventHandler);
        // default event handler is registered here so that if a plugin wishes
        // it can unregister the
        // event handler and register its own in its place

        DefaultSetupProxy.setupConfiguration();
        // configuration is also setup here so that any values can be
        // overwritten from the specific impl

        globalAliasRegistries = new AliasHandler(new AliasOwner.GunsmithAliasOwner());
        globalAliasRegistries.registerTarget("brush");

        mainThread = Thread.currentThread();
        classloader = Gunsmith.class.getClassLoader();

        Gunsmith.getLoggingDistributor().registerLogger(provider.getLogger(), provider.getLoggerName());

        platformProxy = provider.getPlatformProxy();
        checkRef(platformProxy);
        platformProxy.getDataFolder().mkdirs();

        provider.registerConfiguration();

        defaultMaterialRegistry = provider.getDefaultMaterialRegistry();
        checkRef(defaultMaterialRegistry);

        worldRegistry = provider.getWorldRegistry();
        checkRef(worldRegistry);

        permissionsProxy = provider.getPermissionProxy();
        checkRef(permissionsProxy);

        sniperRegistry = provider.getSniperRegistry();
        checkRef(sniperRegistry);

        provider.registerEventProxies();

        defaultBrushLoader = provider.getDefaultBrushLoader();
        checkRef(defaultBrushLoader);

        globalBrushManager = provider.getGlobalBrushManager();
        checkRef(globalBrushManager);

        commandHandler = provider.getCommandHandler();
        checkRef(commandHandler);

        schedulerProxy = provider.getSchedulerProxy();
        checkRef(schedulerProxy);

        biomeRegistry = provider.getBiomeRegistry();

        ArgumentParsers.init();

        offlineUndo = new CommonOfflineUndoHandler();

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
        }

        aliasTask = new AliasSaveTask();

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

        schedulerProxy.startSynchronousTask(new ChangeQueueTask(), 100);
        schedulerProxy.startSynchronousTask(aliasTask, 30000);

        getLogger().info("Gunsmith initialization finalized.");
        isPluginEnabled = true;

    }

    /**
     * Checks that Gunsmith is still in the initialization phase.
     */
    private static void check()
    {
        if (isPluginEnabled)
        {
            throw new IllegalStateException("VoxelSniper is already enabled!");
        }
    }

    /**
     * Checks if a reference is null and throws a state exception if it is.
     * 
     * @param ref The reference to check
     */
    private static void checkRef(Object ref)
    {
        if (ref == null)
        {
            isPluginEnabled = false;
            throw new IllegalStateException("VoxelSniper was not properly setup while loading");
        }
    }

    /**
     * Stops Gunsmith and dereferences all handlers and managers.
     */
    public static void stop()
    {
        if (!isPluginEnabled)
        {
            throw new IllegalStateException("VoxelSniper has not been enabled yet, cannot stop!");
        }

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
        }

        if (eventBusExecutor != null)
        {
            eventBusExecutor.shutdownNow();
            eventBusExecutor = null;
        }

        isPluginEnabled = false;
        globalBrushManager = null;
        defaultBrushLoader = null;
        commandHandler = null;
        defaultEventHandler = null;
        eventBus = null;
        configuration = null;
        logDistributor = null;
        globalAliasRegistries = null;
        platformProxy = null;
        mainThread = null;
        classloader = null;
        defaultMaterialRegistry = null;
        worldRegistry = null;
        permissionsProxy = null;
        sniperRegistry = null;
        if (schedulerProxy != null)
        {
            schedulerProxy.stopAllTasks();
        }
        schedulerProxy = null;
        aliasTask = null;
        formatProxy = null;
        AnnotationHelper.clean();
        offlineUndo = null;
    }

    private static class DefaultSetupProxy
    {

        public static void setupConfiguration()
        {
            if (configuration != null)
            {
                return;
            }
            configuration = new ConfigurationManager();
            configuration.registerContainer(BaseConfiguration.class);
            configuration.registerContainer(VoxelSniperConfiguration.class);
        }

        public static void setupLogger()
        {
            if (logDistributor != null)
            {
                return;
            }
            logDistributor = new CommonLoggingDistributor();
        }

    }

}
