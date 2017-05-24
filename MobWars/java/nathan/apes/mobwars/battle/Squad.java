package nathan.apes.mobwars.battle;

import java.util.*;

import nathan.apes.mobwars.main.MobWars;
import nathan.apes.mobwars.util.BattleManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static nathan.apes.mobwars.main.MobWars.scheduler;

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
    private double xSteps = 0;
    private double zSteps = 0;
    private int xCurrStep = 0;
    private int zCurrStep = 0;

    //Health control
    private static double health;

    //Refrence to main
    private JavaPlugin mainClass = JavaPlugin.getProvidingPlugin(MobWars.class);

    //Init Squad
    public Squad(Player owner, ArrayList<Player> squadPlayers, Location spawnloc, int index){

        //Set variables
        this.squadPlayers = squadPlayers;
        squadowner = owner;
        squadindex = index;
        squadLocation = this.squadPlayers.get(0).getLocation();
        targetDestination = null;
        health = 20.0;

        //Spawn it in
        spawnSquad(this.squadPlayers, spawnloc, squadindex);

        //Movement & Health Controller
        scheduler.scheduleSyncRepeatingTask(mainClass,
            () -> {
                move();
                squadPlayers.forEach(player -> player.setCustomName("Squad " + (squadindex + 1) + "[" + getHealth() + "HP]"));
            }
        , 0L, 40L);

        //Register Squad Events
        //(Fill later)
    }

    //Teleport the players to their Squad, spawning the Squad
    private void spawnSquad(ArrayList<Player> players, Location loc, int index){

        //Location variables
        Location newLoc;
        World bw = Bukkit.getWorld("mw_BattleWorld");
        //Change to config later...

        //Equipment
        ItemStack[] squadEquipment = new ItemStack[]{ new ItemStack(Material.IRON_SWORD) };

        for(int i = 0; i < players.size(); i++) {

            //Teleport the players to their ranks
            if (index == 1 || index == 3)
                newLoc = loc.subtract(0, 0, i);
            else
                newLoc = loc.add(0, 0, i);
            newLoc.setY(bw.getHighestBlockYAt((int)newLoc.getX(), (int)newLoc.getZ()));
            newLoc.setWorld(bw);
            newLoc.setYaw(270);
            players.get(i).teleport(newLoc);

            //Set their equipment
            players.get(i).getInventory().setContents(squadEquipment);
            players.get(i).getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
            players.get(i).getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            players.get(i).getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
            players.get(i).getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));

            //Display their identity
            players.get(i).setCustomNameVisible(true);
        }
    }

    //Movement Functions
    //Moves the squad
    private void move(){
        if(!(targetDestination == null)) {
            Location currLocation;
            Location newLocation;
            setInForm(false);
            squadPlayers.get(0).sendMessage("Test: " + xSteps);
            if (xCurrStep < xSteps) {
                for (int i = 0; i < this.squadPlayers.size(); i++) {
                    currLocation = this.squadPlayers.get(i).getLocation();
                    newLocation = currLocation.add(1, 0, 0);
                    newLocation.setY(currLocation.getWorld().getHighestBlockYAt((int) newLocation.getX(), (int) newLocation.getZ()));
                    newLocation.setYaw(270);
                    this.squadPlayers.get(i).teleport(newLocation);
                    xCurrStep++;
                }
            } else if(zCurrStep < zSteps && xCurrStep == xSteps){
                for (int i = 0; i < this.squadPlayers.size(); i++) {
                    currLocation = this.squadPlayers.get(i).getLocation();
                    newLocation = currLocation.add(0, 0, 1);
                    newLocation.setY(currLocation.getWorld().getHighestBlockYAt((int) newLocation.getX(), (int) newLocation.getZ()));
                    newLocation.setYaw(270);
                    this.squadPlayers.get(i).teleport(newLocation);
                    zCurrStep++;
                }
            }
            squadLocation = squadPlayers.get(0).getLocation();
            if(xCurrStep == xSteps && zCurrStep == zSteps) {
                targetDestination = null;
                xCurrStep = 0;
                zCurrStep = 0;
                xSteps = 0;
                zSteps = 0;
                setInForm(true);
            }
        }
    }
    //Sets the marching route cordinates
    public void marchTo(Location destination){
        targetDestination = destination;
        xSteps = targetDestination.getX() - squadLocation.getX();
        zSteps = targetDestination.getZ() - squadLocation.getZ();
    }
    //Halts all movement of the squad
    public void halt(){
        targetDestination = null;
        xSteps = 0;
        zSteps = 0;
        xCurrStep = 0;
        zCurrStep = 0;
    }

    //Get the Squad from certain values
    public static Squad getSquadPlayer(Player player){
        Squad squad = null;
        for(int i = 0; i < Battle.getPlayerBattle(player).squads.size(); i++)
            if(Battle.getPlayerBattle(player).squads.get(i).squadPlayers.contains(player))
                squad = Battle.getPlayerBattle(player).squads.get(i);
        return squad;
    }

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

    //Get & Set Health
    public double getHealth(){ return health; }
    public void setHealth(double setAmount){ health = setAmount; }
}
