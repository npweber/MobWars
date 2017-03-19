package nathan.apes.mw.battle.util;

import nathan.apes.mw.main.MobWars;
import nathan.apes.mw.battle.*;
import nathan.apes.mw.util.PlayerQueue;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class UpdateBattleStage {
        
    public UpdateBattleStage(){
        
        JavaPlugin mainclass = JavaPlugin.getProvidingPlugin(MobWars.class);
        
        BukkitScheduler scheduler = mainclass.getServer().getScheduler();
        
        scheduler.scheduleSyncRepeatingTask(mainclass, new Runnable(){
            
            public void run(){
                
                for(int i = 0; i < PlayerQueue.currbattles.size(); i++){
                
                    Battle b = PlayerQueue.currbattles.get(i);
                    
                    int sqamt = b.squads.size();
                    
                    if(sqamt == 10){ b.initMainBattleStage(b, b.battlearea); }
                
                }
            
            }
            
        }, 0L, 5L);
    
    }
    
}
