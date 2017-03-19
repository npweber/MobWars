package nathan.apes.mw.event.battle;

import nathan.apes.mw.battle.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;

public class EventClickAIGUI implements Listener{
    
    @EventHandler
    public void onClick(InventoryClickEvent ice){
        
        Player clkpl = (Player) ice.getWhoClicked();
        
        String invname = ice.getClickedInventory().getName();
        
        if(invname.startsWith("Mob Squadron")){
                
            int type = -1;
            
            if(ice.getSlot() == 11){ type = 1; clkpl.sendMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.GOLD + "Set Squadron to ATTACK..."); }
            
            if(ice.getSlot() == 13){ type = 2; clkpl.sendMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.GOLD + "Set Squadron to GUARD..."); }
            
            if(ice.getSlot() == 15){ type = 3; clkpl.sendMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.GOLD + "Set Squadron to SNIPE..."); }
        
            String numstr = invname.substring(13, 13);
            
            int ind = Integer.parseInt(numstr);
            
            Battle b = Battle.getPlayerBattle(clkpl);
            
            for(int i = 0; i < b.squads.size(); i++){
                
                Squad sq = b.squads.get(i);
                
                if(sq.squadindex == ind){
                    
                    sq.ai = type;
                    
                }
                
            }
        
        }   

    }
    
}
