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
        for(int i = 0; i < BattleManager.currbattles.size(); i++){
            Battle b = BattleManager.currbattles.get(i);

            //Check for if in battle
            if(b.isPlayerInBattle(pl)){
                int x = (int) b.battlearea.getX();
                int z = (int) b.battlearea.getZ();
                Location ploc = pl.getLocation();
                double plx = ploc.getX();
                double plz = ploc.getZ();
                Location newloc = ploc;

                //Send back if trying to go out
                if(plx > x || plx < x - 200){
                    if(plx > 0){ newloc.setX(plx - 1); pl.teleport(newloc); }
                    if(plx < 0){ newloc.setX(plx + 1); pl.teleport(newloc); }
                }
                if(plz > z || plz < z - 200){
                    if(plz > 0){ newloc.setZ(plz - 1); pl.teleport(newloc); }
                    if(plz < 0){ newloc.setZ(plz + 1); pl.teleport(newloc); }
                }

                //Check if in a squad
                if(Squad.isPlayerInSquad(pl) && Squad.getSquadPlayer(pl, b.battleindex).getInForm())
                    //If so, restrain to position
                    pl.teleport(pl.getLocation());
            }
        }
    }
}
