package nathan.apes.mobwars.battle;

import nathan.apes.mobwars.event.battle.EventCombat;
import nathan.apes.mobwars.event.battle.EventCommanderAction;
import nathan.apes.mobwars.event.battle.EventPlayerMoveOut;
import nathan.apes.mobwars.main.MobWars;
import nathan.apes.mobwars.util.*;

import java.util.*;

import nathan.apes.mobwars.world.battle.FindBattleground;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static nathan.apes.mobwars.main.MobWars.loggingPrefix;
import static nathan.apes.mobwars.main.MobWars.scheduler;
import static nathan.apes.mobwars.util.BattleManager.currbattles;

//The Battle Object: Operates all battles in the system

public class Battle{

    //BattleIndex
    public int battleindex;

    //Players in Battle
    private final ArrayList<Player> battlePlayers = new ArrayList<>();

    //Commanders of the armies
    public static Player[] opposingCommanders = new Player[2];

    //Squads in Battle
    public final ArrayList<Squad> squads = new ArrayList<>();

    //BattleGrounds
    public static Vector battlearea;

    //Refrence to main
    private JavaPlugin mainClass = JavaPlugin.getProvidingPlugin(MobWars.class);

    //Init Battle
    public Battle(ArrayList<Player> players, Vector grounds, int index){

        //Set variables
        battlePlayers.addAll(players);
        players.clear();
        battlearea = grounds;
        battleindex = index;

        //Start Battle
        initBattle(battleindex, battlearea);
    }    

    //Init starting stage of the Battle
    private void initBattle(int ind, Vector telearea) {

        //All player tasks
        battlePlayers.forEach(
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
        playerChooseList.addAll(battlePlayers);
        opposingCommanders[0] = playerChooseList.get(new Random().nextInt(playerChooseList.size()));
        playerChooseList.remove(opposingCommanders[0]);
        opposingCommanders[1] = playerChooseList.get(new Random().nextInt(playerChooseList.size()));
        playerChooseList.remove(opposingCommanders[1]);

        //Assign commanding properties
        opposingCommanders[0].setGameMode(GameMode.CREATIVE);
        opposingCommanders[1].setGameMode(GameMode.CREATIVE);
        opposingCommanders[0].setInvulnerable(true);
        opposingCommanders[1].setInvulnerable(true);

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

        //Give devices
        ItemStack[] commandingItems = new ItemStack[]{ commandersBaton, commandersPickStick, commandersRetreat };
        opposingCommanders[0].getInventory().setContents(commandingItems);
        opposingCommanders[0].getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        opposingCommanders[1].getInventory().setContents(commandingItems);
        opposingCommanders[1].getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));

        //Assign Commander Variables
        //Assign Spawn Location
        World bw = Bukkit.getWorld("mw_BattleWorld");
        Location[] commanderLocations = new Location[]{
            new Location(bw, telearea.getX() + 5, bw.getHighestBlockYAt((int) telearea.getX() + 5, (int) telearea.getZ() - 50), telearea.getZ() - 50),
            new Location(bw, telearea.getX() + 95, bw.getHighestBlockYAt((int) telearea.getX() + 95, (int) telearea.getZ() - 50), telearea.getZ() - 50)
        };
        commanderLocations[0].setYaw(270);
        commanderLocations[1].setYaw(90);

        //Teleport Opposing Commanders In
        opposingCommanders[0].teleport(commanderLocations[0]);
        opposingCommanders[1].teleport(commanderLocations[1]);

        //Tell commanders a tip
        scheduler.scheduleSyncDelayedTask(mainClass,
            () -> {
                opposingCommanders[0].sendMessage(loggingPrefix + ChatColor.AQUA + "You are Commander of Army 1! Fly around and command your MobSquadrons to Victory!");
                opposingCommanders[1].sendMessage(loggingPrefix + ChatColor.AQUA + "You are Commander of Army 2! Fly around and command your MobSquadrons to Victory!");
            }
        , 20L);

        //Assign Squads
        ArrayList<Player>[] squadPlayerLists = new ArrayList[]{new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()};
        playerChooseList.forEach(
            player -> {
                if(playerChooseList.indexOf(player) < 2)
                    squadPlayerLists[0].add(player);
                else if(playerChooseList.indexOf(player) > 1 && playerChooseList.indexOf(player) < 4)
                    squadPlayerLists[1].add(player);
                else if(playerChooseList.indexOf(player) > 3 && playerChooseList.indexOf(player) < 6)
                    squadPlayerLists[2].add(player);
                else
                    squadPlayerLists[3].add(player);
            }
        );

        //Assign squad spawn location
        Location[] squadLocations = new Location[]{
            commanderLocations[0].add(0, 0, 4).subtract(1, 0, 0),
            commanderLocations[0].subtract(2, 0, 4),
            commanderLocations[1].add(0, 0, 4).subtract(1, 0, 0),
            commanderLocations[1].subtract(1, 0, 4)
        };
        for(int i = 0; i < squadLocations.length; i++)
            if(i < 2)
                squadLocations[i].setYaw(270);
            else
                squadLocations[i].setYaw(90);

        //Create Squads
        Player owner;
        for(int i = 0; i < squadPlayerLists.length; i++) {
            if(i < 2)
                owner = opposingCommanders[0];
            else
                owner = opposingCommanders[1];
            squads.add(new Squad(owner, squadPlayerLists[i], squadLocations[i], i));
        }

        //Control Squads
        scheduler.scheduleSyncRepeatingTask(mainClass,
            () -> {
                squads.forEach(squad -> squad.move(squads.indexOf(squad)));
                for(int i = 0; i < squads.size(); i++)
                    Squad.setSquadLocation(Squad.getSquadPlayers(i).get(0).getLocation(), i);
            }
        , 0L, 20L);

        //Log the Squad's Health to the SquadPlayers
        scheduler.scheduleSyncRepeatingTask(mainClass, () -> battlePlayers.forEach(player -> player.sendMessage(ChatColor.BLUE + "Your Squad's Health: " + Squad.getHealth(Battle.getSquadIndex(0, Squad.getSquadPlayer(player))) + "HP")), 0L, 1800L);

        //Check for an army's defeat and declare it
        scheduler.scheduleSyncRepeatingTask(mainClass,
            () -> {
                for(int i = 0; i < squads.size(); i++)
                    if(Squad.getHealth(i) < 0.5){
                        for(int i2 = 0; i2 < battlePlayers.size(); i2++) {
                            battlePlayers.get(i2).setInvulnerable(false);
                            battlePlayers.get(i2).setAllowFlight(false);
                            if (Squad.getOwner(Battle.getSquadIndex(0, Squad.getSquadPlayer(battlePlayers.get(i2)))).equals(Battle.getBattleSquads(0).get(i)))
                                battlePlayers.get(i2).kickPlayer("The battle is LOST.");
                            else
                                battlePlayers.get(i2).kickPlayer("You WON the Battle! Congrats!");
                        }
                    }
            }
        , 0L, 40L);

        //Register function events
        mainClass.getServer().getPluginManager().registerEvents(new EventPlayerMoveOut(), mainClass);
        mainClass.getServer().getPluginManager().registerEvents(new EventCommanderAction(), mainClass);
        mainClass.getServer().getPluginManager().registerEvents(new EventCombat(), mainClass);
    }

    //Get the players in an instance
    public static ArrayList<Player> getBattlePlayers(int index){ return BattleManager.getBattle(index).battlePlayers; }

    //Get the players in an instance
    public static ArrayList<Squad> getBattleSquads(int index){ return BattleManager.getBattle(index).squads; }

    public static int getSquadIndex(int index, Squad squad){ return getBattleSquads(index).indexOf(squad); }

    //Get which Battle the player is in
    public static Battle getPlayerBattle(Player p){
        Battle battle = currbattles.get(0);
            for(int i = 0; i < currbattles.size(); i++)
                for(int i2 = 0; i2 < getBattlePlayers(i).size(); i2++)
                    if(getBattlePlayers(i).contains(p))
                        battle = BattleManager.getBattle(i);
        return battle;
    }

    //Get if the player is in a Battle
    public static boolean isPlayerInBattle(Player p){
        boolean b = false;
        for(int i = 0; i < currbattles.size(); i++)
            for(int i2 = 0; i2 < getBattlePlayers(i).size(); i2++)
                if(getBattlePlayers(i).contains(p))
                    b = true;
        return b;
    }
    
}
