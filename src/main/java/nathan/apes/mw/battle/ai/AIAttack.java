package nathan.apes.mw.battle.ai;

import nathan.apes.mw.battle.*;

import org.bukkit.*;
import org.bukkit.entity.*;

public class AIAttack {
    
    public AIAttack(Squad s){
        
        formup(s);
        
    }
    
    private void formup(Squad sq){
        
        Player owner = sq.squadowner;
        
        Location ownloc = owner.getLocation();
        
        float yaw = owner.getLocation().getYaw();
        
        World bw = Bukkit.getWorld("mw_BattleWorld");
        //Change to config later...
        
        if(yaw > 0 && yaw < 90){
            
            rotate(sq, 1);
        
            Location loc = new Location(bw, ownloc.getX(), ownloc.getY(), ownloc.getZ() + 5.0);
            setDestination(sq, loc); 
            
        }
        if(yaw > 90 && yaw < 180){
            
            rotate(sq, 2);
        
            Location loc = new Location(bw, ownloc.getX() - 5.0, ownloc.getY(), ownloc.getZ());
            setDestination(sq, loc); 
                    
        }
        if(yaw > 180 && yaw < 270){
            
            rotate(sq, 3);
        
            Location loc = new Location(bw, ownloc.getX(), ownloc.getY(), ownloc.getZ() - 5.0);
            setDestination(sq, loc);            
            
        }
        if(yaw > 270 && yaw < 360){
            
            rotate(sq, 4);
        
            Location loc = new Location(bw, ownloc.getX() + 5.0, ownloc.getY(), ownloc.getZ());
            setDestination(sq, loc);            
            
        }
        
    }
    
    private void rotate(Squad s, int dir){}
    
    private void setDestination(Squad s, Location dest){}
    
}
