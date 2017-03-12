package nathan.apes.mw.event.battle;

import nathan.apes.mw.battle.*;
import nathan.apes.mw.util.*;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;

public class EventPlaceSquad implements Listener{
    
    @EventHandler
    public void onPlace(PlayerInteractEvent pie){
        
        Player pl = pie.getPlayer();
        
        for(int i = 0; i < PlayerQueue.currbattles.size(); i++){
            
            Battle b = PlayerQueue.currbattles.get(i);
            
            if(b.getPlayers().contains(pl)){
                
                if(pie.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                    
                    //Check if outside boundries... (Later)
                            
                    EntityType mobtype = null;
                    
                    Block clkedb = pie.getClickedBlock();
                    
                    Location clkedloc = clkedb.getLocation();
                    
                    int ind = -1;
                    
                    if(pl.getInventory().getHeldItemSlot() == 0){
                        
                        mobtype = EntityType.ZOMBIE;
                     
                        if(pl.getInventory().getItemInMainHand().getAmount() == 3){ ind = 0; }
                        if(pl.getInventory().getItemInMainHand().getAmount() == 2){ ind = 1; }
                        if(pl.getInventory().getItemInMainHand().getAmount() == 1){ ind = 2; }
                        
                    }
                    
                    else if(pl.getInventory().getHeldItemSlot() == 1){
                        
                        mobtype = EntityType.SKELETON;
                        
                        if(pl.getInventory().getItemInMainHand().getAmount() == 2){ ind = 3; }
                        if(pl.getInventory().getItemInMainHand().getAmount() == 1){ ind = 4; }
                        
                    }
                    
                    Battle.squads.add(new Squad(mobtype, pl, clkedloc, ind));
                    
                }
                
            }
            
        }
        
    }
    
}
