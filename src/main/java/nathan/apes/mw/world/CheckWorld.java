package nathan.apes.mw.world;

import java.util.*;

import org.bukkit.*;

public class CheckWorld {
    
    public boolean hasw = false;
    
    public CheckWorld(){}
    
    public boolean worldCheck(String name){
        
        List<World> serverws = Bukkit.getWorlds();
        
        for(int i = 0; i < serverws.size(); i++){
            
            if(serverws.get(i).getName().equalsIgnoreCase(name)){
    
                hasw = true;
                
            }

        }
        
        return hasw;
    }
    
}
