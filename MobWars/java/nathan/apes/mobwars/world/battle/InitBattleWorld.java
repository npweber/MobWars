package nathan.apes.mobwars.world.battle;

import nathan.apes.mobwars.world.CheckWorld;

import org.bukkit.*;

import static nathan.apes.mobwars.main.MobWars.loggingPrefix;
import static nathan.apes.mobwars.main.MobWars.mainClass;

//InitBattleWorld: System for BattleWorld and its environment

public class InitBattleWorld {

    //World
    public static World battlew;

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
