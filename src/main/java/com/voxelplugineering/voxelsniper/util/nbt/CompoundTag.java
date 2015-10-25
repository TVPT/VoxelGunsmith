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

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * The <code>TAG_Compound</code> tag.
 * 
 * @author Graham Edgecombe
 */
public final class CompoundTag extends Tag
{

    /**
     * The value.
     */
    private final Map<String, Tag> value;

    /**
     * Creates the tag.
     * 
     * @param name The name.
     * @param value The value.
     */
    public CompoundTag(String name, Map<String, Tag> value)
    {
        super(name);
        this.value = Collections.unmodifiableMap(value);
    }

    @Override
    public Map<String, Tag> getValue()
    {
        return this.value;
    }

    /**
     * Checks if the given key is stored within this compound tag.
     * 
     * @param key The key
     * @return If the key is found
     */
    public boolean contains(String key)
    {
        return this.value.containsKey(key);
    }

    /**
     * Gets a child tag from this {@link CompoundTag}.
     * 
     * @param key The key to retrieve
     * @param expected The expected Tag type
     * @param <T> The tag type
     * @return The tag
     */
    public <T extends Tag> Optional<T> getChildTag(String key, Class<T> expected)
    {
        if (!this.value.containsKey(key))
        {
            return Optional.empty();
        }
        Tag tag = this.value.get(key);
        if (!expected.isInstance(tag))
        {
            throw new IllegalArgumentException(key + " tag is not of tag type " + expected.getName());
        }
        return Optional.of(expected.cast(tag));
    }

    @Override
    public String toString()
    {
        String name = getName();
        String append = "";
        if (!"".equals(name))
        {
            append = "(\"" + this.getName() + "\")";
        }
        StringBuilder bldr = new StringBuilder();
        bldr.append("TAG_Compound" + append + ": " + this.value.size() + " entries\r\n{\r\n");
        for (Map.Entry<String, Tag> entry : this.value.entrySet())
        {
            bldr.append("   " + entry.getValue().toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
        }
        bldr.append("}");
        return bldr.toString();
    }

}
