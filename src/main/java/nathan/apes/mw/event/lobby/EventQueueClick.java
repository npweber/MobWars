package nathan.apes.mw.event.lobby;

import nathan.apes.mw.world.lobby.InitLobbyWorld;
import nathan.apes.mw.util.PlayerQueue;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class EventQueueClick implements Listener{
    
    private boolean hasClked = false;
    
    @EventHandler
    public void onClick(PlayerInteractEvent pie){
        
        Player clker = pie.getPlayer();
        
        if(clker.getWorld().equals(InitLobbyWorld.lobbyw)){
    
            if(pie.getAction().equals(Action.RIGHT_CLICK_AIR)){
                
                if(pie.hasItem()){
                
                    if(pie.getItem().equals(new ItemStack(Material.NETHER_STAR))){
                        
                        if(hasClked == false){
                            
                            PlayerQueue.playerpair.add(clker);
                            
                            hasClked = true;
                            
                            clker.sendMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.GREEN + "You have been added to the Queue. Once an opponent is found, your game will start...");
                            
                        }
                        
                        else {
                            
                            PlayerQueue.playerpair.remove(clker);
                            
                            hasClked = false;
                            
                            clker.sendMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.GOLD + "You have been removed from the Queue. You can come back anytime...");
                            
                        }
    
                    }
                    
                }

            }
        
        }

    }
    
}
