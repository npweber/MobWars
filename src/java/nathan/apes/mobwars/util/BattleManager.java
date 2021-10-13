package nathan.apes.mobwars.util;

import nathan.apes.mobwars.battle.Battle;
import nathan.apes.mobwars.battle.Squad;
import nathan.apes.mobwars.event.battle.EventCombat;
import nathan.apes.mobwars.event.battle.EventCommanderAction;
import nathan.apes.mobwars.event.battle.EventPlayerMoveOut;
import nathan.apes.mobwars.main.MobWars;

import java.util.*;

import nathan.apes.mobwars.world.battle.FindBattleground;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static nathan.apes.mobwars.main.MobWars.*;

//The Battle Manager: Manages and Creates all on-going Battles

public class BattleManager {

    //Players associating with one battle
    public static ArrayList<Player> battlePlayers = new ArrayList<>();

    //All ongoing battles
    private static ArrayList<Battle> currbattles = new ArrayList<>();

    //Battle Index Counter
    private int battleind = -1;

    //Main reference
    private final JavaPlugin mainClass = JavaPlugin.getProvidingPlugin(MobWars.class);

    //Initialize PlayerMatchMaking and Functions
    public BattleManager(){

        //Init MatchMaking
        scheduler.scheduleSyncRepeatingTask(mainClass, () -> matchMaking(), 0L, 40L);

        //Update Movements
        scheduler.scheduleSyncRepeatingTask(mainClass, () -> currbattles.forEach(
            battle ->
                Battle.getSquads(currbattles.indexOf(battle)).forEach(
                    squad -> {
                        squad.move(currbattles.indexOf(battle), Battle.getSquadIndex(currbattles.indexOf(battle), squad));
                        Squad.setSquadLocation(
                            Squad.getSquadPlayers(currbattles.indexOf(battle),
                            Battle.getSquadIndex(currbattles.indexOf(battle), squad)).get(0).getLocation(),
                                currbattles.indexOf(battle), Battle.getSquadIndex(currbattles.indexOf(battle)
                                , squad
                            )
                        );
                    }
                )
            ),
        0L, 12L);

        //Update Game Ending
        scheduler.scheduleSyncRepeatingTask(mainClass,
            () -> {
                currbattles.forEach(battle -> Battle.getSquads(BattleManager.getBattleIndex(battle)).forEach(
                    squad -> {
                        if(Squad.getHealth(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), squad)) < 0.5) {
                            if(!(Squad.getInForm(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), squad)))) {
                                if(!disabling) {
                                    Battle.getBattlePlayers(BattleManager.getBattleIndex(battle)).forEach(
                                        player ->
                                            player.sendMessage(loggingPrefix + ChatColor.RED + "A Squad of Army " + ChatColor.GOLD +
                                                (Squad.getArmyIndex(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), squad)) + 1)
                                                + ChatColor.RED + " has fallen!"
                                            )
                                    );
                                    Squad.getSquadPlayers(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), squad)).forEach(
                                        player -> player.sendMessage(ChatColor.RED + " Your squad has died in action!"));
                                }
                            }
                            Squad.setInForm(true, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), squad));
                            Squad.setDisabled(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), squad), true);
                            if((Squad.getDisableds(BattleManager.getBattleIndex(battle)).get(0).equals(Boolean.TRUE)
                                && Squad.getDisableds(BattleManager.getBattleIndex(battle)).get(1).equals(Boolean.TRUE))
                            || (Squad.getDisableds(BattleManager.getBattleIndex(battle)).get(2).equals(Boolean.TRUE)
                                && Squad.getDisableds(BattleManager.getBattleIndex(battle)).get(3).equals(Boolean.TRUE))) {
                                    Battle.getBattlePlayers(BattleManager.getBattleIndex(battle)).forEach(
                                        player -> {
                                            player.setAllowFlight(false);
                                            player.setInvulnerable(false);
                                            player.getInventory().clear();
                                            player.getInventory().setArmorContents(new ItemStack[]{});
                                            if(config.isSet("lobbyspawnX") && config.isSet("lobbyspawnY") && config.isSet("lobbyspawnZ"))
                                                player.teleport(new Location(Bukkit.getWorld("mw_Lobby"), config.getDouble("lobbyspawnX"),
                                                    config.getDouble("lobbyspawnY"), config.getDouble("lobbyspawnZ")
                                                ));
                                            else {
                                                mainClass.getLogger().severe("Could not end game properly. Operator forgot to set the Lobby Cords. Cannot send users back...");
                                                player.sendMessage(ChatColor.RED + "Please contact the Operator. Your match has failed to return you to the lobby.");
                                            }
                                        }
                                    );
                                    String[] endMessages = new String[]{
                                        loggingPrefix + ChatColor.DARK_RED + "The Battle is LOST.",
                                        loggingPrefix + ChatColor.GREEN + "You WON the Battle!",
                                        loggingPrefix + ChatColor.BLUE + "Your game was stopped due to the plugin being disabled."
                                    };
                                    if(!disabling){
                                        if (Squad.getDisableds(BattleManager.getBattleIndex(battle)).get(0).equals(Boolean.TRUE)
                                                && Squad.getDisableds(BattleManager.getBattleIndex(battle)).get(1).equals(Boolean.TRUE)) {
                                            Battle.getCommanders(BattleManager.getBattleIndex(battle))[0].sendMessage(endMessages[0]);
                                            Battle.getCommanders(BattleManager.getBattleIndex(battle))[0].playSound(
                                                Battle.getCommanders(BattleManager.getBattleIndex(battle))[0].getLocation(),
                                                Sound.ENTITY_GHAST_DEATH, 1.0f, 1.0f
                                            );
                                            Battle.getCommanders(BattleManager.getBattleIndex(battle))[1].sendMessage(endMessages[1]);
                                            Battle.getCommanders(BattleManager.getBattleIndex(battle))[1].playSound(
                                                Battle.getCommanders(BattleManager.getBattleIndex(battle))[1].getLocation(),
                                                Sound.ENTITY_FIREWORK_TWINKLE, 1.0f, 1.0f
                                            );
                                            Squad.getSquadPlayers(BattleManager.getBattleIndex(battle), 0).forEach(
                                                player -> {
                                                    player.sendMessage(endMessages[0]);
                                                    player.playSound(player.getLocation(), Sound.ENTITY_GHAST_DEATH, 1.0f, 1.0f);
                                                }
                                            );
                                            Squad.getSquadPlayers(BattleManager.getBattleIndex(battle), 1).forEach(
                                                player -> {
                                                    player.sendMessage(endMessages[0]);
                                                    player.playSound(player.getLocation(), Sound.ENTITY_GHAST_DEATH, 1.0f, 1.0f);
                                                }
                                            );
                                            Squad.getSquadPlayers(BattleManager.getBattleIndex(battle), 2).forEach(
                                                player -> {
                                                    player.sendMessage(endMessages[1]);
                                                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 1.0f, 1.0f);
                                                }
                                            );
                                            Squad.getSquadPlayers(BattleManager.getBattleIndex(battle), 3).forEach(
                                                player -> {
                                                    player.sendMessage(endMessages[1]);
                                                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 1.0f, 1.0f);
                                                }
                                            );
                                        }
                                        if (Squad.getDisableds(BattleManager.getBattleIndex(battle)).get(2).equals(Boolean.TRUE)
                                                && Squad.getDisableds(BattleManager.getBattleIndex(battle)).get(3).equals(Boolean.TRUE)) {
                                            Battle.getCommanders(BattleManager.getBattleIndex(battle))[0].sendMessage(endMessages[1]);
                                            Battle.getCommanders(BattleManager.getBattleIndex(battle))[0].playSound(
                                                Battle.getCommanders(BattleManager.getBattleIndex(battle))[0].getLocation(),
                                                Sound.ENTITY_FIREWORK_TWINKLE, 1.0f, 1.0f
                                            );
                                            Battle.getCommanders(BattleManager.getBattleIndex(battle))[1].sendMessage(endMessages[0]);
                                            Battle.getCommanders(BattleManager.getBattleIndex(battle))[1].playSound(
                                                Battle.getCommanders(BattleManager.getBattleIndex(battle))[1].getLocation(),
                                                Sound.ENTITY_GHAST_DEATH, 1.0f, 1.0f
                                            );
                                            Squad.getSquadPlayers(BattleManager.getBattleIndex(battle), 0).forEach(
                                                player -> {
                                                    player.sendMessage(endMessages[1]);
                                                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 1.0f, 1.0f);
                                                }
                                            );
                                            Squad.getSquadPlayers(BattleManager.getBattleIndex(battle), 1).forEach(
                                                player -> {
                                                    player.sendMessage(endMessages[1]);
                                                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 1.0f, 1.0f);
                                                }
                                            );
                                            Squad.getSquadPlayers(BattleManager.getBattleIndex(battle), 2).forEach(
                                                player -> {
                                                    player.sendMessage(endMessages[0]);
                                                    player.playSound(player.getLocation(), Sound.ENTITY_GHAST_DEATH, 1.0f, 1.0f);
                                                }
                                            );
                                            Squad.getSquadPlayers(BattleManager.getBattleIndex(battle), 3).forEach(
                                                player -> {
                                                    player.sendMessage(endMessages[0]);
                                                    player.playSound(player.getLocation(), Sound.ENTITY_GHAST_DEATH, 1.0f, 1.0f);
                                                }
                                            );
                                        }
                                    }
                                    if(disabling && !disabled){
                                        Battle.getBattlePlayers(BattleManager.getBattleIndex(battle)).forEach(player -> player.sendMessage(endMessages[2]));
                                        disabled = true;
                                    }
                            }
                        }
                    }
                ));
            }
        , 0L, 40L);
        scheduler.scheduleSyncRepeatingTask(mainClass, () -> currbattles.forEach(
            battle -> {
                if(!(bw.getEntities().containsAll(Battle.getBattlePlayers(BattleManager.getBattleIndex(battle))))) {
                    Battle.getAllSquads().remove(BattleManager.getBattleIndex(battle));
                    Battle.getAllBattlePlayers().remove(BattleManager.getBattleIndex(battle));
                    Battle.getBattleAreas().remove(BattleManager.getBattleIndex(battle));
                    Squad.getAllArmyIndex().remove(BattleManager.getBattleIndex(battle));
                    Squad.getAllDestination().remove(BattleManager.getBattleIndex(battle));
                    Squad.getAllDisabled().remove(BattleManager.getBattleIndex(battle));
                    Squad.getAllForms().remove(BattleManager.getBattleIndex(battle));
                    Squad.getAllHealth().remove(BattleManager.getBattleIndex(battle));
                    Squad.getAllOwner().remove(BattleManager.getBattleIndex(battle));
                    Squad.getAllSquadLocation().remove(BattleManager.getBattleIndex(battle));
                    Squad.getAllRetreat().remove(BattleManager.getBattleIndex(battle));
                    Squad.getAllSquadPlayer().remove(BattleManager.getBattleIndex(battle));
                    Squad.getAllHealthIdentifiers().get(BattleManager.getBattleIndex(battle)).forEach(enity -> enity.remove());
                    Squad.getAllHealthIdentifiers().remove(BattleManager.getBattleIndex(battle));
                    EventCommanderAction.commander1Squad.remove(BattleManager.getBattleIndex(battle));
                    EventCommanderAction.commander2Squad.remove(BattleManager.getBattleIndex(battle));
                    BattleManager.getBattles().remove(battle);
                    battleind--;
                }
            }
        ), 0L, 40L);

        //Update Health Identifiers
        scheduler.scheduleSyncRepeatingTask(mainClass,
            () -> getBattles().forEach(battle -> Battle.getSquads(BattleManager.getBattleIndex(battle)).forEach(
                squad -> {
                    ArmorStand healthTag = Squad.getHealthIdentifier(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), squad));
                    StringBuilder tagString = new StringBuilder(healthTag.getCustomName());
                    tagString.replace(15, tagString.length(),
                        " " + Squad.getHealth(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(battle), squad)) + "HP"
                    );
                    healthTag.setCustomName(tagString.toString());
                }
            ))
        , 20L, 40L);

        //Send Health Bank Status
        scheduler.scheduleSyncRepeatingTask(mainClass, () -> getBattles().forEach(
            battle -> {
                for(int i = 0; i < 2; i++){
                    Battle.getCommanders(BattleManager.getBattleIndex(battle))[i]
                        .sendMessage(ChatColor.BLUE + "Your Health Bank is at ["
                            + Battle.getArmyHealthBank(BattleManager.getBattleIndex(battle))[i] + "HP]"
                        );
                }
            })
        , 20L, 900L);

        //Init Core Game Functions
        mainClass.getServer().getPluginManager().registerEvents(new EventPlayerMoveOut(), mainClass);
        mainClass.getServer().getPluginManager().registerEvents(new EventCommanderAction(), mainClass);
        mainClass.getServer().getPluginManager().registerEvents(new EventCombat(), mainClass);
    }

    //Matchmake players in the BattlePlayerList
    private void matchMaking(){
        if(battlePlayers.size() == 10){

            //Control battle index
            battleind++;

            //Log MatchMake
            battlePlayers.forEach(
                player -> {
                    player.sendMessage(loggingPrefix + ChatColor.GOLD + "You have a game! It's now time for the battle!");
                    player.sendMessage(loggingPrefix + ChatColor.AQUA + "You will now be teleported to a "
                        + ChatColor.GOLD + "Randomly Generated Battlefield" + ChatColor.AQUA + ". Take it on with pride!"
                    );
                }
            );

            //Misc Init
            EventCommanderAction.commander1Squad.add(null);
            EventCommanderAction.commander2Squad.add(null);

            //Start Battle
            currbattles.add(new Battle(battlePlayers, new FindBattleground().findBattleground(), battleind));
        }
    }

    //Get the current Battles
    public static ArrayList<Battle> getBattles(){ return currbattles; }

    //Gets a battle out of the array
    public static Battle getBattle(int index){ return currbattles.get(index); }

    //Get the Battle Index
    public static int getBattleIndex(Battle b){ return currbattles.indexOf(b); }

}