package nathan.apes.mw.battle.util;

import nathan.apes.mw.main.MobWars;
import nathan.apes.mw.battle.*;
import nathan.apes.mw.battle.ai.*;
import nathan.apes.mw.util.PlayerQueue;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class UpdateSquadAI {
    
    public UpdateSquadAI(){
        
        JavaPlugin mainclass = JavaPlugin.getProvidingPlugin(MobWars.class);
        
        BukkitScheduler scheduler = mainclass.getServer().getScheduler();
        
        scheduler.scheduleSyncRepeatingTask(mainclass, new Runnable(){
            
            public void run(){
                
                for(int i = 0; i < PlayerQueue.currbattles.size(); i++){
                    
                    Battle b = PlayerQueue.currbattles.get(i);
                    
                    if(b.battlestage == 2){
                        
                        for(int i2 = 0; i < 10; i++){
                        
                            if(b.squads.get(i2).ai == 1){
                                
                                new AIAttack(b.squads.get(i2));
                                //Init Squad "Attack" AI
                                
                            }
                            
                            if(b.squads.get(i2).ai == 2){
                                
                                new AIGuard(b.squads.get(i2)); 
                                //Init Squad "Guard" AI
                                
                            }
                            
                            if(b.squads.get(i2).ai == 3){
                                
                                new AISnipe(b.squads.get(i2));
                                //Init Squad "Snipe" AI
                                
                            }
                            
                        }

                    }
                    
                }
                
            }  
            
        } , 0L, 5L);
        
    }
    
}
