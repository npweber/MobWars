package nathan.apes.mw.event.battle;

import nathan.apes.mw.battle.*;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;

public class EventClickFunctionGUI implements Listener{
    
    @EventHandler
    public void onClick(InventoryClickEvent ice){
        
        Player clkpl = (Player) ice.getWhoClicked();
        
        String invname = ice.getClickedInventory().getName();
        
        if(invname.startsWith("Mob Squadron")){
                
            int type = -1;
            
            if(ice.getSlot() == 11){ type = 1; }
            
            if(ice.getSlot() == 13){ type = 2; }
            
            if(ice.getSlot() == 15){ type = 3; }
        
            String numstr = invname.substring(13, 13);
            
            int ind = Integer.parseInt(numstr);
            
            Battle b = Battle.getPlayerBattle(clkpl);
            
            for(int i = 0; i < b.squads.size(); i++){
                
                Squad sq = b.squads.get(i);
                
                if(sq.squadindex == ind){
                    
                    sq.setAI(type, ind, clkpl);
                    
                }
                
            }
        }   

    }
    
}
