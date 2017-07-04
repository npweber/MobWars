package nathan.apes.mobwars.main;

import nathan.apes.mobwars.command.GameCommand;
import nathan.apes.mobwars.world.battle.FindBattleground;
import nathan.apes.mobwars.world.lobby.InitLobbyWorld;
import nathan.apes.mobwars.world.battle.InitBattleWorld;
import nathan.apes.mobwars.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;

//MobWars by NathanApes.

public class MobWars extends JavaPlugin{

    //MobWars ChatPrefix
    public static final String loggingPrefix = "[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] ";

    //Scheduler object for convience use
    public static final BukkitScheduler scheduler = Bukkit.getScheduler();

    //Config objects
    public static FileConfiguration config;
    public static FileConfiguration xBndDatabase;
    private File configdatabaseFile;

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

        //Setup config (Unused currently)
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();

        //Setup Battleground Database File
        configdatabaseFile = new File(getDataFolder() + "/battlegroundDatabase.yml");
        xBndDatabase = YamlConfiguration.loadConfiguration(configdatabaseFile);
        xBndDatabase.options().copyDefaults(true);
        try {
            Reader inputReader = new InputStreamReader(this.getResource("battlegroundDatabase.yml"), "UTF8");
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(inputReader);
            xBndDatabase.setDefaults(defaults);
        } catch (UnsupportedEncodingException e1) { getLogger().severe("Failed to load Battleground Database File."); }
    }

    //Disable
    public void onDisable(){

        //Save Battleground Database
        try {
            xBndDatabase.save(configdatabaseFile);
        } catch (IOException e) { getLogger().severe("Could not save Battleground Database File."); }

        //Log Disable
        this.getLogger().info("Disabling MobWars...");
    }
}
