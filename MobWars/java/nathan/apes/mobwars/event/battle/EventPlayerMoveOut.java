package nathan.apes.mobwars.event.battle;

import nathan.apes.mobwars.battle.Battle;
import nathan.apes.mobwars.battle.Squad;
import nathan.apes.mobwars.util.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

//EventPlayerMoveOut: Restrains the player to the Battegrounds & If they are in a squad they are restrained to a squad

public class EventPlayerMoveOut implements Listener{

    @EventHandler
    public void onPlayerMoveOutOfBounds(PlayerMoveEvent pme){

        //Get Player
        Player pl = pme.getPlayer();

        //Manage all Battles triggering the event
        for(int i = 0; i < BattleManager.getBattles().size(); i++){

            //Check for if in battle
            if(Battle.getBattlePlayers(i).contains(pl)) {

                int x = (int) Battle.getBattleArea(i).getX();
                int z = (int) Battle.getBattleArea(i).getZ();
                Location ploc = pl.getLocation();
                double plx = ploc.getX();
                double plz = ploc.getZ();

                //Send back if trying to go out
                if ((plx < x) || (plx > (x + 200))) {
                    pl.teleport(ploc);
                    pl.sendMessage(ChatColor.RED + "Don't leave the battle!");
                }
                if ((plz < (z - 200)) || (plz > z)) {
                    pl.teleport(ploc);
                    pl.sendMessage(ChatColor.RED + "Don't leave the battle!");
                }

                //Check if in a squad
                if (Squad.isPlayerInSquad(pl)) {
                    //Define Squad Object
                    Squad squad = Squad.getSquadPlayer(pl);

                    //Check if they are in formation
                    if (Squad.getInForm(i, Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad)))
                        //If so, restrain to position
                        pl.teleport(ploc);
                }
            }
        }
    }
}
