package nathan.apes.mw.event.battle;

import nathan.apes.mw.util.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class EventPlayerMoveOut implements Listener{

    @EventHandler
    public void onPlayerMoveOutofBounds(PlayerMoveEvent pme){
        
        Player pl = pme.getPlayer();
        
        for(int i = 0; i < PlayerQueue.currbattles.size(); i++){
            
            Battle b = PlayerQueue.currbattles.get(i);
            
            if(b.getPlayers().contains(pl)){
                
                int x = (int) b.battlearea.getX();
                int z = (int) b.battlearea.getZ(); 
                
                Location ploc = pl.getLocation(); 
                
                double plx = ploc.getX();
                double plz = ploc.getZ();
                
                Location newloc = ploc;      
                
                if(plx > x || plx < x - 200){
                    
                    if(plx > 0){ newloc.setX(plx - 1); pl.teleport(newloc); }
                    if(plx < 0){ newloc.setX(plx + 1); pl.teleport(newloc); }
                    
                }      
                
                if(plz > z || plz < z - 200){
                    
                    if(plz > 0){ newloc.setZ(plz - 1); pl.teleport(newloc); }
                    if(plz < 0){ newloc.setZ(plz + 1); pl.teleport(newloc); }
                    
                }  
        
            }
            
        }
        
    }

}
