package nathan.apes.mobwars.main;

import nathan.apes.mobwars.world.lobby.InitLobbyWorld;
import nathan.apes.mobwars.world.battle.InitBattleWorld;
import nathan.apes.mobwars.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

//MobWars by NathanApes.

public class MobWars extends JavaPlugin{

    //MobWars ChatPrefix
    public static final String loggingPrefix = "[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] ";

    //Scheduler object for convience use
    public static BukkitScheduler scheduler = Bukkit.getScheduler();

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
