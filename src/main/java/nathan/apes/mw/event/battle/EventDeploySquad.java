package nathan.apes.mw.event.battle;

import nathan.apes.mw.battle.*;

import java.util.ArrayList;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;

public class EventDeploySquad implements Listener{
    
    @EventHandler
    public void onPlace(PlayerInteractEvent pie){
        
        Player pl = pie.getPlayer();
        
        if(Battle.isPlayerInBattle(pl)){
            
            if(pie.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                
                //Check if outside boundries... (Later)
                        
                EntityType mobtype = null;
                
                Block clkedb = pie.getClickedBlock();
                
                Location clkedloc = clkedb.getLocation();
                
                int ind = -1;
                
                if(pl.getInventory().getHeldItemSlot() == 0){
                    
                    mobtype = EntityType.ZOMBIE;
                 
                    if(pl.getInventory().getItemInMainHand().getAmount() == 3){ ind = 1; }
                    if(pl.getInventory().getItemInMainHand().getAmount() == 2){ ind = 2; }
                    if(pl.getInventory().getItemInMainHand().getAmount() == 1){ ind = 3; }
                    
                }
                
                else if(pl.getInventory().getHeldItemSlot() == 1){
                    
                    mobtype = EntityType.SKELETON;
                    
                    if(pl.getInventory().getItemInMainHand().getAmount() == 2){ ind = 4; }
                    if(pl.getInventory().getItemInMainHand().getAmount() == 1){ ind = 5; }
                    
                }
                
                Battle b = Battle.getPlayerBattle(pl);
                
                ArrayList<Player> bpls = Battle.getBattlePlayers(b);
                
                if(bpls.indexOf(pl) == 1){ ind =+ 5; }
                
                b.squads.add(new Squad(mobtype, pl, clkedloc, ind));
                
            }
            
        }
        
    }
    
}
