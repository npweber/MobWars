package nathan.apes.mw.battle;

import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public class Squad {
    
    public EntityType squad;
    
    public ArrayList<LivingEntity> squadentities = new ArrayList<LivingEntity>();
    
    public Player squadowner;
    
    public int squadindex = -1;
    
    public Squad(EntityType squadtype, Player owner, Location spawnloc, int index){
        
        squad = squadtype;
        
        squadowner = owner;
        
        squadindex = index;
        
        spawnSquad(squadtype, spawnloc);
        
        //Initialize Squad Events
        
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
    
}
