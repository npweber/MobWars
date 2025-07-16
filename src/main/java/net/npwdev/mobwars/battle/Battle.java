package net.npwdev.mobwars.battle;

import net.npwdev.mobwars.main.MobWars;
import net.npwdev.mobwars.util.*;

import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static net.npwdev.mobwars.main.MobWars.bw;
import static net.npwdev.mobwars.main.MobWars.loggingPrefix;
import static net.npwdev.mobwars.main.MobWars.scheduler;

//The Battle Object: Operates all battles in the system

public class Battle{

    //Players in Battle
    private static ArrayList<ArrayList<Player>> battlePlayers = new ArrayList<>();

    //Commanders of the armies
    private static ArrayList<Player[]> opposingCommanders = new ArrayList<>();
    private static ArrayList<double[]> armysHealthBank = new ArrayList<>();

    //Squads in Battle
    private static ArrayList<ArrayList<Squad>> squads = new ArrayList<>();

    //BattleGrounds
    private static ArrayList<Vector> battlearea = new ArrayList<>();

    //Refrence to main
    private final JavaPlugin mainClass = JavaPlugin.getProvidingPlugin(MobWars.class);

    //Init Battle
    public Battle(ArrayList<Player> players, Vector grounds, int index){

        //Set variables
        battlePlayers.add(new ArrayList<>());
        opposingCommanders.add(new Player[2]);
        armysHealthBank.add(new double[] {20.0, 20.0});
        squads.add(new ArrayList<>());
        battlePlayers.get(index).addAll(players);
        players.clear();
        battlearea.add(grounds);

        //Start Battle
        initBattle(index, battlearea.get(index));
    }    

    //Init starting stage of the Battle
    private void initBattle(int index, Vector telearea) {

        //All player tasks
        getBattlePlayers(index).forEach(
            player -> {
                scheduler.scheduleSyncDelayedTask(mainClass,
                    () -> {
                        player.setInvulnerable(false);
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        player.sendMessage(loggingPrefix + ChatColor.GREEN + "Game started!");
                    }
                , 100L);
            }
        );

        //Assign commanders
        ArrayList<Player> playerChooseList = new ArrayList<>();
        playerChooseList.addAll(getBattlePlayers(index));
        opposingCommanders.get(index)[0] = playerChooseList.get(new Random().nextInt(playerChooseList.size()));
        playerChooseList.remove(opposingCommanders.get(index)[0]);
        opposingCommanders.get(index)[1] = playerChooseList.get(new Random().nextInt(playerChooseList.size()));
        playerChooseList.remove(opposingCommanders.get(index)[1]);

        //Assign commanding properties
        opposingCommanders.get(index)[0].setAllowFlight(true);
        opposingCommanders.get(index)[1].setAllowFlight(true);
        opposingCommanders.get(index)[0].setFlying(true);
        opposingCommanders.get(index)[1].setFlying(true);
        opposingCommanders.get(index)[0].setInvulnerable(true);
        opposingCommanders.get(index)[1].setInvulnerable(true);

        //Give commanding devices
        //Commanders Baton (For direction to troops)
        ItemStack commandersBaton = new ItemStack(Material.BLAZE_ROD);
        ItemMeta itemMeta2 = commandersBaton.getItemMeta();
        itemMeta2.setDisplayName("Commander's Baton");
        commandersBaton.setItemMeta(itemMeta2);

        //Commanders Picker (For Troop Selection)
        ItemStack commandersPickStick = new ItemStack(Material.STICK);
        ItemMeta itemMeta3 = commandersPickStick.getItemMeta();
        itemMeta3.setDisplayName("Commander's Picker");
        commandersPickStick.setItemMeta(itemMeta3);

        //Commanders Retreat Option (Item that gives specific orders to retreat to the given cordinates)
        ItemStack commandersRetreat = new ItemStack(Material.LEASH);
        ItemMeta itemMeta4 = commandersRetreat.getItemMeta();
        itemMeta4.setDisplayName("Commander's Retreat Option");
        commandersRetreat.setItemMeta(itemMeta4);

        //Commanders Retreat Option (Item that gives specific orders to retreat to the given cordinates)
        ItemStack commandersRegen = new ItemStack(Material.REDSTONE);
        ItemMeta itemMeta5 = commandersRegen.getItemMeta();
        itemMeta5.setDisplayName("Commander's Regen Option");
        commandersRegen.setItemMeta(itemMeta5);

        //Give devices
        ItemStack[] commandingItems = new ItemStack[]{ commandersBaton, commandersPickStick, commandersRetreat, commandersRegen };
        opposingCommanders.get(index)[0].getInventory().setContents(commandingItems);
        opposingCommanders.get(index)[0].getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        opposingCommanders.get(index)[1].getInventory().setContents(commandingItems);
        opposingCommanders.get(index)[1].getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));

        //Assign Commander Variables
        //Assign Spawn Location
        Location[] commanderLocations = new Location[]{
            new Location(bw, telearea.getX() + 5, bw.getHighestBlockYAt((int) telearea.getX() + 5,
                (int) telearea.getZ() - 50), telearea.getZ() - 50),
            new Location(bw, telearea.getX() + 95, bw.getHighestBlockYAt((int) telearea.getX() + 95,
                (int) telearea.getZ() - 50), telearea.getZ() - 50)
        };
        commanderLocations[0].setYaw(270);
        commanderLocations[1].setYaw(90);

        //Teleport Opposing Commanders In
        opposingCommanders.get(index)[0].teleport(commanderLocations[0]);
        opposingCommanders.get(index)[1].teleport(commanderLocations[1]);

        //Tell commanders a tip
        scheduler.scheduleSyncDelayedTask(mainClass,
            () -> {
                opposingCommanders.get(index)[0].sendMessage(loggingPrefix + ChatColor.AQUA + "You are Commander of Army 1!");
                opposingCommanders.get(index)[0].sendMessage(loggingPrefix + ChatColor.GOLD + "Your job is to tactically " +
                    "control your " + ChatColor.BLUE + "Mob Squadrons" + ChatColor.GOLD + " to fight the other army!"
                );
                opposingCommanders.get(index)[0].sendMessage(loggingPrefix + ChatColor.AQUA + "Use whatever means necessary to defeat the enemy!");
                opposingCommanders.get(index)[0].sendMessage(loggingPrefix + ChatColor.GOLD + "Good luck!");
                opposingCommanders.get(index)[1].sendMessage(loggingPrefix + ChatColor.AQUA + "You are Commander of Army 2!");
                opposingCommanders.get(index)[1].sendMessage(loggingPrefix + ChatColor.GOLD + "Your job is to tactically " +
                        "control your " + ChatColor.BLUE + "Mob Squadrons" + ChatColor.GOLD + " to fight the other army!"
                );
                opposingCommanders.get(index)[1].sendMessage(loggingPrefix + ChatColor.AQUA + "Use whatever means necessary to defeat the enemy!");
                opposingCommanders.get(index)[1].sendMessage(loggingPrefix + ChatColor.GOLD + "Good luck!");
            }
        , 20L);

        //Assign Squads
        ArrayList<Player>[] squadPlayerLists = new ArrayList[]{new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()};
        playerChooseList.forEach(
            player -> {
                if(playerChooseList.indexOf(player) < 2)
                    squadPlayerLists[0].add(player);
                if(playerChooseList.indexOf(player) > 1 && playerChooseList.indexOf(player) < 4)
                    squadPlayerLists[1].add(player);
                if(playerChooseList.indexOf(player) > 3 && playerChooseList.indexOf(player) < 6)
                    squadPlayerLists[2].add(player);
                if(playerChooseList.indexOf(player) > 5 && playerChooseList.indexOf(player) < 8)
                    squadPlayerLists[3].add(player);
            }
        );
        //Assign squad spawn location
        Location[] squadLocations = new Location[]{
            commanderLocations[0].subtract(1, 0, 0),
            commanderLocations[0].subtract(1, 0, 0),
            commanderLocations[1].add(1, 0, 0),
            commanderLocations[1].add(1, 0, 0)
        };
        squadLocations[0].subtract(0, 0, 4);
        squadLocations[1].add(0, 0, 4);
        squadLocations[2].subtract(0, 0, 4);
        squadLocations[3].add(0, 0, 4);
        for(int i = 0; i < squadLocations.length; i++) {
            if (i < 2)
                squadLocations[i].setYaw(270);
            else
                squadLocations[i].setYaw(90);
        }

        //Create Squads
        Player owner;
        int armySquadIndex;
        for(int i = 0; i < squadPlayerLists.length; i++) {
            if(i < 2) {
                owner = opposingCommanders.get(index)[0];
                armySquadIndex = 0;
            } else {
                owner = opposingCommanders.get(index)[1];
                armySquadIndex = 1;
            }
            squads.get(index).add(new Squad(owner, squadPlayerLists[i], squadLocations[i], index, armySquadIndex, i));
        }
    }

    //Get a certain Squad's index
    public static int getSquadIndex(int index, Squad squad){ return getSquads(index).indexOf(squad); }

    //Get which Battle the player is in
    public static Battle getPlayerBattle(Player p){
        Battle battle = null;
        for(int i = 0; i < BattleManager.getBattles().size(); i++)
            for(int i2 = 0; i2 < getBattlePlayers(i).size(); i2++)
                if(getBattlePlayers(i).contains(p))
                    battle = BattleManager.getBattle(i);
        return battle;
    }

    //Get if the player is in a Battle
    public static boolean isPlayerInBattle(Player p){
        boolean b = false;
        for(int i = 0; i < BattleManager.getBattles().size(); i++)
            for(int i2 = 0; i2 < getBattlePlayers(i).size(); i2++)
                if(getBattlePlayers(i).contains(p))
                    b = true;
        return b;
    }

    //Get certain battle's players
    public static ArrayList<Player> getBattlePlayers(int index){ return Battle.battlePlayers.get(index); }
    public static ArrayList<ArrayList<Player>> getAllBattlePlayers(){ return Battle.battlePlayers; }

    //Get certain battle's commanders
    public static Player[] getCommanders(int index){ return opposingCommanders.get(index); }
    public static ArrayList<Player[]> getAllCommanders(){ return opposingCommanders; }

    //Get Commander's Health Banked
    public static double[] getArmyHealthBank(int battleIndex){ return armysHealthBank.get(battleIndex); }
    public static void setArmyHealthBank(double healthBanked, int battleIndex, int armyIndex){
        armysHealthBank.get(battleIndex)[armyIndex] = healthBanked;
    }

    //Get certain battle's squads
    public static ArrayList<Squad> getSquads(int index){ return squads.get(index); }
    public static ArrayList<ArrayList<Squad>> getAllSquads(){ return squads; }

    //Get certain battle's combat field
    public static Vector getBattleArea(int index){ return battlearea.get(index); }
    public static ArrayList<Vector> getBattleAreas(){ return battlearea; }

}
