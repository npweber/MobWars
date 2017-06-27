package nathan.apes.mobwars.battle;

import java.util.*;

import nathan.apes.mobwars.util.BattleManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import static nathan.apes.mobwars.main.MobWars.loggingPrefix;

//The Squad Object: Handles all Squads in all Battles

public class Squad {

    //Players in Squad
    private static ArrayList<ArrayList<ArrayList<Player>>> squadPlayers = new ArrayList<>();

    //Owner of Squad
    private static ArrayList<ArrayList<Player>> squadowner = new ArrayList<>();

    //Location
    private static ArrayList<ArrayList<Location>> squadLocation = new ArrayList<>();

    //Movement Control
    private static ArrayList<ArrayList<Boolean>> isInForm = new ArrayList<>();
    private static ArrayList<ArrayList<Location>> targetDestination = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> xSteps = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> zSteps = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> xCurrStep = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> zCurrStep = new ArrayList<>();

    //Health control
    private static ArrayList<ArrayList<Double>> health = new ArrayList<>();

    //Retreat control
    private static ArrayList<ArrayList<Boolean>> retreat = new ArrayList<>();

    //Init Squad
    public Squad(Player owner, ArrayList<Player> localSquadPlayers, Location spawnloc, int battleIndex, int squadIndex){

        //Set variables
        squadPlayers.add(new ArrayList<>());
        squadPlayers.get(battleIndex).add(localSquadPlayers);
        squadowner.add(new ArrayList<>());
        squadowner.get(battleIndex).add(owner);
        squadLocation.add(new ArrayList<>());
        squadLocation.get(battleIndex).add(squadPlayers.get(battleIndex).get(0).get(0).getLocation());
        targetDestination.add(new ArrayList<>());
        targetDestination.get(battleIndex).add(null);
        xSteps.add(new ArrayList<>());
        xSteps.get(battleIndex).add(0.0);
        xCurrStep.add(new ArrayList<>());
        xCurrStep.get(battleIndex).add(0.0);
        zSteps.add(new ArrayList<>());
        zSteps.get(battleIndex).add(0.0);
        zCurrStep.add(new ArrayList<>());
        zCurrStep.get(battleIndex).add(0.0);
        isInForm.add(new ArrayList<>());
        isInForm.get(battleIndex).add(true);
        retreat.add(new ArrayList<>());
        retreat.get(battleIndex).add(false);
        health.add(new ArrayList<>());
        health.get(battleIndex).add(60.0);

        //Spawn it in
        spawnSquad(squadPlayers.get(battleIndex).get(squadIndex), spawnloc, squadIndex);

        //Give the Squad Players a tip
        squadPlayers.get(battleIndex).get(squadIndex).forEach(player -> player.sendMessage(loggingPrefix + ChatColor.AQUA + "You are in a Squad under commander " + ChatColor.RESET + "[" + ChatColor.GOLD + owner.getPlayerListName() + ChatColor.RESET + "[" + ChatColor.GREEN + (squadIndex + 1) + ChatColor.RESET + "]" + ChatColor.RESET + "]" + ChatColor.AQUA + " May luck be on your side..."));
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
    public void move(int battleIndex, int squadIndex){
        if(!(targetDestination.isEmpty())){
            if(!(targetDestination.get(battleIndex).get(squadIndex) == null)) {
                Location currLocation;
                Location newLocation;
                if (xCurrStep.get(battleIndex).get(squadIndex) < xSteps.get(battleIndex).get(squadIndex)) {
                    for (int i = 0; i < getSquadPlayers(battleIndex, squadIndex).size(); i++) {
                        currLocation = getSquadPlayers(battleIndex, squadIndex).get(i).getLocation();
                        if ((targetDestination.get(battleIndex).get(squadIndex).getX() - squadLocation.get(battleIndex).get(squadIndex).getX()) > 0){
                            newLocation = currLocation.add(1, 0, 0);
                            newLocation.setYaw(270);
                        }
                        else {
                            newLocation = currLocation.subtract(1, 0, 0);
                            newLocation.setYaw(90);
                        }
                        newLocation.setY(currLocation.getWorld().getHighestBlockYAt((int) newLocation.getX(), (int) newLocation.getZ()));
                        getSquadPlayers(battleIndex, squadIndex).get(i).teleport(newLocation);
                        xCurrStep.get(battleIndex).set(squadIndex, xCurrStep.get(battleIndex).get(squadIndex) + 1);
                    }
                }
                if (zCurrStep.get(battleIndex).get(squadIndex) < zSteps.get(battleIndex).get(squadIndex)) {
                    for (int i = 0; i < getSquadPlayers(battleIndex, squadIndex).size(); i++) {
                        currLocation = getSquadPlayers(battleIndex, squadIndex).get(i).getLocation();
                        if((targetDestination.get(battleIndex).get(squadIndex).getZ() - squadLocation.get(battleIndex).get(squadIndex).getZ()) > 0) {
                            newLocation = currLocation.add(0, 0, 1);
                            newLocation.setYaw(0);
                        }
                        else {
                            newLocation = currLocation.subtract(0, 0, 1);
                            newLocation.setYaw(180);
                        }
                        newLocation.setY(currLocation.getWorld().getHighestBlockYAt((int) newLocation.getX(), (int) newLocation.getZ()));
                        getSquadPlayers(battleIndex, squadIndex).get(i).teleport(newLocation);
                        zCurrStep.get(battleIndex).set(squadIndex, zCurrStep.get(battleIndex).get(squadIndex) + 1);
                    }
                }
                squadLocation.get(battleIndex).set(squadIndex, squadPlayers.get(battleIndex).get(squadIndex).get(0).getLocation());
                if (xCurrStep.get(battleIndex).get(squadIndex).equals(xSteps.get(battleIndex).get(squadIndex)) && zCurrStep.get(battleIndex).get(squadIndex).equals(zSteps.get(battleIndex).get(squadIndex))) {
                    setDestination(null, battleIndex, squadIndex);
                    xCurrStep.get(battleIndex).set(squadIndex, 0.0);
                    zCurrStep.get(battleIndex).set(squadIndex, 0.0);
                    xSteps.get(battleIndex).set(squadIndex, 0.0);
                    zSteps.get(battleIndex).set(squadIndex, 0.0);
                    setInForm(true, battleIndex, squadIndex);
                }
            }
        }
    }
    //Sets the marching route cordinates
    public static void marchTo(Location destination, int battleIndex, int squadIndex){
        setInForm(true, battleIndex, squadIndex);
        targetDestination.get(battleIndex).set(squadIndex, destination);
        xSteps.get(battleIndex).set(squadIndex, Math.abs(targetDestination.get(battleIndex).get(squadIndex).getX() - squadLocation.get(battleIndex).get(squadIndex).getX()));
        zSteps.get(battleIndex).set(squadIndex, Math.abs(targetDestination.get(battleIndex).get(squadIndex).getZ() - squadLocation.get(battleIndex).get(squadIndex).getZ()));
        xCurrStep.get(battleIndex).set(squadIndex, 0.0);
        zCurrStep.get(battleIndex).set(squadIndex, 0.0);
    }
    //Halts all movement of the squad
    public static void halt(int battleIndex, int squadIndex){
        targetDestination.get(battleIndex).set(squadIndex, null);
        xSteps.get(battleIndex).set(squadIndex, 0.0);
        zSteps.get(battleIndex).set(squadIndex, 0.0);
        xCurrStep.get(battleIndex).set(squadIndex, 0.0);
        zCurrStep.get(battleIndex).set(squadIndex, 0.0);
    }

    //Get the Squad from certain values
    public static Squad getSquadPlayer(Player player){
        Squad squad = null;
        for(int i = 0; i < BattleManager.getBattles().size(); i++)
            for(int i2 = 0; i2 < Battle.getSquads(i).size(); i2++)
                if(getSquadPlayers(i, i2).contains(player))
                    squad = Battle.getSquads(i).get(i2);
        return squad;
    }

    //Get which Battle the Squad is in
    public static Battle getSquadBattle(Squad s){
        Battle battle = BattleManager.getBattles().get(0);
        for(int i = 0; i < BattleManager.getBattles().size(); i++)
            for(int i2 = 0; i2 < Battle.getSquads(BattleManager.getBattleIndex(BattleManager.getBattle(i))).size(); i2++)
                if(Battle.getSquads(BattleManager.getBattleIndex(BattleManager.getBattle(i))).get(i2).equals(s))
                    battle = BattleManager.getBattle(i);
        return battle;
    }

    //Get if the player is in a Battle
    public static boolean isPlayerInSquad(Player p) {
        boolean b = false;
        for(int i = 0; i < BattleManager.getBattles().size(); i++)
            for (int i2 = 0; i2 < Battle.getSquads(i).size(); i2++) {
                if (Squad.getSquadPlayers(i, i2).contains(p))
                    b = true;
            }
        return b;
    }

    //Get & Set if in formation
    public static boolean getInForm(int battleIndex, int squadIndex){
        return isInForm.get(battleIndex).get(squadIndex);
    }
    public static void setInForm(boolean b, int battleIndex, int squadIndex){
        isInForm.get(battleIndex).set(squadIndex, b);
    }

    //Get & Set Health
    public static double getHealth(int battleIndex, int squadIndex){
        return health.get(battleIndex).get(squadIndex);
    }
    public static void setHealth(double setAmount, int battleIndex, int squadIndex){
        health.get(battleIndex).set(squadIndex, setAmount);
    }

    //Get & Set Target Destination
    public static Location getDestination(int battleIndex, int squadIndex){
        return targetDestination.get(battleIndex).get(squadIndex);
    }
    public static void setDestination(Location location, int battleIndex, int squadIndex){
        targetDestination.get(battleIndex).set(squadIndex, location);
    }

    //Get & Set Location
    public static Location getSquadLocation(int battleIndex, int squadIndex){
        return squadLocation.get(battleIndex).get(squadIndex);
    }
    public static void setSquadLocation(Location location, int battleIndex, int squadIndex){
        squadLocation.get(battleIndex).set(squadIndex, location);
    }

    //Get Owner
    public static Player getOwner(int battleIndex, int squadIndex){
        return squadowner.get(battleIndex).get(squadIndex);
    }

    //Get & Set retreat status
    public static boolean getRetreatStatus(int battleIndex, int squadIndex){
        return retreat.get(battleIndex).get(squadIndex);
    }
    public static void setRetreatStatus(boolean b, int battleIndex, int squadIndex){
        retreat.get(battleIndex).set(squadIndex, b);
    }

    //Get Squad Players
    public static ArrayList<Player> getSquadPlayers(int battleIndex, int squadIndex){
        return squadPlayers.get(battleIndex).get(squadIndex);
    }
}
