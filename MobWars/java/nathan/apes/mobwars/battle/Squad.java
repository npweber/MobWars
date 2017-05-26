package nathan.apes.mobwars.battle;

import java.util.*;

import nathan.apes.mobwars.event.battle.EventCombat;
import nathan.apes.mobwars.main.MobWars;
import nathan.apes.mobwars.util.BattleManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

//The Squad Object: Handles all Squads in all Battles

public class Squad {

    //Players in Squad
    public static ArrayList<ArrayList<Player>> squadPlayers = new ArrayList<>();

    //Owner of Squad
    public static ArrayList<Player> squadowner = new ArrayList<>();

    //Location
    public static ArrayList<Location> squadLocation = new ArrayList<>();

    //Movement Control
    private static ArrayList<Boolean> isInForm = new ArrayList<>();
    private static ArrayList<Location> targetDestination = new ArrayList<>();
    private ArrayList<Double> xSteps = new ArrayList<>();
    private ArrayList<Double> zSteps = new ArrayList<>();
    private ArrayList<Integer> xCurrStep = new ArrayList<>();
    private ArrayList<Integer> zCurrStep = new ArrayList<>();

    //Health control
    private static ArrayList<Double> health = new ArrayList<>();

    //Refrence to main
    private JavaPlugin mainClass = JavaPlugin.getProvidingPlugin(MobWars.class);

    //Init Squad
    public Squad(Player owner, ArrayList<Player> localSquadPlayers, Location spawnloc, int index){

        //Set variables
        squadPlayers.add(localSquadPlayers);
        squadowner.add(owner);
        squadLocation.add(squadPlayers.get(index).get(0).getLocation());
        targetDestination.add(null);
        isInForm.add(true);
        health.add(20.0);

        //Spawn it in
        spawnSquad(squadPlayers.get(index), spawnloc, index);
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
        }
    }

    //Movement Functions
    //Moves the squad
    public void move(int index){
        if(!(targetDestination.isEmpty())){
            if(!(targetDestination.get(index) == null)) {
                Location currLocation;
                Location newLocation;
                if (xCurrStep.get(index) < xSteps.get(index)) {
                    for (int i = 0; i < getSquadPlayers(index).size(); i++) {
                        currLocation = getSquadPlayers(index).get(i).getLocation();
                        newLocation = currLocation.add(1, 0, 0);
                        newLocation.setY(currLocation.getWorld().getHighestBlockYAt((int) newLocation.getX(), (int) newLocation.getZ()));
                        newLocation.setYaw(270);
                        getSquadPlayers(index).get(i).teleport(newLocation);
                        xCurrStep.set(index, xCurrStep.get(index) + 1);
                    }
                } else if (zCurrStep.get(index) < zSteps.get(index) && xCurrStep.get(index).equals(xSteps.get(index))) {
                    for (int i = 0; i < getSquadPlayers(index).size(); i++) {
                        currLocation = getSquadPlayers(index).get(i).getLocation();
                        newLocation = currLocation.add(0, 0, 1);
                        newLocation.setY(currLocation.getWorld().getHighestBlockYAt((int) newLocation.getX(), (int) newLocation.getZ()));
                        newLocation.setYaw(270);
                        getSquadPlayers(index).get(i).teleport(newLocation);
                        zCurrStep.set(index, zCurrStep.get(index) + 1);
                    }
                }
                squadLocation.set(index, squadPlayers.get(index).get(0).getLocation());
                if (xCurrStep.get(index).equals(xSteps.get(index)) && zCurrStep.get(index).equals(zSteps.get(index))) {
                    setDestination(null, index);
                    xCurrStep.set(index, 0);
                    zCurrStep.set(index, 0);
                    xSteps.set(index, 0.0);
                    zSteps.set(index, 0.0);
                    setInForm(true, index);
                }
            }
        }
    }
    //Sets the marching route cordinates
    public void marchTo(Location destination, int index){
        setInForm(true, index);
        targetDestination.set(index, destination);
        xSteps.set(index, targetDestination.get(index).getX() - squadLocation.get(index).getX());
        zSteps.set(index, targetDestination.get(index).getZ() - squadLocation.get(index).getZ());
    }
    //Halts all movement of the squad
    public void halt(int index){
        targetDestination.set(index, null);
        xSteps.set(index, 0.0);
        zSteps.set(index, 0.0);
        xCurrStep.set(index, 0);
        zCurrStep.set(index, 0);
    }

    //Get the Squad from certain values
    public static Squad getSquadPlayer(Player player){
        Squad squad = null;
        for(int i = 0; i < Battle.getPlayerBattle(player).squads.size(); i++)
            if(Battle.getPlayerBattle(player).squads.get(i).squadPlayers.get(i).contains(player))
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
            if (getSquadPlayers(Battle.getSquadIndex(p, Squad.getSquadPlayer(p))).contains(p))
                b = true;
            else
                b = false;
        }
        return b;
    }

    //Get & Set if in formation
    public static boolean getInForm(int index){ return isInForm.get(index); }
    public static void setInForm(boolean b, int index){ isInForm.set(index, b); }

    //Get & Set Health
    public static double getHealth(int index){ return health.get(index); }
    public static void setHealth(double setAmount, int index){ health.set(index, setAmount); }

    //Get & Set Target Destination
    public static Location getDestination(int index){ return targetDestination.get(index); }
    public static void setDestination(Location location, int index){ targetDestination.set(index, location); }

    //Get Location
    public static Location getSquadLocation(int index){ return squadLocation.get(index); }
    public static void setSquadLocation(Location location, int index){ squadLocation.set(index, location); }

    //Get Owner
    public static Player getOwner(int index){ return squadowner.get(index); }

    //Get Squad Players
    public static ArrayList<Player> getSquadPlayers(int index){ return squadPlayers.get(index); }
}
