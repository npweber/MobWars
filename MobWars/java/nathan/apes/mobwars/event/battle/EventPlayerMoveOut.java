package nathan.apes.mobwars.event.battle;

import nathan.apes.mobwars.battle.Battle;
import nathan.apes.mobwars.battle.Squad;
import nathan.apes.mobwars.util.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import static nathan.apes.mobwars.util.BattleManager.battlePlayers;
import static nathan.apes.mobwars.util.BattleManager.currbattles;

//EventPlayerMoveOut: Restrains the player to the Battegrounds & If they are in a squad they are restrained to a squad

public class EventPlayerMoveOut implements Listener{

    @EventHandler
    public void onPlayerMoveOutOfBounds(PlayerMoveEvent pme){

        //Get Player
        Player pl = pme.getPlayer();

        //Manage all Battles triggering the event
        for(int i = 0; i < BattleManager.currbattles.size(); i++){
            Battle b = BattleManager.currbattles.get(i);

            //Check for if in battle
            if(b.isPlayerInBattle(pl)) {
                int x = (int) b.battlearea.getX();
                int z = (int) b.battlearea.getZ();
                Location ploc = pl.getLocation();
                double plx = ploc.getX();
                double plz = ploc.getZ();

                //Send back if trying to go out
                if ((plx < x) || (plx > (x + 200)))
                    pl.teleport(ploc);
                if ((plz < (z - 200)) || (plz > z))
                    pl.teleport(ploc);

                //Check if in a squad
                if (Squad.isPlayerInSquad(pl) && Squad.getSquadPlayer(pl).getInForm())
                    //If so, restrain to position
                    pl.teleport(ploc);
            }
        }
    }
}
