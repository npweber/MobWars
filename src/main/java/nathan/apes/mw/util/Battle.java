package nathan.apes.mw.util;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.util.Vector;

public class Battle{
    
    public static Player p1;
    public static Player p2;
    
    public Vector battlearea;
    
    public Battle(Player p1, Player p2, Vector grounds, int index){
        
        
        
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
        
        p1.getInventory().setItem(0, bizomb);
        p2.getInventory().setItem(0, bizomb);
        
        p1.getInventory().setItem(0, biskel);
        p2.getInventory().setItem(0, biskel);
        
    }
    
}
