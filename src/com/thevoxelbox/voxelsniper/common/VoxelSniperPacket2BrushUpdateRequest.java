package com.thevoxelbox.voxelsniper.common;

import java.io.Serializable;

public class VoxelSniperPacket2BrushUpdateRequest implements Serializable {
	
	private final BrushInfo brushInfo;
	private final int size;
	private final SendableItemInfo material;
	private final SendableItemInfo mask;
	
	public VoxelSniperPacket2BrushUpdateRequest(BrushInfo brush, int size, SendableItemInfo material, SendableItemInfo mask) {
		this.brushInfo = brush;
		this.size = size;
		this.material = material;
		this.mask = mask;
	}
	
	public BrushInfo getBrushInfo() {
		return brushInfo;
	}
	
	public int getSize() {
		return size;
	}
	
	public SendableItemInfo getMaterial() {
		return material;
	}

	public SendableItemInfo getMask() {
		return mask;
	}	
	
	public boolean noMask() {
		return mask.getID() == -1;
	}
	
}
