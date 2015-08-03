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
package com.voxelplugineering.voxelsniper.service.meta;

import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.service.AbstractService;
import com.voxelplugineering.voxelsniper.service.AnnotationScanner;
import com.voxelplugineering.voxelsniper.util.Context;

import com.google.common.base.StandardSystemProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * An implementation of the {@link AnnotationScanner} service.
 */
public class AnnotationScannerService extends AbstractService implements AnnotationScanner
{

    private static final String JAVA_HOME = StandardSystemProperty.JAVA_HOME.value();
    private static final String CLASS_EXTENSION = ".class";
    private static final FileFilter CLASS_OR_DIRECTORY = new FileFilter()
    {

        @Override
        public boolean accept(File file)
        {
            return file.isDirectory() || file.getName().endsWith(CLASS_EXTENSION);
        }
    };
    static final FilenameFilter ARCHIVE = new FilenameFilter()
    {

        @Override
        public boolean accept(File dir, String name)
        {
            return name.endsWith(".jar") || name.endsWith(".zip");
        }
    };

    private static Set<String> DEFAULT_SCANNER_EXCLUSIONS = Sets.newHashSet();

    static
    {
        DEFAULT_SCANNER_EXCLUSIONS.add("java/");
        DEFAULT_SCANNER_EXCLUSIONS.add("sun/");
        DEFAULT_SCANNER_EXCLUSIONS.add("com/sun/");
        DEFAULT_SCANNER_EXCLUSIONS.add("javax/");
        DEFAULT_SCANNER_EXCLUSIONS.add("org/lwjgl/");
        DEFAULT_SCANNER_EXCLUSIONS.add("org/bukkit/");
        DEFAULT_SCANNER_EXCLUSIONS.add("org/spongepowered/");
        DEFAULT_SCANNER_EXCLUSIONS.add("net/minecraft/");
        DEFAULT_SCANNER_EXCLUSIONS.add("net/minecraftforge/");
        DEFAULT_SCANNER_EXCLUSIONS.add("com/google/");
        DEFAULT_SCANNER_EXCLUSIONS.add("org/objectweb/asm/");
    }

    private final Map<String, List<AnnotationConsumer>> consumers = Maps.newHashMap();
    private Set<String> scannerExclusions = Sets.newHashSet();

    /**
     * Creates a new {@link AnnotationScannerService}.
     * 
     * @param context the context
     */
    public AnnotationScannerService(Context context)
    {
        super(context);
        this.scannerExclusions.addAll(DEFAULT_SCANNER_EXCLUSIONS);
    }

    @Override
    protected void _init()
    {

    }

    @Override
    protected void _shutdown()
    {

    }

    @Override
    public void addScannerExclusion(String exc)
    {
        this.scannerExclusions.add(exc);
    }

    private List<AnnotationConsumer> getOrCreateConsumers(Class<?> cls)
    {
        String desc = Type.getDescriptor(cls);
        if (this.consumers.containsKey(desc))
        {
            return this.consumers.get(desc);
        }
        List<AnnotationConsumer> list = Lists.newArrayList();
        this.consumers.put(desc, list);
        return list;
    }

    @Override
    public void register(Class<? extends Annotation> target, AnnotationConsumer consumer)
    {
        GunsmithLogger.getLogger().debug("Registered AnnotationConsumer: " + consumer.getClass().getName() + " for " + target.getName());
        getOrCreateConsumers(target).add(consumer);
    }

    @Override
    public void scanClassPath(URLClassLoader loader)
    {
        GunsmithLogger.getLogger().debug("Scanning classloader for annotations");
        for (String ex : this.scannerExclusions)
        {
            GunsmithLogger.getLogger().debug("\tExcluding: " + ex);
        }
        Set<URI> sources = Sets.newHashSet();

        for (URL url : loader.getURLs())
        {
            if (!url.getProtocol().equals("file"))
            {
                GunsmithLogger.getLogger().warn("Skipping unsupported classpath source: " + url);
                continue;
            }

            if (url.getPath().startsWith(JAVA_HOME))
            {
                GunsmithLogger.getLogger().debug("Skipping JRE classpath entry: " + url);
                continue;
            }

            URI source;
            try
            {
                source = url.toURI();
            } catch (URISyntaxException e)
            {
                GunsmithLogger.getLogger().error("Failed to search for classpath plugins in " + url);
                continue;
            }

            if (sources.add(source))
            {
                try
                {
                    scanFile(new File(source));
                } catch (IOException e)
                {
                    GunsmithLogger.getLogger().error(e, "Failed to scan file " + source.getPath());
                }
            }
        }
    }

    private void scanFile(File file) throws IOException
    {
        if (file.exists())
        {
            if (file.isDirectory())
            {
                scanDirectory(file);
            } else
            {
                scanZip(file);
            }
        }
    }

    private void scanDirectory(File dir) throws IOException
    {
        for (File file : dir.listFiles(CLASS_OR_DIRECTORY))
        {
            if (file.isDirectory())
            {
                scanDirectory(file);
            } else
            {
                InputStream in = new FileInputStream(file);
                try
                {
                    findAnnotations(in);
                } finally
                {
                    in.close();
                }
            }
        }
    }

    private void scanZip(File file)
    {
        GunsmithLogger.getLogger().debug("Scanning file: " + file.getAbsolutePath());
        if (!ARCHIVE.accept(null, file.getName()))
        {
            return;
        }

        try
        {
            ZipFile zip = new ZipFile(file);
            try
            {
                Enumeration<? extends ZipEntry> entries = zip.entries();
                while (entries.hasMoreElements())
                {
                    ZipEntry entry = entries.nextElement();
                    GunsmithLogger.getLogger().debug("Checking entry: " + entry.getName());
                    if (entry.isDirectory() || !entry.getName().endsWith(CLASS_EXTENSION))
                    {
                        continue;
                    }

                    InputStream in = zip.getInputStream(entry);
                    try
                    {
                        findAnnotations(in);
                    } finally
                    {
                        in.close();
                    }
                }
            } finally
            {
                zip.close();
            }
        } catch (IOException e)
        {
            GunsmithLogger.getLogger().error(e, "Failed to scan JAR: " + file.getAbsolutePath());
        }
    }

    @SuppressWarnings("unchecked")
    private void findAnnotations(InputStream in) throws IOException
    {
        ClassReader reader = new ClassReader(in);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        for (String exc : this.scannerExclusions)
        {
            if (classNode.name.startsWith(exc))
            {
                return;
            }
        }
        GunsmithLogger.getLogger().debug("Checking class: " + classNode.name);
        if (classNode.visibleAnnotations != null)
        {
            List<AnnotationConsumer> allConsumers = Lists.newArrayList();
            for (AnnotationNode node : (List<AnnotationNode>) classNode.visibleAnnotations)
            {
                GunsmithLogger.getLogger().debug("\tFound annotation: " + node.desc);
                for (String desc : this.consumers.keySet())
                {
                    if (node.desc.equals(desc))
                    {
                        allConsumers.addAll(this.consumers.get(desc));
                        break;
                    }
                }
            }
            if (!allConsumers.isEmpty())
            {
                GunsmithLogger.getLogger().debug("Found targetted annotation on class " + classNode.name);
                try
                {
                    Class<?> cls = Class.forName(classNode.name.replaceAll("/", "."));
                    for (AnnotationConsumer consumer : allConsumers)
                    {
                        GunsmithLogger.getLogger().debug("\tPassing to " + consumer.getClass().getName());
                        consumer.consume(cls);
                    }
                } catch (ClassNotFoundException ignored)
                {
                    ignored.printStackTrace();
                }

            }
        }
    }

}
