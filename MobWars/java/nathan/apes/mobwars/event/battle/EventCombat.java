package nathan.apes.mobwars.event.battle;

import nathan.apes.mobwars.battle.Battle;
import nathan.apes.mobwars.battle.Squad;
import org.bukkit.ChatColor;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.sql.SQLInput;

//EventCombat: Allows Opposing Squads to engage in Combat & Register their inflicted Casualties

public class EventCombat implements Listener{

    @EventHandler
    public void onPreparetoEngage(PlayerMoveEvent pme){

        //Get Player
        Player squadPlayer = pme.getPlayer();

        if(Squad.isPlayerInSquad(squadPlayer)) {

            //Get Squad Involved
            Squad squadInvolved = Squad.getSquadPlayer(squadPlayer);

            //Get Battle
            Battle battle = Battle.getPlayerBattle(squadPlayer);

            //Check for Squads in proximity to each other and engage them into battle if in proximity
            battle.squads.forEach(
                squad -> {
                    if (!(Squad.getOwner(Battle.getSquadIndex(squadPlayer, squadInvolved)).equals(Squad.squadowner.get(Battle.getSquadIndex(squadPlayer, squad))))) {
                        double distance = Math.abs(Squad.getSquadLocation(Battle.getSquadIndex(squadPlayer, squad)).getX() - Squad.getSquadLocation(Battle.getSquadIndex(squadPlayer, squadInvolved)).getX());
                        if (distance < 4) {
                            if(Squad.getInForm(Battle.getSquadIndex(squadPlayer, squad))) {
                                Squad.setInForm(false, Battle.getSquadIndex(squadPlayer, squadInvolved));
                                Squad.getSquadPlayers(Battle.getSquadIndex(squadPlayer, squadInvolved)).forEach(player -> player.sendMessage(ChatColor.RED + "Your squadron is engaged! FIGHT!"));
                                Squad.setInForm(false, Battle.getSquadIndex(squadPlayer, squad));
                                Squad.getSquadPlayers(Battle.getSquadIndex(squadPlayer, squad)).forEach(player -> player.sendMessage(ChatColor.RED + "Your squadron is engaged! FIGHT!"));
                            }
                        }
                    }
                }
            );
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent damageEvent) {

        if (damageEvent.getEntity().getType().equals(EntityType.PLAYER)){

            //Get players
            Player player = (Player) damageEvent.getEntity();
            Player otherPlayer = (Player) damageEvent.getDamager();

            //If the player is damaged in battle, God-mode the player and reduce Squad Health
            if (Squad.isPlayerInSquad(player) && Squad.isPlayerInSquad(otherPlayer)) {
                damageEvent.setCancelled(true);
                Squad squad = Squad.getSquadPlayer(player);
                Squad otherSquad = Squad.getSquadPlayer(otherPlayer);
                if(!(Squad.getInForm(Battle.getSquadIndex(player, squad)) && Squad.getInForm(Battle.getSquadIndex(otherPlayer, otherSquad)))) {
                    Squad.setHealth(Squad.getHealth(Battle.getSquadIndex(player, squad)) - 0.5, Battle.getSquadIndex(player, squad));
                    Squad.getSquadPlayers(Battle.getSquadIndex(player, squad)).forEach(squadPlayer -> squadPlayer.sendMessage(ChatColor.RED + "Your squad was damaged! [" + Squad.getHealth(Battle.getSquadIndex(player, squad)) + "HP]"));
                    otherPlayer.sendMessage(ChatColor.BLUE + "You inflicted damage onto the enemy squad! [" + Squad.getHealth(Battle.getSquadIndex(player, squad)) + "HP]");
                }
            }
        }
    }
}
