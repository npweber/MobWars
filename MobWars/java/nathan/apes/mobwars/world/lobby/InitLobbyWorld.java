package nathan.apes.mobwars.world.lobby;

import nathan.apes.mobwars.main.MobWars;
import nathan.apes.mobwars.world.CheckWorld;
import nathan.apes.mobwars.event.lobby.*;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

//InitLobbyWorld: System for LobbyWorld and its Environment

public class InitLobbyWorld {

    //World Field
    public static World lobbyw;

    //MatchMaking Switch
    private static boolean matchMakingEnabled;

    //Refrence to main
    private JavaPlugin mainClass = JavaPlugin.getProvidingPlugin(MobWars.class);

    public InitLobbyWorld(){

        //Check for occurance of the Lobby world. If not, create it for the user.
        if(new CheckWorld("mw_Lobby").hasw == false){

            //Create world
            WorldCreator wc = new WorldCreator("mw_Lobby");
            wc = wc.generateStructures(false);
            wc = wc.type(WorldType.FLAT);
            wc = wc.generatorSettings("2;0;1;");
            World w = wc.createWorld();

            //Log Creation
            mainClass.getLogger().info("Generated lobby world...");
            Bukkit.broadcastMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.AQUA + "Generated lobby world. Build your spawn!");

            //Mark world
            lobbyw = w;
        }
        lobbyw = Bukkit.getWorld("mw_Lobby");
        //Change to config value later...

        //Register Lobby Events
        mainClass.getServer().getPluginManager().registerEvents(new EventQueueClick(), mainClass);
        matchMakingEnabled = true;
    }

    public static boolean getMatchMakingStatus(){ return matchMakingEnabled; }
    public static void setMatchMakingEnabled(boolean b){ matchMakingEnabled = b; }
}

