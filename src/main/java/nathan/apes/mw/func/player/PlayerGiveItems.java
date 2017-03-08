package nathan.apes.mw.func.player;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

public class PlayerGiveItems {
    
    public void giveBattleItems(){
        
        ItemStack bizomb = new ItemStack(Material.IRON_SPADE);
        ItemStack biskel = new ItemStack(Material.BOW);
        
        ItemMeta im1 = bizomb.getItemMeta();
        im1.setDisplayName("Place Zombie Squad");
        
        ItemMeta im2 = biskel.getItemMeta();
        im2.setDisplayName("Place Skelebow Squad");
        
    }
    
}
