package nathan.apes.mobwars.world.lobby;

import nathan.apes.mobwars.main.MobWars;
import nathan.apes.mobwars.world.CheckWorld;
import nathan.apes.mobwars.event.lobby.*;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import static nathan.apes.mobwars.main.MobWars.loggingPrefix;

//InitLobbyWorld: System for LobbyWorld and its Environment

public class InitLobbyWorld {

    //MatchMaking Switch
    private static boolean matchMakingEnabled;

    //Refrence to main
    private JavaPlugin mainClass = JavaPlugin.getProvidingPlugin(MobWars.class);

    public InitLobbyWorld(){

        //Check for occurance of the Lobby world. If not, create it for the user.
        if(!(new CheckWorld("mw_Lobby").hasw)){

            //Create world
            WorldCreator wc = new WorldCreator("mw_Lobby");
            wc = wc.generateStructures(false);
            wc = wc.type(WorldType.FLAT);
            wc = wc.generatorSettings("2;0;1;");
            World w = wc.createWorld();

            //Log Creation
            mainClass.getLogger().info("Generated lobby world...");
            Bukkit.broadcastMessage(loggingPrefix + ChatColor.AQUA + "Generated lobby world. Build your spawn!");
        }

        //(Not needed for this stage in Development)
        //Register Lobby Events
        mainClass.getServer().getPluginManager().registerEvents(new EventQueueClick(), mainClass);
        matchMakingEnabled = true;
    }

    //Get & Set MatchmakingEnabled
    public static boolean getMatchMakingStatus(){ return matchMakingEnabled; }
    public static void setMatchMakingEnabled(boolean b){ matchMakingEnabled = b; }
}