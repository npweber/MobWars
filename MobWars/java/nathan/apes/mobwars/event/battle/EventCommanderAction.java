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

import static nathan.apes.mobwars.battle.Battle.opposingCommanders;
import static nathan.apes.mobwars.main.MobWars.loggingPrefix;

//CommanderAction Event: Listens for the commanders decisions and relays them for the troops to take action

public class EventCommanderAction implements Listener {

    public static Squad commander1Squad;
    public static Squad commander2Squad;

    //Command the Squad
    @EventHandler
    public void onCommandingAction(PlayerInteractEvent pie){

        //Get player
        Player commander = pie.getPlayer();

        //Specify conditions
        if(commander.getLocation().getWorld().equals(Bukkit.getWorld("mw_BattleWorld"))) {
            if (commander == opposingCommanders[0] || commander == opposingCommanders[1]) {
                //If the item is the commanders get his action
                if (pie.getPlayer().getInventory().getHeldItemSlot() == 0) {
                    //If he points, find the location he points to and march the Squadron to the position
                    if (pie.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        Location pointLocation = pie.getClickedBlock().getLocation();
                        if (commander == opposingCommanders[0])
                            if (!(commander1Squad == null)) {
                                Squad.marchTo(pointLocation, Battle.getSquadIndex(0, commander1Squad));
                                if(Squad.getRetreatStatus(Battle.getSquadIndex(0, commander1Squad)))
                                    Squad.setRetreatStatus(Battle.getSquadIndex(0, commander1Squad), false);
                                commander.sendMessage(loggingPrefix + ChatColor.GREEN + "You told your selected squad to march forth.");
                            } else
                                commander.sendMessage(loggingPrefix + ChatColor.RED + "Select a Squadron in order to give orders...");
                        else if (!(commander2Squad == null)) {
                            Squad.marchTo(pointLocation, Battle.getSquadIndex(0, commander2Squad));
                            if(Squad.getRetreatStatus(Battle.getSquadIndex(0, commander2Squad)))
                                Squad.setRetreatStatus(Battle.getSquadIndex(0, commander2Squad), false);
                            commander.sendMessage(loggingPrefix + ChatColor.GREEN + "You told your selected squad to march forth.");
                        } else
                            commander.sendMessage(loggingPrefix + ChatColor.RED + "Select a Squadron in order to give orders...");
                    }
                    //If he withdrawls, halt the postition
                    if (pie.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                        if (commander == opposingCommanders[0])
                            if (!(commander1Squad == null)) {
                                Squad.halt(Battle.getSquadIndex(0, commander1Squad));
                                if(Squad.getRetreatStatus(Battle.getSquadIndex(0, commander1Squad)))
                                    Squad.setRetreatStatus(Battle.getSquadIndex(0, commander1Squad), false);
                                commander.sendMessage(loggingPrefix + ChatColor.GOLD + "You halted your selected Squad.");
                            } else
                                commander.sendMessage(loggingPrefix + ChatColor.RED + "Select a Squadron in order to give orders...");
                        else if (commander == opposingCommanders[1])
                            if (!(commander2Squad == null)) {
                                Squad.halt(Battle.getSquadIndex(0, commander2Squad));
                                if(Squad.getRetreatStatus(Battle.getSquadIndex(0, commander2Squad)))
                                    Squad.setRetreatStatus(Battle.getSquadIndex(0, commander2Squad), false);
                                commander.sendMessage(loggingPrefix + ChatColor.GOLD + "You halted your selected Squad.");
                            } else
                                commander.sendMessage(loggingPrefix + ChatColor.RED + "Select a Squadron in order to give orders...");
                    }
                }
                //Give the retreat order, on the commanders call
                if (pie.getPlayer().getInventory().getHeldItemSlot() == 2) {
                    if (pie.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        Location pointLocation = pie.getClickedBlock().getLocation();
                        if (commander == opposingCommanders[0])
                            if (!(commander1Squad == null)) {
                                Squad.marchTo(pointLocation, Battle.getSquadIndex(0, commander1Squad));
                                Squad.setRetreatStatus(Battle.getSquadIndex(0, commander1Squad), true);
                                commander.sendMessage(loggingPrefix + ChatColor.GREEN + "You told your selected squad to retreat.");
                            } else
                                commander.sendMessage(loggingPrefix + ChatColor.RED + "Select a Squadron in order to give orders...");
                        else
                            if (!(commander2Squad == null)) {
                                Squad.marchTo(pointLocation, Battle.getSquadIndex(0, commander2Squad));
                                Squad.setRetreatStatus(Battle.getSquadIndex(0, commander2Squad), true);
                                commander.sendMessage(loggingPrefix + ChatColor.GREEN + "You told your selected squad to retreat.");
                            } else
                                commander.sendMessage(loggingPrefix + ChatColor.RED + "Select a Squadron in order to give orders...");
                    }
                }
            }
        }
    }

    //Pick a Squad
    @EventHandler
    public void onSquadPick(PlayerInteractAtEntityEvent pie){

        //Get player
        Player commander = pie.getPlayer();

        //Specify conditions
        if(commander.getLocation().getWorld().equals(Bukkit.getWorld("mw_BattleWorld"))) {
            if (pie.getRightClicked().getType().equals(EntityType.PLAYER)) {
                if (commander == opposingCommanders[0] || commander == opposingCommanders[1]) {
                    //If the item is the commanders get his action
                    if (pie.getPlayer().getInventory().getHeldItemSlot() == 1) {
                        //If he points, find the squad he picks
                        if(Squad.isPlayerInSquad((Player) pie.getRightClicked())) {
                            if (commander == opposingCommanders[0]) {
                                commander1Squad = Squad.getSquadPlayer((Player) pie.getRightClicked());
                                commander.sendMessage(loggingPrefix + ChatColor.GOLD + "You selected Squad " + (Battle.getSquadIndex(0, commander1Squad) + 1) + ".");
                            } else if (commander == opposingCommanders[1]) {
                                commander2Squad = Squad.getSquadPlayer((Player) pie.getRightClicked());
                                commander.sendMessage(loggingPrefix + ChatColor.GOLD + "You selected Squad " + (Battle.getSquadIndex(0, commander2Squad) + 1) + ".");
                            }
                        }
                    }
                }
            }
        }
    }
}
