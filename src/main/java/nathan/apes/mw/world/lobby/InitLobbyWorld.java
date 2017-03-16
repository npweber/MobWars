package nathan.apes.mw.world.lobby;

import nathan.apes.mw.main.MobWars;
import nathan.apes.mw.world.CheckWorld;
import nathan.apes.mw.event.lobby.*;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

public class InitLobbyWorld {
    
    public static World lobbyw;
    
    public InitLobbyWorld(){
        
        JavaPlugin mainclass = JavaPlugin.getProvidingPlugin(MobWars.class);
        
        if(new CheckWorld().worldCheck("mw_Lobby") == false){
            
            WorldCreator wc = new WorldCreator("mw_Lobby");
            
            wc = wc.generateStructures(false);
            wc = wc.type(WorldType.FLAT);
            wc = wc.generatorSettings("2;0;1;");
            
            World w = wc.createWorld();
            
            mainclass.getLogger().info("Generated lobby world...");
            
            Bukkit.broadcastMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.AQUA + "Generated lobby world. Build your spawn!");
            
            lobbyw = w;
            
        }
        
        lobbyw = Bukkit.getWorld("mw_Lobby");
        //Change to config value later...
        
        mainclass.getServer().getPluginManager().registerEvents(new EventQueueClick(), mainclass);        
        
    }

}

