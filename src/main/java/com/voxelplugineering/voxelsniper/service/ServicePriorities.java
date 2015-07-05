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
package com.voxelplugineering.voxelsniper.service;

/**
 * An enumeration of service builder and init hook priorities.
 */
public final class ServicePriorities
{
    /** DataSourceFactory service builder priority. */
    public static final int DATA_SOURCE_FACTORY_PRIORITY = -1000;
    /** TextFormatParser service builder priority. */
    public static final int TEXT_FORMAT_PRIORITY = 0;
    /** Configuration service builder priority. */
    public static final int CONFIGURATION_PRIORITY = 1000;
    /** EventBus service builder priority. */
    public static final int EVENT_BUS_PRIORITY = 2000;
    /** PlatformProxy service builder priority. */
    public static final int PLATFORM_PROXY_PRIORITY = 4000;
    /** GlobalAliasHandler service builder priority. */
    public static final int GLOBAL_ALIAS_HANDLER_PRIORITY = 5000;
    /** PermissionProxy service builder priority. */
    public static final int PERMISSION_PROXY_PRIORITY = 7000;
    /** GlobalBrushManager service builder priority. */
    public static final int GLOBAL_BRUSH_MANAGER_PRIORITY = 7500;
    /** CommandHandler service builder priority. */
    public static final int COMMAND_HANDLER_PRIORITY = 10000;
    /** OfflineUndoHandler service builder priority. */
    public static final int UNDO_HANDLER_PRIORITY = 13000;

    /**
     * No instance for you.
     */
    private ServicePriorities()
    {

    }
}
