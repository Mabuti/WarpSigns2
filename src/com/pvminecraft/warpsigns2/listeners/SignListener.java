package com.pvminecraft.warpsigns2.listeners;

import com.pvminecraft.points.warps.OwnedWarp;
import com.pvminecraft.points.warps.PlayerWarpManager;
import com.pvminecraft.warpsigns2.SignManager;
import com.pvminecraft.warpsigns2.WarpSign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
    private PlayerWarpManager manager;
    private SignManager signManager;
    
    public SignListener(PlayerWarpManager manager, SignManager signManager) {
        this.manager = manager;
        this.signManager = signManager;
    }
    
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        String[] lines = event.getLines();
        Player player = event.getPlayer();
        if(!(lines[1].equalsIgnoreCase("warp") && !lines[2].isEmpty()))
            return;
        if(!player.hasPermission("warpsigns2.create")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to create warp signs!");
            return;
        }
        
        String ownerName = lines[0].isEmpty() ? player.getName() : lines[0];
        OwnedWarp warp = manager.getWarp(ownerName, lines[2]);
        if(warp == null || !warp.getVisible()) {
            player.sendMessage(ChatColor.RED + "That isn't a valid warp!");
            return;
        }
        WarpSign ws = new WarpSign(warp, event.getBlock().getLocation());
        if(!ws.validate()) {
            player.sendMessage(ChatColor.RED + "That isn't a valid warp!");
            return;
        }
        event.setLine(0, ChatColor.BLUE + ownerName);
        signManager.add(ws);
        player.sendMessage(ChatColor.GREEN + "Sign created to " + ChatColor.YELLOW + warp.getName() +
                ChatColor.GREEN + " created!");
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if(!(block.getState() instanceof Sign))
            return;
        WarpSign warp = signManager.get(block.getLocation());
        if(warp == null || !warp.validate())
            return;
        event.getPlayer().sendMessage(ChatColor.YELLOW + "You've destroyed a warp sign");
        signManager.remove(warp.getLocation());
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() instanceof Sign) {
            Location location = event.getClickedBlock().getLocation();
            WarpSign sign = signManager.get(location);
            Player player = event.getPlayer();
            if(sign != null) {
                if(player.hasPermission("warpsigns2.follow")) {
                    OwnedWarp warp = sign.getWarp();
                    player.teleport(warp.getTarget());
                } else {
                    player.sendMessage("You don't have permission to use warp signs!");
                }
            }
        }
    }
}
