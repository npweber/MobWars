package nathan.apes.mobwars.event.battle;

import nathan.apes.mobwars.battle.Battle;
import nathan.apes.mobwars.battle.Squad;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static nathan.apes.mobwars.main.MobWars.loggingPrefix;

//CommanderAction Event: Listens for the commanders decisions and relays them for the troops to take action

public class EventCommanderAction implements Listener {

    public static Squad pickedSquad;

    @EventHandler
    public void onCommandingAction(PlayerInteractEvent pie){

        //Get player
        Player commander = pie.getPlayer();

        //Specify conditions
        if(commander.getLocation().getWorld().equals(Bukkit.getWorld("mw_BattleWorld"))) {
            if (pie.hasItem()) {
                //If the item is the commanders get his action
                if (pie.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Commander's Baton")) {
                    //If he points, find the location he points to and march the Squadron to the position
                    if (pie.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        Location pointLocation = pie.getClickedBlock().getLocation();
                        if(!(pickedSquad == null)) {
                            pickedSquad.marchTo(pointLocation, Battle.getSquadIndex(commander, pickedSquad));
                            commander.sendMessage(loggingPrefix + ChatColor.GREEN + "You told your selected squad to march forth.");
                        } else commander.sendMessage(loggingPrefix + ChatColor.RED + "Select a Squadron in order to give orders...");
                    }
                    //If he withdrawls, halt the postition
                    if (pie.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                        if(!(pickedSquad == null)) {
                            Squad.getSquadPlayer(commander).halt(Battle.getSquadIndex(commander, pickedSquad));
                            commander.sendMessage(loggingPrefix + ChatColor.GOLD + "You halted your selected Squad.");
                        } else commander.sendMessage(loggingPrefix + ChatColor.RED + "Select a Squadron in order to give orders...");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSquadPick(PlayerInteractAtEntityEvent pie){

        //Get player
        Player commander = pie.getPlayer();

        //Specify conditions
        if(commander.getLocation().getWorld().equals(Bukkit.getWorld("mw_BattleWorld"))) {
            if (pie.getRightClicked().getType().equals(EntityType.PLAYER)) {
                if (!(pie.getPlayer().getInventory().getItemInMainHand() == null)) {
                    //If the item is the commanders get his action
                    if (pie.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("Commander's Baton")) {
                        //If he points, find the squad he picks
                        pickedSquad = Squad.getSquadPlayer((Player) pie.getRightClicked());
                        commander.sendMessage(loggingPrefix + ChatColor.GOLD + "You selected Squad " + (Battle.getSquadIndex(commander, pickedSquad) + 1) + ".");
                    }
                }
            }
        }
    }
}
