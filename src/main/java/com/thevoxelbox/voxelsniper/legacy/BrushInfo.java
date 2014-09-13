package com.thevoxelbox.voxelsniper.legacy;

import java.io.Serializable;

/**
 * Brush information Container
 *
 * @author thatapplefreak
 */
public class BrushInfo implements Serializable {

    /**
     * Name of the brush
     */
    private String name;

    /**
     * The Category that the brush is under
     */
    private String category;

    /**
     * The BrushCode for the brush
     */
    private String brushCode;

    public BrushInfo(String name, String category, String brushCode) {
        this.name = name;
        this.category = category;
        this.brushCode = brushCode;
    }

    /**
     * Get the name of the Brush
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the Category that contains the brush
     *
     * @return the Category that contains the brush
     */
    public String getCategory() {
        return category;
    }

    /**
     * Get the brushCode
     *
     * @return the brushCode
     */
    public String getBrushCode() {
        return brushCode;
    }

}
