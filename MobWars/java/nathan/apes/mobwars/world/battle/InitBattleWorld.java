package nathan.apes.mobwars.world.battle;

import nathan.apes.mobwars.main.MobWars;
import nathan.apes.mobwars.world.CheckWorld;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import static nathan.apes.mobwars.main.MobWars.loggingPrefix;

//InitBattleWorld: System for BattleWorld and its environment

public class InitBattleWorld {

    //World
    public static World battlew;

    //Refrence to main
    private JavaPlugin mainClass = JavaPlugin.getProvidingPlugin(MobWars.class);

    public InitBattleWorld(){

        //Check for occurance of the Battle world. If not, create it for the user.
        if(new CheckWorld("mw_BattleWorld").hasw == false){

            //Create World
            WorldCreator wc = new WorldCreator("mw_BattleWorld");
            wc.generateStructures(false);
            wc.type(WorldType.NORMAL);
            World w = wc.createWorld();

            //Log Creation
            mainClass.getLogger().info("Generated battle world...");
            Bukkit.broadcastMessage(loggingPrefix + ChatColor.AQUA + "Generated battle world. Games will be played here...");

            //Mark world
            battlew = w;
        }
        battlew = Bukkit.getWorld("mw_BattleWorld");
        //Change to config later...
    }
    
}
