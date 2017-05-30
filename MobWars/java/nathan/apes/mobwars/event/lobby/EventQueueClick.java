package nathan.apes.mobwars.event.lobby;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

import static nathan.apes.mobwars.main.MobWars.loggingPrefix;
import static nathan.apes.mobwars.util.BattleManager.battlePlayers;

//EventQueueClick: Lobby PlayerMatchMaking Event

public class EventQueueClick implements Listener{

    @EventHandler
    public void onClick(PlayerInteractEvent pie){

        //Get Clicking Player
        Player clker = pie.getPlayer();

        //Listen for click
        if(clker.getWorld().equals(Bukkit.getWorld("mw_Lobby"))){
            if(pie.getAction().equals(Action.RIGHT_CLICK_AIR)){
                if(pie.hasItem()){
                    if(pie.getItem().equals(new ItemStack(Material.NETHER_STAR))){

                        //If they are not in the List, add them
                        if(!(battlePlayers.contains(clker))){
                            battlePlayers.add(clker);
                            clker.sendMessage(loggingPrefix + ChatColor.GREEN + "You have been added to a list of players. When everyone wants some action, your game will start...");
                        }
                        //If they are, remove them
                        else {
                            battlePlayers.remove(clker);
                            clker.sendMessage(loggingPrefix + ChatColor.GOLD + "You have been removed from the list. You can come back anytime...");
                        }
                    }
                }
            }
        }
    }
}
