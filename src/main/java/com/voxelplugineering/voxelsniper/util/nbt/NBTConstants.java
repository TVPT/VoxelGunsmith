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
package com.voxelplugineering.voxelsniper.util.nbt;

import java.nio.charset.Charset;

/*
 * JNBT License
 * 
 * Copyright (c) 2010 Graham Edgecombe
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *       
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *       
 *     * Neither the name of the JNBT team nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
 */

/**
 * A class which holds constant values.
 * 
 * @author Graham Edgecombe
 */
@SuppressWarnings("javadoc")
public final class NBTConstants
{

    /**
     * The character set used by NBT (UTF-8).
     */
    public static final Charset CHARSET = Charset.forName("UTF-8");

    /**
     * Tag type constants.
     */
    public static final int TYPE_END = 0;
    public static final int TYPE_BYTE = 1;
    public static final int TYPE_SHORT = 2;
    public static final int TYPE_INT = 3;
    public static final int TYPE_LONG = 4;
    public static final int TYPE_FLOAT = 5;
    public static final int TYPE_DOUBLE = 6;
    public static final int TYPE_BYTE_ARRAY = 7;
    public static final int TYPE_STRING = 8;
    public static final int TYPE_LIST = 9;
    public static final int TYPE_COMPOUND = 10;
    public static final int TYPE_INT_ARRAY = 11;

    /**
     * Default private constructor.
     */
    private NBTConstants()
    {

    }

    /**
     * Convert a type ID to its corresponding {@link Tag} class.
     * 
     * @param id type ID
     * @return tag class
     * @throws IllegalArgumentException thrown if the tag ID is not valid
     */
    public static Class<? extends Tag> getClassFromType(int id)
    {
        switch (id)
        {
        case TYPE_END:
            return EndTag.class;
        case TYPE_BYTE:
            return ByteTag.class;
        case TYPE_SHORT:
            return ShortTag.class;
        case TYPE_INT:
            return IntTag.class;
        case TYPE_LONG:
            return LongTag.class;
        case TYPE_FLOAT:
            return FloatTag.class;
        case TYPE_DOUBLE:
            return DoubleTag.class;
        case TYPE_BYTE_ARRAY:
            return ByteArrayTag.class;
        case TYPE_STRING:
            return StringTag.class;
        case TYPE_LIST:
            return ListTag.class;
        case TYPE_COMPOUND:
            return CompoundTag.class;
        case TYPE_INT_ARRAY:
            return IntArrayTag.class;
        default:
            throw new IllegalArgumentException("Unknown tag type ID of " + id);
        }
    }

}
