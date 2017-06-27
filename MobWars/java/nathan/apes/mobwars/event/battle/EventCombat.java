package nathan.apes.mobwars.event.battle;

import nathan.apes.mobwars.battle.Battle;
import nathan.apes.mobwars.battle.Squad;
import nathan.apes.mobwars.util.BattleManager;
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
                Battle.getSquads(BattleManager.getBattleIndex(battle)).forEach(
                        squad -> {
                            if (!(Squad.getOwner(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squadInvolved)), squadInvolved)).equals(Squad.getOwner(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad))))) {
                                double distance = Math.abs(Squad.getSquadLocation(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad)).getX() - Squad.getSquadLocation(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squadInvolved)), squadInvolved)).getX());
                                if (distance < 4) {
                                    if (Squad.getInForm(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squadInvolved)), squadInvolved)) && (!(Squad.getRetreatStatus(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squadInvolved)), squadInvolved))))) {
                                        Squad.setInForm(false, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squadInvolved)), squadInvolved));
                                        Squad.getSquadPlayers(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squadInvolved)), squadInvolved)).forEach(player -> player.sendMessage(ChatColor.RED + "Your squadron is engaged! FIGHT!"));
                                        Squad.getOwner(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squadInvolved)), squadInvolved)).sendMessage(ChatColor.RED + "Your squadron is engaged!");
                                        Squad.setInForm(false, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad));
                                        Squad.getSquadPlayers(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad)).forEach(player -> player.sendMessage(ChatColor.RED + "Your squadron is engaged! FIGHT!"));
                                        Squad.getOwner(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad)).sendMessage(ChatColor.RED + "Your squadron is engaged!");
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
                Battle battle = Squad.getSquadBattle(squad);
                if(!(Squad.getInForm(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad)) && Squad.getInForm(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(otherSquad)), otherSquad))) && (!(Squad.getRetreatStatus(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad))))) {
                    Squad.setHealth(Squad.getHealth(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad)) - 0.5, BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad));
                    Squad.getSquadPlayers(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad)).forEach(squadPlayer -> squadPlayer.sendMessage(ChatColor.RED + "Your squad was damaged! [" + Squad.getHealth(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad)) + "HP]"));
                    Squad.getOwner(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad)).sendMessage(ChatColor.RED + "Your squad was damaged! [" + Squad.getHealth(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad)) + "HP]");
                    Squad.getSquadPlayers(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad)).forEach(squadPlayer -> squadPlayer.playSound(squadPlayer.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f));
                    otherPlayer.sendMessage(ChatColor.BLUE + "You inflicted damage onto the enemy squad! [" + Squad.getHealth(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), squad)) + "HP]");
                    Squad.getOwner(BattleManager.getBattleIndex(battle), Battle.getSquadIndex(BattleManager.getBattleIndex(Squad.getSquadBattle(squad)), Squad.getSquadPlayer(otherPlayer)));
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