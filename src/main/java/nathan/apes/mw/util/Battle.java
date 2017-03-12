package nathan.apes.mw.util;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.util.Vector;

public class Battle{
    
    public static Player bp1;
    public static Player bp2;
    
    public Vector battlearea;
    
    public int battleindex;
    
    public Battle(Player p1, Player p2, Vector grounds, int index){
        
        bp1 = p1;
        bp2 = p2;
        
        battlearea = grounds;
        
        battleindex = index;
        
        teleportPlayers();
        giveBattleItems();
        
    }    

    public ArrayList<Player> getPlayers(){

        ArrayList<Player> pls = new ArrayList<Player>();     
        
        pls.add(bp1);
        pls.add(bp2);
    
        return pls; 
    }

    public void teleportPlayers(){
        
        World bw = Bukkit.getWorld("mw_BattleWorld"); 
        
        double x = (double) battlearea.getX() + 100.0;
        double z = (double) battlearea.getX() + 5.0; 
        
        Block yb = bw.getHighestBlockAt( (int) x, (int) z);
        //Change to config later...
        
        double y = (double) yb.getY() + 4.0;
        
        Location p1loc = new Location(bw, x, y, z);
        
        double z2 = z + 190.0; 
        
        Block yb2 = bw.getHighestBlockAt( (int) x, (int) z2);
        double y2 = (double) yb2.getY() + 4.0;
        
        Location p2loc = new Location(bw, x, y2, z2);
        
        bp1.teleport(p1loc);
        bp2.teleport(p2loc);
        
    }
    
    public void giveBattleItems(){
        
        ItemStack bizomb = new ItemStack(Material.IRON_SPADE, 3);
        ItemStack biskel = new ItemStack(Material.BOW, 2);
        
        ItemMeta im1 = bizomb.getItemMeta();
        im1.setDisplayName("Place Zombie Squad");
        bizomb.setItemMeta(im1);
        
        ItemMeta im2 = biskel.getItemMeta();
        im2.setDisplayName("Place Skelebow Squad");
        biskel.setItemMeta(im2);
        
        bp1.getInventory().setItem(0, bizomb);
        bp2.getInventory().setItem(0, bizomb);
        
        bp1.getInventory().setItem(1, biskel);
        bp2.getInventory().setItem(1, biskel);
        
    }
    
}
