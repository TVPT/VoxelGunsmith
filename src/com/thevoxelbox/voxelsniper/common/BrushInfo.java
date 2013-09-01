package com.thevoxelbox.voxelsniper.common;

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
	 * The Catagory that the brush is under
	 */
	private String catagory;

	/**
	 * The BrushCode for the brush
	 */
	private String brushCode;

	public BrushInfo(String name, String catagory, String brushCode) {
		this.name = name;
		this.catagory = catagory;
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
	public String getCatagory() {
		return catagory;
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
