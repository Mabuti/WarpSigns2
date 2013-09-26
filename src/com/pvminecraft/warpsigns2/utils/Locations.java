package com.pvminecraft.warpsigns2.utils;

import com.pvminecraft.FlatDB.Row;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

public class Locations {
    public static Row locationToRow(Location location, String index) {
        Row row = new Row(index);
        String world = location.getWorld().getName();
        String x = String.valueOf(location.getX());
        String y = String.valueOf(location.getY());
        String z = String.valueOf(location.getZ());
        row.addElement("world", world);
        row.addElement("x", x);
        row.addElement("y", y);
        row.addElement("z", z);
        return row;
    }
    
    public static Location locationFromRow(Row row, Server server) {
        World world = server.getWorld(row.getElement("world"));
        double x = Double.parseDouble(row.getElement("x"));
        double y = Double.parseDouble(row.getElement("y"));
        double z = Double.parseDouble(row.getElement("z"));
        return new Location(world, x, y, z);
    }
}
