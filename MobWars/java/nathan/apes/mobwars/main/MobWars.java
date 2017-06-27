package nathan.apes.mobwars.main;

import nathan.apes.mobwars.command.GameCommand;
import nathan.apes.mobwars.world.lobby.InitLobbyWorld;
import nathan.apes.mobwars.world.battle.InitBattleWorld;
import nathan.apes.mobwars.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

//MobWars by NathanApes.

public class MobWars extends JavaPlugin{

    //MobWars ChatPrefix
    public static final String loggingPrefix = "[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] ";

    //Scheduler object for convience use
    public static final BukkitScheduler scheduler = Bukkit.getScheduler();

    //Config object
    public static FileConfiguration config;

    //Enable
    public void onEnable(){

        //Log Enable
        this.getLogger().info("Enabling MobWars...");

        //Init Plugin Systems
        new InitLobbyWorld();
        new InitBattleWorld();
        new BattleManager();

        //Register Beta Stage Game Command (Creates a game environment for Beta purposes)
        getCommand("mw").setExecutor(new GameCommand());

        //Setup config for Beta Stage Uses (Allows the user to set a location used for Battle)
        config = getConfig();
        config.addDefault("battlelocationX", 0.0);
        config.addDefault("battlelocationZ", 0.0);
        config.options().copyDefaults(true);
        saveConfig();
    }

    //Disable
    public void onDisable(){
        //Log Disable
        this.getLogger().info("Disabling MobWars...");
    }
}
