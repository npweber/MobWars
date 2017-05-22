package nathan.apes.mobwars.util;

import nathan.apes.mobwars.battle.Battle;
import nathan.apes.mobwars.battle.util.UpdateBattleStage;
import nathan.apes.mobwars.world.battle.*;

import java.util.*;

import nathan.apes.mobwars.world.lobby.InitLobbyWorld;
import org.bukkit.*;
import org.bukkit.entity.Player;

import static nathan.apes.mobwars.main.MobWars.loggingPrefix;

//The Battle Manager: Manages and Creates all on-going Battles

public class BattleManager {

    //Players assiociating with one battle
    public static ArrayList<Player> battlePlayers = new ArrayList<>();

    //All ongoing battles
    public static ArrayList<Battle> currbattles = new ArrayList<>();

    //Battle Environment Runner Switch
    public static boolean battlesEnabled = false;

    public static int battleind = -1;
    //Remove on decision

    //Initialize BattleManaging/PlayerMatchMaking
    public BattleManager(){
        do {
            matchMaking();
        } while (InitLobbyWorld.getMatchMakingStatus());
        battlesEnabled = true;
    }

    //Matchmake players in the BattlePlayerList
    private void matchMaking(){
        if(battlePlayers.size() == 18){

            battleind++;

            //if(battleind == 0){ new UpdateBattleStage(); }
            //(Temp) Remove or keep on Decision

            battlePlayers.forEach(player -> player.sendMessage(loggingPrefix + ChatColor.GREEN + "You have a game! It's now time for the battle!"));
            currbattles.add(new Battle(battlePlayers, new FindBattleground().findBattleground(), battleind));

            battlePlayers.clear();
        }
    }

    //Gets a battle out of the array
    public static Battle getBattle(int index){ return currbattles.get(index); }
    
}
