package nathan.apes.mobwars.battle;

import java.util.*;

import nathan.apes.mobwars.util.BattleManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import static nathan.apes.mobwars.main.MobWars.mainClass;

//The Squad Object: Handles all Squads in all Battles

public class Squad {

    //Players in Squad
    public ArrayList<Player> squadPlayers = new ArrayList<>();

    //Owner of Squad
    public Player squadowner;

    //Location
    private Location squadLocation;

    //Index
    public int squadindex = -1;

    //Movement Control
    private static boolean isInForm = true;
    private Location targetDestination;
    private boolean behindZ = false;

    //Init Squad
    public Squad(Player owner, ArrayList<Player> squadPlayers, Location spawnloc, int index){

        this.squadPlayers = squadPlayers;
        squadowner = owner;
        squadindex = index;
        squadLocation = this.squadPlayers.get(0).getLocation();
        BukkitScheduler scheduler = Bukkit.getScheduler();

        //Spawn it in
        spawnSquad(this.squadPlayers, spawnloc, squadindex);

        //Movement Controller
        scheduler.scheduleSyncRepeatingTask(mainClass, () -> move(), 0L, 5L);
    }

    //Teleport the players to their Squad, spawning the Squad
    private void spawnSquad(ArrayList<Player> players, Location loc, int index){
        ItemStack[] squadEquipment = new ItemStack[]{ new ItemStack(Material.BOW), new ItemStack(Material.IRON_SWORD) };
        ItemStack[] squadArmor = new ItemStack[]{
            new ItemStack(Material.IRON_HELMET),
            new ItemStack(Material.IRON_CHESTPLATE),
            new ItemStack(Material.IRON_LEGGINGS),
            new ItemStack(Material.IRON_BOOTS)
        };
        for(int i = 0; i < players.size(); i++) {
            if (index == 1 || index == 3)
                players.get(i).teleport(loc.subtract(i, 0, 0));
            else
                players.get(i).teleport(loc.add(i, 0, 0));
            players.get(i).getInventory().setContents(squadEquipment);
            players.get(i).getInventory().setArmorContents(squadArmor);
        }
    }

    //Movement Functions
    private void move(){
        Location currLocation;
        Location newLocation;
        if(squadLocation.getZ() < targetDestination.getZ()){
            for(int i = 0; i < this.squadPlayers.size(); i++) {
                currLocation = this.squadPlayers.get(i).getLocation();
                if(currLocation.getX() > 0)
                    newLocation = currLocation.add(0.25, 0, 0);
                else
                    newLocation = currLocation.subtract(0.25, 0, 0);
                newLocation.setY(currLocation.getWorld().getHighestBlockYAt((int) currLocation.getX(), (int) currLocation.getZ()));
                this.squadPlayers.get(i).teleport(newLocation);
            }
        }
        else {
            for(int i = 0; i < this.squadPlayers.size(); i++) {
                currLocation = this.squadPlayers.get(i).getLocation();
                if(currLocation.getX() > 0)
                    newLocation = currLocation.add(0.25, 0, 0);
                else
                    newLocation = currLocation.subtract(0.25, 0, 0);
                newLocation.setY(currLocation.getWorld().getHighestBlockYAt((int) currLocation.getX(), (int) currLocation.getZ()));
                this.squadPlayers.get(i).teleport(newLocation);
            }
        }
        squadLocation = this.squadPlayers.get(0).getLocation();
        if(squadLocation.equals(targetDestination))
            targetDestination = null;
    }
    public void marchTo(Location destination){ targetDestination = destination; }
    public void halt(){ targetDestination = null; }

    //Get the Squad from certain values
    public static Squad getSquadPlayer(Player owner, int index){ return Battle.getPlayerBattle(owner).squads.get(index); }

    //Get which Battle the Squad is in
    public static Battle getSquadBattle(Squad s){
        Battle battle = BattleManager.currbattles.get(0);
        for(int i = 0; i < BattleManager.currbattles.size(); i++)
            for(int i2 = 0; i2 < BattleManager.getBattle(i).squads.size(); i2++)
                if(BattleManager.getBattle(i).squads.get(i2).equals(s))
                    battle = BattleManager.getBattle(i);
        return battle;
    }

    //Get if the player is in a Battle
    public static boolean isPlayerInSquad(Player p) {
        boolean b = false;
        for (int i = 0; i < Battle.getPlayerBattle(p).squads.size(); i++) {
            if (Battle.getPlayerBattle(p).squads.get(i).squadPlayers.contains(p))
                b = true;
            else
                b = false;
        }
        return b;
    }

    //Get & Set if in formation
    public boolean getInForm(){ return isInForm; }
    public void setInForm(boolean b){ isInForm = b; }
}
