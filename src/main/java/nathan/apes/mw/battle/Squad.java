package nathan.apes.mw.battle;

import nathan.apes.mw.main.MobWars;
import nathan.apes.mw.event.battle.*;

import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Squad {
    
    public EntityType squad;
    
    public ArrayList<LivingEntity> squadentities = new ArrayList<LivingEntity>();
    
    public Player squadowner;
    
    public int squadindex = -1;
    
    public Squad(EntityType squadtype, Player owner, Location spawnloc, int index){
        
        squad = squadtype;
        
        squadowner = owner;
        
        squadindex = index;
        
        JavaPlugin mainclass = JavaPlugin.getProvidingPlugin(MobWars.class);
        
        spawnSquad(squadtype, spawnloc);
        
        openSquadGUI(owner, index);
        
        mainclass.getServer().getPluginManager().registerEvents(new EventClickFunctionGUI(), mainclass);
        
    }
    
    public void spawnSquad(EntityType spawntype, Location loc){
        
        World bw = Bukkit.getWorld("mw_BattleWorld");
        //Change to config later...
        
        Location placementloc = null;
        
        float yaw = squadowner.getLocation().getYaw();
        
        for(int i = 0; i < 5; i++){
        
            if(yaw > 0 && yaw < 90){ placementloc = new Location(bw, loc.getX() + (double) i, loc.getY(), loc.getZ()); }
            if(yaw > 90 && yaw < 180){ placementloc = new Location(bw, loc.getX(), loc.getY(), loc.getZ() + (double) i); }
            if(yaw > 180 && yaw < 270){ placementloc = new Location(bw, loc.getX() - (double) i, loc.getY(), loc.getZ()); }
            if(yaw > 270 && yaw < 360){ placementloc = new Location(bw, loc.getX(), loc.getY(), loc.getZ() - (double) i); }
            
            squadentities.add((LivingEntity) bw.spawnEntity(placementloc, spawntype));
            
            LivingEntity e = squadentities.get(i); 
            
            e.setAI(false);
            e.setSilent(true);
            
            EntityEquipment armor = e.getEquipment();
            
            if(spawntype == EntityType.ZOMBIE){
                
                armor.setHelmet(new ItemStack(Material.IRON_HELMET)); 
                armor.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                armor.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                armor.setBoots(new ItemStack(Material.IRON_BOOTS));  
                armor.setItemInMainHand(new ItemStack(Material.IRON_SPADE));
                
            }
            
            if(spawntype == EntityType.SKELETON){
                
                armor.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                armor.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                armor.setBoots(new ItemStack(Material.LEATHER_BOOTS));
                armor.setItemInMainHand(new ItemStack(Material.BOW));
                
            }
            
        }
        
    }
    
    private void openSquadGUI(Player owner, int ind){
        
        String type = "";
        
        if(ind < 4){ type = "Zombie"; }
        if(ind > 3){ type = "Skelebow"; }
        
        Inventory inv = Bukkit.createInventory(owner, InventoryType.CHEST, "MobSquadron #" + ind + " - " + type + " | Function?");
        
        ItemStack iatt = new ItemStack(Material.IRON_SWORD);
        ItemStack igrd = new ItemStack(Material.SHIELD);
        ItemStack isni = new ItemStack(Material.BOW); 
        
        ItemMeta im1 = iatt.getItemMeta();
        im1.setDisplayName("Attack");
        iatt.setItemMeta(im1);
        
        ItemMeta im2 = igrd.getItemMeta();
        im2.setDisplayName("Guard");
        igrd.setItemMeta(im2);
        
        ItemMeta im3 = isni.getItemMeta();
        im3.setDisplayName("Snipe");
        isni.setItemMeta(im3);
        
        inv.setItem(11, iatt);
        inv.setItem(13, igrd);
        inv.setItem(15, isni); 
        
        owner.openInventory(inv);
        
    }
    
    public static void setAI(int typeAI, int index, Player sqowner){
        
        
        
    }
    
}
