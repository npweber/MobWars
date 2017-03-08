package nathan.apes.mw.func.world.battle;

import nathan.apes.mw.event.battle.*;
import nathan.apes.mw.func.world.CheckWorld;
import nathan.apes.mw.main.MobWars;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

public class InitBattle {
    
    public World battlew;
    
    public InitBattle(){}
    
    public void initBattleWorld(){
        
        JavaPlugin mainclass = JavaPlugin.getProvidingPlugin(MobWars.class);
        
        if(new CheckWorld().worldCheck("mw_BattleWorld") == false){
    
            WorldCreator wc = new WorldCreator("mw_BattleWorld");
            
            wc = wc.generateStructures(false);
            wc = wc.type(WorldType.NORMAL);
            
            World w = wc.createWorld(); 
            
            mainclass.getLogger().info("Generated battle world...");
            
            Bukkit.broadcastMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.AQUA + "Generated battle world. Games will be played here...");
            
            battlew = w;
            
        }
        
        mainclass.getServer().getPluginManager().registerEvents(new EventPlayerMoveOut(), mainclass);
        
    }
    
}
