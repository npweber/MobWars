package nathan.apes.mobwars.event.battle;

import nathan.apes.mobwars.battle.Battle;
import nathan.apes.mobwars.battle.Squad;
import nathan.apes.mobwars.util.BattleManager;
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
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;

import static nathan.apes.mobwars.battle.Battle.getCommanders;
import static nathan.apes.mobwars.main.MobWars.loggingPrefix;

//CommanderAction Event: Listens for the commanders decisions and relays them for the troops to take action

public class EventCommanderAction implements Listener {

    public static ArrayList<Squad> commander1Squad = new ArrayList<>();
    public static ArrayList<Squad> commander2Squad = new ArrayList<>();

    //Command the Squad
    @EventHandler
    public void onCommandingAction(PlayerInteractEvent pie){

        //Get player
        Player commander = pie.getPlayer();

        //Specify conditions
        if(commander.getLocation().getWorld().equals(Bukkit.getWorld("mw_BattleWorld"))) {
            if (commander == getCommanders(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))[0] || commander == getCommanders(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))[1]) {

                //Define Battle
                Battle battle = Battle.getPlayerBattle(commander);

                //If the item is the commanders get his action
                if (pie.getPlayer().getInventory().getHeldItemSlot() == 0) {
                    //If he points, find the location he points to and march the Squadron to the position
                    if (pie.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        Location pointLocation = pie.getClickedBlock().getLocation();
                        if (commander == getCommanders(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))[0])
                            if (!(commander1Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander))) == null)) {
                                if(!(Squad.isDisabled(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), commander1Squad.get(BattleManager.getBattleIndex(battle)))))) {
                                    Squad.marchTo(pointLocation, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander1Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))));
                                    if (Squad.getRetreatStatus(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander1Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander))))))
                                        Squad.setRetreatStatus(false, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander1Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))));
                                    commander.sendMessage(loggingPrefix + ChatColor.GREEN + "You told your selected squad to march forth.");
                                } else {
                                    commander.sendMessage(loggingPrefix + ChatColor.RED + "This Squad is DEAD. You cannot use it.");
                                    pie.setCancelled(true);
                                }
                            } else
                                commander.sendMessage(loggingPrefix + ChatColor.RED + "Select a Squadron in order to give orders...");
                        if (commander == getCommanders(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))[1])
                            if (!(commander2Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander))) == null)) {
                                if(!(Squad.isDisabled(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), commander2Squad.get(BattleManager.getBattleIndex(battle)))))) {
                                    Squad.marchTo(pointLocation, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander2Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))));
                                    if (Squad.getRetreatStatus(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander2Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander))))))
                                        Squad.setRetreatStatus(false, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander2Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))));
                                    commander.sendMessage(loggingPrefix + ChatColor.GREEN + "You told your selected squad to march forth.");
                                } else {
                                    commander.sendMessage(loggingPrefix + ChatColor.RED + "This Squad is DEAD. You cannot use it.");
                                    pie.setCancelled(true);
                                }
                            } else
                                commander.sendMessage(loggingPrefix + ChatColor.RED + "Select a Squadron in order to give orders...");
                    }
                    //If he withdrawls, halt the postition
                    if (pie.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                        if (commander == getCommanders(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))[0])
                            if (!(commander1Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander))) == null)) {
                                if(!(Squad.isDisabled(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), commander1Squad.get(BattleManager.getBattleIndex(battle)))))) {
                                    Squad.halt(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander1Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))));
                                    if (Squad.getRetreatStatus(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander1Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander))))))
                                        Squad.setRetreatStatus(false, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander1Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))));
                                    commander.sendMessage(loggingPrefix + ChatColor.GOLD + "You halted your selected Squad.");
                                } else {
                                    commander.sendMessage(loggingPrefix + ChatColor.RED + "This Squad is DEAD. You cannot use it.");
                                    pie.setCancelled(true);
                                }
                            } else
                                commander.sendMessage(loggingPrefix + ChatColor.RED + "Select a Squadron in order to give orders...");
                        if (commander == getCommanders(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))[1])
                            if (!(commander2Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander))) == null)) {
                                if(!(Squad.isDisabled(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), commander2Squad.get(BattleManager.getBattleIndex(battle)))))) {
                                    Squad.halt(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander2Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))));
                                    if (Squad.getRetreatStatus(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander2Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander))))))
                                        Squad.setRetreatStatus(false, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander2Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))));
                                    commander.sendMessage(loggingPrefix + ChatColor.GOLD + "You halted your selected Squad.");
                                } else {
                                    commander.sendMessage(loggingPrefix + ChatColor.RED + "This Squad is DEAD. You cannot use it.");
                                    pie.setCancelled(true);
                                }
                            } else
                                commander.sendMessage(loggingPrefix + ChatColor.RED + "Select a Squadron in order to give orders...");
                    }
                }
                //Give the retreat order, on the commanders call
                if (pie.getPlayer().getInventory().getHeldItemSlot() == 2) {
                    if (pie.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        Location pointLocation = pie.getClickedBlock().getLocation();
                        if (commander == getCommanders(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))[0])
                            if (!(commander1Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander))) == null)) {
                                if(!(Squad.isDisabled(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), commander1Squad.get(BattleManager.getBattleIndex(battle)))))) {
                                    Squad.marchTo(pointLocation, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander1Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))));
                                    Squad.setRetreatStatus(true, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander1Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))));
                                    commander.sendMessage(loggingPrefix + ChatColor.GREEN + "You told your selected squad to retreat.");
                                } else {
                                    commander.sendMessage(loggingPrefix + ChatColor.RED + "This Squad is DEAD. You cannot use it.");
                                    pie.setCancelled(true);
                                }
                            } else
                                commander.sendMessage(loggingPrefix + ChatColor.RED + "Select a Squadron in order to give orders...");
                        if (commander == getCommanders(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))[1])
                            if (!(commander2Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander))) == null)) {
                                if(!(Squad.isDisabled(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), commander2Squad.get(BattleManager.getBattleIndex(battle)))))) {
                                    Squad.marchTo(pointLocation, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander2Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))));
                                    Squad.setRetreatStatus(true, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), commander2Squad.get(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))));
                                    commander.sendMessage(loggingPrefix + ChatColor.GREEN + "You told your selected squad to retreat.");
                                } else {
                                    commander.sendMessage(loggingPrefix + ChatColor.RED + "This Squad is DEAD. You cannot use it.");
                                    pie.setCancelled(true);
                                }
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
                if (commander == getCommanders(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))[0] || commander == getCommanders(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))[1]) {
                    //If the item is the commanders get his action
                    if (pie.getHand().equals(EquipmentSlot.HAND) && pie.getPlayer().getInventory().getHeldItemSlot() == 1) {
                        //If he points, find the squad he picks
                        if(Squad.isPlayerInSquad((Player) pie.getRightClicked())) {
                            //Make sure it's a working Squad
                            if (!(Squad.isDisabled(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), Squad.getSquadPlayer((Player) pie.getRightClicked()))))){
                                if (commander == getCommanders(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))[0]) {
                                    commander1Squad.set(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), Squad.getSquadPlayer((Player) pie.getRightClicked()));
                                    commander.sendMessage(loggingPrefix + ChatColor.GOLD + "You selected Squad " + ChatColor.AQUA + (Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), Squad.getSquadPlayer((Player) pie.getRightClicked())) + 1) + ChatColor.GOLD + ".");
                                }
                                if (commander == getCommanders(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)))[1]) {
                                    commander2Squad.set(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), Squad.getSquadPlayer((Player) pie.getRightClicked()));
                                    commander.sendMessage(loggingPrefix + ChatColor.GOLD + "You selected Squad " + ChatColor.AQUA + ((Battle.getSquadIndex(BattleManager.getBattleIndex(Battle.getPlayerBattle(commander)), Squad.getSquadPlayer((Player) pie.getRightClicked())) - 2) + 1) + ChatColor.GOLD + ".");
                                }
                            } else commander.sendMessage(loggingPrefix + ChatColor.RED + " You cannot pick this Squad. It has died in action.");
                        }
                    }
                }
            }
        }
    }
}
