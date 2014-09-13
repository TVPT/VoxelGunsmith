package com.thevoxelbox.voxelsniper.legacy;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Packet Sent by server to VoxelSniperGUI at Login.
 * Contains the users permissions level and the brushes that he/she can use
 *
 * @author thatapplefreak
 */
public class VoxelSniperPacket1LoginPayload implements Serializable {

    private final int userPermissionsLevel;
    private final ArrayList<BrushInfo> availableBrushes;

    public VoxelSniperPacket1LoginPayload(int userPermissionsLevel, ArrayList<BrushInfo> availableBrushes) {
        this.userPermissionsLevel = userPermissionsLevel;
        this.availableBrushes = availableBrushes;
    }

    public int getPermissionsLevel() {
        return userPermissionsLevel;
    }

    public ArrayList<BrushInfo> getAvailableBrushes() {
        return availableBrushes;
    }

}
