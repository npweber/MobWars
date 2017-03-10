package nathan.apes.mw.event.lobby;

import nathan.apes.mw.func.world.lobby.InitLobbyWorld;
import nathan.apes.mw.util.PlayerQueue;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class EventQueueClick implements Listener{
    
    @EventHandler
    public void onClick(PlayerInteractEvent pie){
        
        Player clker = pie.getPlayer();
        
        clker.sendMessage("Test.");
        
        if(clker.getWorld().equals(InitLobbyWorld.lobbyw)){
        
            if(pie.getAction().equals(Action.LEFT_CLICK_AIR)){
                
                if(pie.getItem().equals(new ItemStack(Material.NETHER_STAR))){
                
                    for(int i = 0; i < PlayerQueue.players.size(); i++){
                
                        if(PlayerQueue.players.get(i).equals(clker)){

                            clker.sendMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.DARK_RED + "You are already in the Queue...");             
                
                        }
                    
                        else {
                        
                            PlayerQueue.players.add(clker);
                     
                            clker.sendMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.GOLD + "You've been added to the Queue. Wait until you've got your opponent, so you can start the match!");
                     
                        }
                
                    }
                    
                }   
            
            }
        
        }

    }
    
}
