package com.pvminecraft.warpsigns2.utils;

import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.PointsService;
import com.pvminecraft.points.warps.OwnedWarp;
import com.pvminecraft.warpsigns2.WarpSign;
import org.bukkit.Location;
import org.bukkit.Server;

public class SignUtils {
    public static Row signToRow(WarpSign sign) {
        Row row = Locations.locationToRow(sign.getLocation(), String.valueOf(sign.hashCode()));
        String owner = sign.getWarp().getOwner();
        String warp = sign.getWarp().getName();
        String entry = owner + ";" + warp;
        row.addElement("warp", entry);
        return row;
    }
    
    public static WarpSign signFromRow(Row row, Server server, PointsService points) {
        Location location = Locations.locationFromRow(row, server);
        String player = row.getElement("warp").split(";")[0];
        String wname = row.getElement("warp").split(";")[1];
        OwnedWarp warp = points.getPlayerManager().getWarp(player, wname);
        if(warp == null)
            return null;
        WarpSign sign = new WarpSign(warp, location);
        if(!sign.validate())
            return null;
        return sign;
    }
}
