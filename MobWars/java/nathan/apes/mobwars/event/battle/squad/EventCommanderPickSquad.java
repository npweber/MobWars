package nathan.apes.mobwars.event.battle.squad;

import nathan.apes.mobwars.battle.Battle;
import nathan.apes.mobwars.battle.Squad;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import static nathan.apes.mobwars.main.MobWars.loggingPrefix;

//CommanderAction Event: Listens for the commanders selection of Squad

public class EventCommanderPickSquad implements Listener {

    public static Squad pickedSquad;

    @EventHandler
    public void onPick(PlayerInteractAtEntityEvent pie){

        //Get player
        Player commander = pie.getPlayer();

        //Specify conditions
        if(commander.getLocation().getWorld().equals(Bukkit.getWorld("mw_BattleWorld"))) {
            if (pie.getRightClicked().getType().equals(EntityType.PLAYER)) {
                if (!(pie.getPlayer().getInventory().getItemInMainHand() == null)) {
                    //If the item is the commanders get his action
                    if (pie.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("Commander's Baton")) {
                        //If he points, find the squad he picks
                        //pickedSquad = Squad.getSquadPlayer((Player) pie.getRightClicked());
                        pickedSquad = Battle.getPlayerBattle(commander).squads.get(0);
                        commander.sendMessage(loggingPrefix + ChatColor.GOLD + "You selected Squad " + (pickedSquad.squadindex + 1) + ".");
                    }
                }
            }
        }
    }
}
