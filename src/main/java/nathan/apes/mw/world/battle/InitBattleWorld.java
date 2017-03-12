package nathan.apes.mw.world.battle;

import nathan.apes.mw.event.battle.*;
import nathan.apes.mw.main.MobWars;
import nathan.apes.mw.world.CheckWorld;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

public class InitBattleWorld {
    
    public static World battlew;
    
    public InitBattleWorld(){
        
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
        
        battlew = Bukkit.getWorld("mw_BattleWorld");
        //Change to config later...
        
        mainclass.getServer().getPluginManager().registerEvents(new EventPlayerMoveOut(), mainclass);        
        
    }
    
}
