package com.pvminecraft.warpsigns2;

import com.pvminecraft.points.warps.OwnedWarp;
import org.bukkit.Location;
import org.bukkit.block.Sign;

public class WarpSign {
    private OwnedWarp target;
    private Location location;
    
    public WarpSign(OwnedWarp target, Location location) {
        this.target = target;
        this.location = location;
    }
    
    public OwnedWarp getWarp() {
        return target;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public boolean validate() {
        return target != null && location.getBlock().getState() instanceof Sign;
    }
}
