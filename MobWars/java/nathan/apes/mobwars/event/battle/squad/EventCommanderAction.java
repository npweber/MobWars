package nathan.apes.mobwars.event.battle.squad;

import nathan.apes.mobwars.battle.Battle;
import nathan.apes.mobwars.battle.Squad;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.security.acl.LastOwnerException;

import static nathan.apes.mobwars.event.battle.squad.EventCommanderPickSquad.pickedSquad;

//CommanderAction Event: Listens for the commanders decisions and relays them for the troops to take action

public class EventCommanderAction implements Listener {

    @EventHandler
    public void onCommandingAction(PlayerInteractEvent pie){

        //Get player
        Player commander = pie.getPlayer();

        //Specify conditions
        if(commander.getLocation().getWorld().equals(Bukkit.getWorld("mw_BattleWorld"))) {
            if (pie.hasItem()) {
                //If the item is the commanders get his action
                if (pie.getItem().equals(new ItemStack(Material.BLAZE_ROD))) {
                    //If he points, find the location he points to and march the Squadron to the position
                    if (pie.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        Location pointLocation = pie.getClickedBlock().getLocation();
                        if(!(pickedSquad == null))
                        pickedSquad.marchTo(pointLocation);
                    }
                    //If he withdrawls, halt the postition
                    if (pie.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                        Squad.getSquadPlayer(commander, Battle.getPlayerBattle(commander).battleindex).halt();
                    }
                }
            }
        }
    }
}
