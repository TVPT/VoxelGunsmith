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
package com.voxelplugineering.voxelsniper.entity;

/**
 * Various user action types.
 */
public enum Action
{
    /**
     * When the user has left-clicked air, usually pointing in a direction and not within range of
     * targeting a block.
     */
    LEFT_CLICK_AIR,
    /**
     * When the user has left-clicked a block being pointed at.
     */
    LEFT_CLICK_BLOCK,
    /**
     * When the user has middle-clicked air, usually pointing in a direction and not within range of
     * targeting a block.
     */
    MIDDLE_CLICK_AIR,
    /**
     * When the user has middle-clicked a block being pointed at.
     */
    MIDDLE_CLICK_BLOCK,
    /**
     * When the user has right-clicked air, usually pointing in a direction and not within range of
     * targeting a block.
     */
    RIGHT_CLICK_AIR,
    /**
     * When the user has right-clicked a block being pointed at.
     */
    RIGHT_CLICK_BLOCK, ;
}
