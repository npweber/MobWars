package nathan.apes.mw.event.lobby;

import nathan.apes.mw.func.world.lobby.InitLobby;
import nathan.apes.mw.util.PlayerQueue;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

public class EventQueueClick implements Listener{
    
    @EventHandler
    public void onClick(InventoryClickEvent ice){
        
        Player clker = (Player) ice.getWhoClicked();
        
        if(clker.getWorld().equals(new InitLobby().lobbyw)){
        
            if(ice.getClick().equals(ClickType.RIGHT)){
                
                if(ice.getCurrentItem().equals(new ItemStack(Material.NETHER_STAR))){
                
                    for(int i = 0; i < new PlayerQueue().players.size(); i++){
                
                        if(new PlayerQueue().players.get(i).equals(clker)){

                            clker.sendMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.DARK_RED + "You are already in the Queue...");             
                
                        }
                    
                        else {
                        
                            new PlayerQueue().players.add(clker);
                     
                            clker.sendMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.GOLD + "You've been added to the Queue. Wait until you've got your opponent, so you can start the match!");
                     
                        }
                
                    }
                    
                }   
            
            }
        
        }

    }
    
}
