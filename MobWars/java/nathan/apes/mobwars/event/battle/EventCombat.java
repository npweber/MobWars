package nathan.apes.mobwars.event.battle;

import nathan.apes.mobwars.battle.Battle;
import nathan.apes.mobwars.battle.Squad;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

//EventCombat: Allows Opposing Squads to engage in Combat & Register their inflicted Casualties

public class EventCombat implements Listener{

    //Put the Squads in proximity in Combat
    @EventHandler
    public void onPreparetoEngage(PlayerMoveEvent pme){

        //Get Player
        Player squadPlayer = pme.getPlayer();

        if(Battle.isPlayerInBattle(squadPlayer)) {
            if (Squad.isPlayerInSquad(squadPlayer)) {

                //Get Squad Involved
                Squad squadInvolved = Squad.getSquadPlayer(squadPlayer);

                //Get Battle
                Battle battle = Battle.getPlayerBattle(squadPlayer);

                //Check for Squads in proximity to each other and engage them into battle if in proximity
                battle.squads.forEach(
                        squad -> {
                            if (!(Squad.getOwner(Battle.getSquadIndex(0, squadInvolved)).equals(Squad.getOwner(Battle.getSquadIndex(0, squad))))) {
                                double distance = Math.abs(Squad.getSquadLocation(Battle.getSquadIndex(0, squad)).getX() - Squad.getSquadLocation(Battle.getSquadIndex(0, squadInvolved)).getX());
                                if (distance < 4) {
                                    if (Squad.getInForm(Battle.getSquadIndex(0, squadInvolved)) && (!(Squad.getRetreatStatus(Battle.getSquadIndex(0, squadInvolved))))) {
                                        Squad.setInForm(false, Battle.getSquadIndex(0, squadInvolved));
                                        Squad.getSquadPlayers(Battle.getSquadIndex(0, squadInvolved)).forEach(player -> player.sendMessage(ChatColor.RED + "Your squadron is engaged! FIGHT!"));
                                        Squad.getOwner(Battle.getSquadIndex(0, squadInvolved)).sendMessage(ChatColor.RED + "Your squadron is engaged!");
                                        Squad.setInForm(false, Battle.getSquadIndex(0, squad));
                                        Squad.getSquadPlayers(Battle.getSquadIndex(0, squad)).forEach(player -> player.sendMessage(ChatColor.RED + "Your squadron is engaged! FIGHT!"));
                                        Squad.getOwner(Battle.getSquadIndex(0, squad)).sendMessage(ChatColor.RED + "Your squadron is engaged!");
                                    }
                                }
                            }
                        }
                );
            }
        }
    }

    //Register the damage dealt between Squads
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent damageEvent) {
        if (damageEvent.getEntity().getType().equals(EntityType.PLAYER)){
            //Get players
            Player player = (Player) damageEvent.getEntity();
            Player otherPlayer = (Player) damageEvent.getDamager();

            //If the player is damaged in battle, God-mode the player and reduce Squad Health
            if(Squad.isPlayerInSquad(player) && Squad.isPlayerInSquad(otherPlayer)){
                Squad squad = Squad.getSquadPlayer(player);
                Squad otherSquad = Squad.getSquadPlayer(otherPlayer);
                if(!(Squad.getInForm(Battle.getSquadIndex(0, squad)) && Squad.getInForm(Battle.getSquadIndex(0, otherSquad))) && (!(Squad.getRetreatStatus(Battle.getSquadIndex(0, squad))))) {
                    Squad.setHealth(Squad.getHealth(Battle.getSquadIndex(0, squad)) - 0.5, Battle.getSquadIndex(0, squad));
                    Squad.getSquadPlayers(Battle.getSquadIndex(0, squad)).forEach(squadPlayer -> squadPlayer.sendMessage(ChatColor.RED + "Your squad was damaged! [" + Squad.getHealth(Battle.getSquadIndex(0, squad)) + "HP]"));
                    Squad.getOwner(Battle.getSquadIndex(0, squad)).sendMessage(ChatColor.RED + "Your squad was damaged! [" + Squad.getHealth(Battle.getSquadIndex(0, squad)) + "HP]");
                    Squad.getSquadPlayers(Battle.getSquadIndex(0, squad)).forEach(squadPlayer -> squadPlayer.playSound(squadPlayer.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f));
                    otherPlayer.sendMessage(ChatColor.BLUE + "You inflicted damage onto the enemy squad! [" + Squad.getHealth(Battle.getSquadIndex(0, squad)) + "HP]");
                    Squad.getOwner(Battle.getSquadIndex(0, Squad.getSquadPlayer(otherPlayer)));
                    otherPlayer.playSound(otherPlayer.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);
                }
                damageEvent.setCancelled(true);
            } else damageEvent.setCancelled(true);
        } else damageEvent.setCancelled(true);
    }

    //Cancel all non-wanted damage
    @EventHandler
    public void onUnexpectedDamage(EntityDamageEvent noDamageEvent){
        if(noDamageEvent.getEntity().getType().equals(EntityType.PLAYER))
            if(!(noDamageEvent.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)))
                noDamageEvent.setCancelled(true);
    }

}
