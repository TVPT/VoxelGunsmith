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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.thevoxelbox.vsl.api.IGraphCompilerFactory;
import com.thevoxelbox.vsl.classloader.GraphCompilerFactory;
import com.voxelplugineering.voxelsniper.api.IBrush;
import com.voxelplugineering.voxelsniper.api.IBrushLoader;
import com.voxelplugineering.voxelsniper.api.IBrushManager;
import com.voxelplugineering.voxelsniper.api.IConfiguration;
import com.voxelplugineering.voxelsniper.api.ILogger;
import com.voxelplugineering.voxelsniper.api.ILoggingDistributor;
import com.voxelplugineering.voxelsniper.api.IPlatformProxy;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.api.IVoxelSniper;
import com.voxelplugineering.voxelsniper.common.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.common.command.CommandHandler;
import com.voxelplugineering.voxelsniper.common.event.CommonEventHandler;
import com.voxelplugineering.voxelsniper.config.BaseConfiguration;
import com.voxelplugineering.voxelsniper.config.Configuration;
import com.voxelplugineering.voxelsniper.config.JsonConfigurationLoader;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.logging.CommonLoggingDistributor;
import com.voxelplugineering.voxelsniper.util.BrushCompiler;
import com.voxelplugineering.voxelsniper.util.GunsmithTypes;
import com.voxelplugineering.voxelsniper.world.ChangeQueueTask;

/**
 * The Core of VoxelGunsmith, provides access to handlers and validates initialization is done completely and correctly.
 */
public final class Gunsmith
{

    /**
     * The core of the specific implementation.
     */
    private static IVoxelSniper plugin = null;
    /**
     * The global brush manager, manages the loading and registration of brushes which are available to all users of a multi-user environment. In a
     * single-user environment this would be the core brush manager for the user.
     */
    private static IBrushManager globalBrushManager = null;
    /**
     * The default brush loader. The default source from which brushes are loaded, typically this is the brush loader used by the global brush
     * manager.
     */
    private static IBrushLoader defaultBrushLoader = null;
    /**
     * The command handler, manages the registration of both commands and handlers, and distributes invocations of the commands to its registered
     * handlers. Also handles automatic command argument validation.
     */
    private static CommandHandler commandHandler = null;
    /**
     * The handler for the default behavior for events.
     */
    private static CommonEventHandler defaultEventHandler = null;
    /**
     * The internal event bus for events within Gunsmith.
     */
    private static EventBus eventBus = null;
    /**
     * The global configuration container for Gunsmith.
     */
    private static IConfiguration configuration = null;
    /**
     * The global log distributor for Gunsmith.
     */
    private static ILoggingDistributor logDistributor = null;
    /**
     * The VSL compiler factory for the Brush Managers to reference.
     */
    private static GraphCompilerFactory compilerFactory = null;
    private static AliasHandler globalAliasRegistries = null;
    private static ExecutorService eventBusExecutor;
    private static IPlatformProxy platformProxy = null;

    /**
     * The initialization state of Gunsmith.
     */
    private static boolean isPluginEnabled = false;

    /**
     * Sets the implementation of {@link com.voxelplugineering.voxelsniper.api.IVoxelSniper} to be used.
     *
     * @param sniper Implementation of IVoxelSniper to be used
     */
    public static void setPlugin(IVoxelSniper sniper)
    {
        checkNotNull(sniper, "Cannot set a null plugin!");
        check();
        Gunsmith.plugin = sniper;
    }

    /**
     * Sets the Platform proxy to be used.
     *
     * @param proxy Implementation of IPlatformProxy to be used
     */
    public static void setPlatform(IPlatformProxy proxy)
    {
        checkNotNull(proxy, "Cannot set a null proxy!");
        check();
        Gunsmith.platformProxy = proxy;
    }

    /**
     * Sets the implementation of {@link com.voxelplugineering.voxelsniper.api.IBrushManager} to be used.
     *
     * @param brushManager Implementation of IBrushManager to be used, cannot be null
     */
    public static void setGlobalBrushManager(IBrushManager brushManager)
    {
        checkNotNull(brushManager, "Cannot set a null BrushManager!");
        check();
        Gunsmith.globalBrushManager = brushManager;
    }

    /**
     * Sets the implementation of {@link com.voxelplugineering.voxelsniper.api.IBrushLoader} to be used.
     *
     * @param brushLoader Implementation of IBrushLoader to be used, cannot be null
     */
    public static void setDefaultBrushLoader(IBrushLoader brushLoader)
    {
        checkNotNull(brushLoader, "Cannot set a null BrushLoader!");
        check();
        Gunsmith.defaultBrushLoader = brushLoader;
    }

    /**
     * Sets the implementation of {@link com.voxelplugineering.voxelsniper.common.command.CommandHandler} to be used.
     *
     * @param command Implementation of CommandHandler to be used, cannot be null
     */
    public static void setCommandHandler(CommandHandler command)
    {
        checkNotNull(command, "Cannot set a null CommandHandler!");
        check();
        Gunsmith.commandHandler = command;
    }

    /**
     * Gets the instance of the implementation of IVoxelSniper.
     *
     * @return The instance of the implementation of IVoxelSniper
     */
    public static IVoxelSniper getVoxelSniper()
    {
        return plugin;
    }

    /**
     * Gets the instance of the implementation of IPlatformProxy.
     *
     * @return The instance of the implementation of IPlatformProxy
     */
    public static IPlatformProxy getPlatformProxy()
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
    public static IBrushLoader getDefaultBrushLoader()
    {
        return defaultBrushLoader;
    }

    /**
     * Gets the instance of the implementation of IBrushLoader.
     *
     * @return The instance of the implementation of IBrushLoader
     */
    public static IBrushManager getGlobalBrushManager()
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
    public static IConfiguration getConfiguration()
    {
        return configuration;
    }

    /**
     * Returns the global logger for Gunsmith.
     * 
     * @return the logger
     */
    public static ILogger getLogger()
    {
        return logDistributor;
    }

    /**
     * Returns the logging distribution manager for Gunsmith.
     * 
     * @return the logging distributor
     */
    public static ILoggingDistributor getLoggingDistributor()
    {
        return logDistributor;
    }

    /**
     * Returns the compiler factory for compiling VSL scripts.
     * 
     * @return the factory
     */
    public static IGraphCompilerFactory getCompilerFactory()
    {
        return compilerFactory;
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
     * Should be called at the start of the initialization process. Sets up default states before the specific implementation registers its overrides.
     * 
     * @param base The root data folder for Gunsmith's data
     */
    public static void beginInit(File base)
    {
        check();

        //Create standard log distributor
        logDistributor = new CommonLoggingDistributor();
        logDistributor.init();
        //System.setProperty("voxel.log.location", base.getAbsolutePath());
        //File config = new File(base.getAbsolutePath(), "log4j2.xml");
        //System.out.println("log4j config: " + config.getAbsolutePath());
        //System.setProperty("log4j.configurationFile", config.getAbsolutePath());
        //logDistributor.registerLogger(new Log4jLogger(LogManager.getLogger(Gunsmith.class)), "log4j");

        getLogger().info(
                "Starting Gunsmith initialization process. ("
                        + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(new Date(System.currentTimeMillis())) + ")");

        //Register vsl types for common impl types and set library path
        GunsmithTypes.init();

        //Create the eventBus for all Gunsmith events
        eventBus = new AsyncEventBus(eventBusExecutor = java.util.concurrent.Executors.newCachedThreadPool());

        defaultEventHandler = new CommonEventHandler();
        eventBus.register(defaultEventHandler);
        //default event handler is registered here so that if a plugin wishes it can unregister the
        //event handler and register its own in its place

        configuration = new Configuration();
        configuration.registerContainer(BaseConfiguration.class);
        configuration.registerContainer(VoxelSniperConfiguration.class);
        //configuration is also setup here so that any values can be overwritten from the specific impl

        //Setup the VSL graph compiler
        compilerFactory = new GraphCompilerFactory();
        compilerFactory.registerCompiler(IBrush.class, new BrushCompiler()); //the compiler for all brushes

        globalAliasRegistries = new AliasHandler();
        globalAliasRegistries.registerTarget("brush");

    }

    /**
     * Finalize the initialization process. Contains validations to ensure that the initialization was completed correctly and completely.
     */
    public static void finish()
    {
        check();
        if (plugin == null || globalBrushManager == null || defaultBrushLoader == null || logDistributor == null)
        {
            isPluginEnabled = false;
            throw new IllegalStateException("VoxelSniper was not properly setup while loading");
        }

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

        File globalAliases = new File(platformProxy.getDataFolder(), "aliases.json");
        if (globalAliases.exists())
        {
            try
            {
                globalAliasRegistries.load(globalAliases);
            } catch (IOException e)
            {
                getLogger().error(e, "Error loading global aliases");
            }
        }

        getVoxelSniper().getSchedulerProxy().startSynchronousTask(new ChangeQueueTask(), 100);

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
     * Stops Gunsmith and dereferences all handlers and managers.
     */
    public static void stop()
    {
        if (!isPluginEnabled)
        {
            throw new IllegalStateException("VoxelSniper has not been enabled yet, cannot stop!");
        }

        File globalAliases = new File(platformProxy.getDataFolder(), "aliases.json");

        try
        {
            if (!globalAliases.exists())
            {
                globalAliases.createNewFile();
            }
            getGlobalAliasHandler().save(globalAliases);
        } catch (IOException e)
        {
            getLogger().error(e, "Error saving global aliases");
        }

        //save all player's personal aliases
        for (ISniper player : getVoxelSniper().getPlayerRegistry().getRegisteredValues())
        {
            File playerFolder = new File(Gunsmith.platformProxy.getDataFolder(), "players/" + player.getName());
            File aliases = new File(playerFolder, "aliases.json");

            try
            {
                if (aliases.exists())
                {
                    aliases.createNewFile();
                }
                player.getPersonalAliasHandler().save(aliases);
            } catch (IOException e)
            {
                Gunsmith.getLogger().error(e, "Error saving player aliases!");
            }
        }

        if (eventBusExecutor != null)
        {
            eventBusExecutor.shutdown();
            eventBusExecutor = null;
        }

        isPluginEnabled = false;
        plugin = null;
        defaultBrushLoader = null;
        defaultEventHandler = null;
        globalBrushManager = null;
        eventBus = null;
        commandHandler = null;
        if (logDistributor != null)
        {
            logDistributor.stop();
        }
        logDistributor = null;
    }
}
