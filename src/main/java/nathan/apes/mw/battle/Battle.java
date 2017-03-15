package nathan.apes.mw.battle;

import nathan.apes.mw.main.MobWars;
import nathan.apes.mw.event.battle.*;
import nathan.apes.mw.util.*;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Battle{
    
    public int battlestage = -1;
    
    public Player bp1;
    public Player bp2;
    
    public Vector battlearea;
    
    public int battleindex;
    
    public ArrayList<Squad> squads = new ArrayList<Squad>();
    
    //Fix instance issue... 
    
    public Battle(Player p1, Player p2, Vector grounds, int index){
        
        bp1 = p1;
        bp2 = p2;
        
        battlearea = grounds;
        
        battleindex = index;
        
        initBattle(battleindex, battlearea);
        
    }    
    
    public void initBattle(int ind, Vector telearea){
        
        Battle b = PlayerQueue.getBattle(ind);
        
        Player p1 = Battle.getBattlePlayers(b).get(0);
        Player p2 = Battle.getBattlePlayers(b).get(1);
        
        teleportPlayers(p1, p2, telearea);
        
        p1.setAllowFlight(true);
        p2.setAllowFlight(true);
        
        p1.setInvulnerable(true);
        p2.setInvulnerable(true);
        
        p1.playSound(p1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        p2.playSound(p2.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        
        giveBattleItems(p1, p2);
        
        p1.sendMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.AQUA + "Game started! Fly around and start deploying your " + ChatColor.GOLD + "Mob Squadrons" + ChatColor.AQUA + ". Click on a block to do so.");
        p2.sendMessage("[" + ChatColor.RED + "MobWars" + ChatColor.RESET + "] " + ChatColor.AQUA + "Game started! Fly around and start deploying your " + ChatColor.GOLD + "Mob Squadrons" + ChatColor.AQUA + ". Click on a block to do so.");
        
        JavaPlugin mainclass = JavaPlugin.getProvidingPlugin(MobWars.class);
        
        //mainclass.getServer().getPluginManager().registerEvents(new EventPlayerMoveOut(), mainclass);
        //Enable when fixed...
        
        mainclass.getServer().getPluginManager().registerEvents(new EventDeploySquad(), mainclass);
        
    }

    public void initMainBattleStage(){
        
        //Start the Main Battle...
        
    }
    
    private void teleportPlayers(Player p1, Player p2, Vector area){
        
        World bw = Bukkit.getWorld("mw_BattleWorld"); 
        
        double x = (double) area.getX() + 100.0;
        double z = (double) area.getX() + 5.0; 
        
        Block yb = bw.getHighestBlockAt( (int) x, (int) z);
        //Change to config later...
        
        double y = (double) yb.getY() + 4.0;
        
        Location p1loc = new Location(bw, x, y, z);
        
        double z2 = z + 190.0; 
        
        Block yb2 = bw.getHighestBlockAt( (int) x, (int) z2);
        double y2 = (double) yb2.getY() + 4.0;
        
        Location p2loc = new Location(bw, x, y2, z2);
        
        p1.teleport(p1loc);
        p2.teleport(p2loc);
        
    }
    
    private void giveBattleItems(Player p1, Player p2){
        
        ItemStack bizomb = new ItemStack(Material.IRON_SPADE, 3);
        ItemStack biskel = new ItemStack(Material.BOW, 2);
        
        ItemMeta im1 = bizomb.getItemMeta();
        im1.setDisplayName("Place Zombie Squad");
        bizomb.setItemMeta(im1);
        
        ItemMeta im2 = biskel.getItemMeta();
        im2.setDisplayName("Place Skelebow Squad");
        biskel.setItemMeta(im2);
        
        p1.getInventory().setItem(0, bizomb);
        p2.getInventory().setItem(0, bizomb);
        
        p1.getInventory().setItem(1, biskel);
        p2.getInventory().setItem(1, biskel);
        
    }

    public static ArrayList<Player> getBattlePlayers(Battle b){

        ArrayList<Player> pls = new ArrayList<Player>();     
        
        pls.add(b.bp1);
        pls.add(b.bp2);
    
        return pls; 
    }
    
    public static Battle getPlayerBattle(Player p){
        
        Battle b = null;
        
        for(int i = 0; i < PlayerQueue.currbattles.size(); i++){
        
            if(Battle.getBattlePlayers(PlayerQueue.currbattles.get(i)).contains(p)){ b = PlayerQueue.currbattles.get(i); }
    
        }

        return b;
    }
    
    public static boolean isPlayerInBattle(Player p){
        
        boolean b = false;
        
        for(int i = 0; i < PlayerQueue.currbattles.size(); i++){
        
            if(Battle.getBattlePlayers(PlayerQueue.currbattles.get(i)).contains(p)){ b = true; }
        
            else { b = false; }
        
        }
        
        return b;
    }
    
}
