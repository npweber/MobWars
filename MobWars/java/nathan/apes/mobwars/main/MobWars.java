package nathan.apes.mobwars.main;

import nathan.apes.mobwars.world.lobby.InitLobbyWorld;
import nathan.apes.mobwars.world.battle.InitBattleWorld;
import nathan.apes.mobwars.util.*;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

//MobWars by NathanApes.

public class MobWars extends JavaPlugin{

    //MobWars ChatPrefix
    public static final String loggingPrefix = "[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] ";

    //Convenience Field for Outside Classes Linking to Main
    public static final JavaPlugin mainClass = JavaPlugin.getProvidingPlugin(MobWars.class);

    //Enable
    public void onEnable(){

        //Log Enable
        this.getLogger().info("Enabling MobWars...");

        //Init Plugin Systems
        new InitLobbyWorld();
        new InitBattleWorld();
        new BattleManager();
    }

    //Disable
    public void onDisable(){
        //Log Disable
        this.getLogger().info("Disabling MobWars...");
    }
}
