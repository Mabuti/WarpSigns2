package com.pvminecraft.warpsigns2;

import com.pvminecraft.points.PointsService;
import com.pvminecraft.warpsigns2.listeners.SignListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    private PointsService points;
    private SignListener listener;
    private SignManager manager;

    @Override
    public void onEnable() {
        if(!getPoints()) {
            System.err.println("[WarpSigns2] Couldn't find Points! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        importOld();
        if(!getDataFolder().exists())
            getDataFolder().mkdirs();
        System.out.println("[WarpSigns2] Finished loading Points");
        manager = new SignManager(points, getDataFolder(), getServer());
        listener = new SignListener(points.getPlayerManager(), manager);
        manager.load();
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {

    }
    
    public boolean getPoints() {
        System.out.println("[WarpSigns2] Loading Points...");
        ServicesManager sm = getServer().getServicesManager();
        points = sm.load(PointsService.class);
        return points != null;
    }
    
    public void importOld() {
        File pointsData = getServer().getPluginManager().getPlugin("Points").getDataFolder();
        if(pointsData.exists()) {
            File signsDB = new File(pointsData, "signs.db");
            if(signsDB.exists()) {
                if(!getDataFolder().exists())
                    getDataFolder().mkdirs();
                try {
                    System.out.println("[WarpSigns2] Importing old Points signs");
                    copyFile(signsDB, new File(getDataFolder(), "signs.db"));
                    if(!signsDB.delete()) {
                        System.err.println("[WarpSigns2] Could not delete Points/signs.db");
                        System.err.println("[WarpSigns2] Delete this yourself or risk data loss!");
                    }
                    System.out.println("[WarpSigns2] Finished importing old signs");
                } catch(IOException e) {
                    System.err.println("[WarpSigns2] Could not import old signs");
                }
            }
        }
    }
    
    private void copyFile(File old, File dest) throws IOException {
        if(!dest.exists())
            dest.createNewFile();
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(old).getChannel();
            destination  = new FileOutputStream(dest).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if(source != null)
                source.close();
            if(destination != null)
                destination.close();
        }
    }
    
}
