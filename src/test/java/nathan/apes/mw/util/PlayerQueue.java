package nathan.apes.mw.util;

import nathan.apes.mw.main.MobWars;
import nathan.apes.mw.func.world.battle.*;

import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.*;

public class PlayerQueue {
    
    public static ArrayList<Player> players = new ArrayList<Player>();
    
    public ArrayList<Player> playerpair = new ArrayList<Player>();
    
    public ArrayList<Battle> currbattles = new ArrayList<Battle>();
    
    public int battleind = -1;
    
    public PlayerQueue(){
        
        BukkitScheduler scheduler = Bukkit.getScheduler();
        
        JavaPlugin mainclass = JavaPlugin.getProvidingPlugin(MobWars.class);
        
        scheduler.scheduleSyncRepeatingTask(mainclass, new Runnable(){
            
            public void run(){
                
                matchPlayer();
                
            }
            
        }, 0L, 5L);    
        
    }
    
    public void matchPlayer(){
        
        if(players.size() > 0 && playerpair.size() < 2){
            
            playerpair.add(players.get(0));
            
            players.remove(players.get(0));
            
        }
        
        /*
        
        if(playerpair.size() == 2){
            
            Player p1 = playerpair.get(0);
            Player p2 = playerpair.get(1);
            
            battleind++;
            
            currbattles.add(new Battle(p1, p2, new FindBattleground().findBattleground(), battleind));
            
            playerpair.get(0).sendMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.GREEN + "You have an opponent! " + ChatColor.RESET + "(" + ChatColor.GOLD + playerpair.get(1).getPlayerListName() + ChatColor.RESET + ")" + ChatColor.GREEN + " It's now time for the battle!");
            playerpair.get(1).sendMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.GREEN + "You have an opponent! " + ChatColor.RESET + "(" + ChatColor.GOLD + playerpair.get(0).getPlayerListName() + ChatColor.RESET + ")" + ChatColor.GREEN + " It's now time for the battle!");
            
        }
        
        */
           
    }
    
}
