package com.voxelplugineering.voxelsniper;

import java.io.IOException;

import org.junit.Test;

import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceFactory;
import com.voxelplugineering.voxelsniper.service.persistence.JsonDataSourceReader;
import com.voxelplugineering.voxelsniper.service.persistence.MemoryContainer;
import com.voxelplugineering.voxelsniper.service.persistence.StandardOutDataSource;

/**
 * A test for various aspects of the persistence service.
 */
public class PersistenceTest
{

    /**
     * 
     * @throws IOException On error
     */
    @Test
    public void test() throws IOException
    {
        StandardOutDataSource source = new StandardOutDataSource();
        JsonDataSourceReader reader = new JsonDataSourceReader(source);
        
        DataContainer data = new MemoryContainer("");
        data.writeString("hello", "world");
        
        reader.write(data);
    }

    /**
     * 
     * @throws IOException On error
     */
    @Test
    public void testFactory() throws IOException
    {
        Gunsmith.getServiceManager().setTesting(new CoreServiceProvider());
        DataSourceFactory factory = Gunsmith.getPersistence();
        
        DataContainer args = new MemoryContainer("");
        args.writeString("source", "stdout");
        args.writeContainer("sourceArgs", new MemoryContainer(""));
        
        JsonDataSourceReader output = (JsonDataSourceReader) factory.build("json", args).get();
        
        DataContainer data = new MemoryContainer("");
        data.writeString("hello", "world");
        
        output.write(data);
    }

}
