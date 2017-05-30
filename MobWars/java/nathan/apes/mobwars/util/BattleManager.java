package nathan.apes.mobwars.util;

import nathan.apes.mobwars.battle.Battle;
import nathan.apes.mobwars.main.MobWars;

import java.util.*;

import nathan.apes.mobwars.world.battle.FindBattleground;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static nathan.apes.mobwars.main.MobWars.loggingPrefix;
import static nathan.apes.mobwars.main.MobWars.scheduler;

//The Battle Manager: Manages and Creates all on-going Battles

public class BattleManager {

    //Players assiociating with one battle
    public static ArrayList<Player> battlePlayers = new ArrayList<>();

    //All ongoing battles
    public static ArrayList<Battle> currbattles = new ArrayList<>();

    public static int battleind = -1;
    //Remove on overhaul

    //Main reference
    private final JavaPlugin mainClass = JavaPlugin.getProvidingPlugin(MobWars.class);

    //Initialize PlayerMatchMaking
    public BattleManager(){
        scheduler.scheduleSyncRepeatingTask(mainClass, () -> matchMaking(), 0L, 40L);
    }

    //Matchmake players in the BattlePlayerList
    private void matchMaking(){
        if(battlePlayers.size() == 10){

            battleind++;

            battlePlayers.forEach(player -> player.sendMessage(loggingPrefix + ChatColor.GOLD + "You have a game! It's now time for the battle!"));
            currbattles.add(new Battle(battlePlayers, new FindBattleground().findBattleground(), 0));
        }
    }

    //Gets a battle out of the array
    public static Battle getBattle(int index){ return currbattles.get(index); }

}