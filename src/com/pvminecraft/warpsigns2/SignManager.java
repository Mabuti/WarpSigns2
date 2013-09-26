package com.pvminecraft.warpsigns2;

import com.pvminecraft.FlatDB.FlatDB;
import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.PointsService;
import com.pvminecraft.warpsigns2.utils.SignUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Sign;

public class SignManager {
    private PointsService points;
    private HashMap<Location, WarpSign> signs;
    private File dataDir;
    private Server server;
    
    public static final String DBNAME = "signs.db";
    
    public SignManager(PointsService points, File data, Server server) {
        signs = new HashMap<Location, WarpSign>();
        this.points = points;
        this.dataDir = data;
        this.server = server;
    }
    
    public boolean add(WarpSign sign) {
        Location location = sign.getLocation();
        if(!sign.validate())
            return false;
        signs.put(location, sign);
        save();
        return true;
    }
    
    public WarpSign get(Sign sign) {
        return get(sign.getBlock().getLocation());
    }
    
    public WarpSign get(Location location) {
        return signs.get(location);
    }
    
    public WarpSign remove(Sign sign) {
        return remove(sign.getBlock().getLocation());
    }
    
    public WarpSign remove(Location location) {
        WarpSign ret = signs.remove(location);
        save();
        return ret;
    }
    
    public void save() {
        FlatDB database = new FlatDB(dataDir.getPath(), DBNAME);
        database.removeAll();
        Set<Location> keySet = signs.keySet();
        for(Location key : keySet) {
            WarpSign sign = signs.get(key);
            if(!sign.validate())
                continue;
            Row row = SignUtils.signToRow(sign);
            database.addRow(row);
        }
        database.update();
    }
    
    public void load() {
        FlatDB database = new FlatDB(dataDir.getPath(), DBNAME);
        for(Row row : database.getAll()) {
            WarpSign sign = SignUtils.signFromRow(row, server, points);
            if(sign == null) {
                System.out.println("[WarpSigns2] Skipped sign " + row.getElement("warp"));
                continue;
            }
            signs.put(sign.getLocation(), sign);
        }
    }
}
