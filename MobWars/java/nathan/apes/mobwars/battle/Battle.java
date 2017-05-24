package nathan.apes.mobwars.battle;

import nathan.apes.mobwars.event.battle.*;
import nathan.apes.mobwars.event.battle.squad.EventCommanderAction;
import nathan.apes.mobwars.event.battle.squad.EventCommanderPickSquad;
import nathan.apes.mobwars.main.MobWars;
import nathan.apes.mobwars.util.*;

import java.util.*;

import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import static nathan.apes.mobwars.main.MobWars.loggingPrefix;
import static nathan.apes.mobwars.util.BattleManager.currbattles;

//The Battle Object: Operates all battles in the system

public class Battle{

    //BattleIndex
    public int battleindex;

    //Stage
    public static int battlestage = -1;

    //Players in Battle
    private final ArrayList<Player> battlePlayers = new ArrayList<>();

    //Squads in Battle
    public final ArrayList<Squad> squads = new ArrayList<>();

    //BattleGrounds
    public static Vector battlearea;

    //Refrence to main
    private JavaPlugin mainClass = JavaPlugin.getProvidingPlugin(MobWars.class);

    //Init Battle
    public Battle(ArrayList<Player> players, Vector grounds, int index){

        //Set varibles
        battlePlayers.addAll(players);
        players.clear();
        battlearea = grounds;
        battleindex = index;

        //Start processes
        initBattle(battleindex, battlearea, Bukkit.getScheduler());
    }    

    //Init starting stage of the Battle
    private void initBattle(int ind, Vector telearea, BukkitScheduler scheduler) {

        //All player tasks
        battlePlayers.forEach(
            player -> {
                player.setInvulnerable(true);
                scheduler.scheduleSyncDelayedTask(mainClass,
                    () -> {
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        player.sendMessage(loggingPrefix + ChatColor.GREEN + "Game started!");
                    }
                , 100L);
            }
        );

        //Assign commanders
        Player[] opposingCommanders = new Player[]{
            battlePlayers.get(new Random().nextInt(battlePlayers.size())),
            battlePlayers.get(new Random().nextInt(battlePlayers.size() - 1))
        };

        //Assign commanding properties
        opposingCommanders[0].setAllowFlight(true);
        opposingCommanders[0].setFlying(true);
        opposingCommanders[1].setAllowFlight(true);
        opposingCommanders[1].setFlying(true);

        //Give commanding devices
        //Commanders Bow (For actual shooting)
        ItemStack commandersBow = new ItemStack(Material.BOW);
        ItemMeta itemMeta1 = commandersBow.getItemMeta();
        itemMeta1.setDisplayName("Commander's Bow");
        commandersBow.setItemMeta(itemMeta1);

        //Commanders Baton (For direction to troops)
        ItemStack commandersBaton = new ItemStack(Material.BLAZE_ROD);
        ItemMeta itemMeta2 = commandersBaton.getItemMeta();
        itemMeta2.setDisplayName("Commander's Baton");
        commandersBaton.setItemMeta(itemMeta2);

        //Give devices
        ItemStack[] commandingItems = new ItemStack[]{ commandersBow, commandersBaton };
        opposingCommanders[0].getInventory().setContents(commandingItems);
        opposingCommanders[1].getInventory().setContents(commandingItems);

        //Assign Commander Variables
        World bw = Bukkit.getWorld("mw_BattleWorld");
        Location[] commanderLocations = new Location[]{
            new Location(bw, telearea.getX() + 5, bw.getHighestBlockYAt((int) telearea.getX() + 5, (int) telearea.getZ() + 5), telearea.getZ() - 100),
            new Location(bw, telearea.getX() + 195, bw.getHighestBlockYAt((int) telearea.getX() + 195, (int) telearea.getZ() + 5), telearea.getZ() - 100)
        };
        for(int i = 0; i < 2; i++)
            commanderLocations[i].setYaw(270);

        //Teleport Opposing Commanders In
        opposingCommanders[0].teleport(commanderLocations[0]);
        opposingCommanders[1].teleport(commanderLocations[1]);

        //Tell commanders a tip
        scheduler.scheduleSyncDelayedTask(mainClass,
            () -> {
                opposingCommanders[0].sendMessage(loggingPrefix + ChatColor.AQUA + "Fly around and command your MobSquadrons!");
                opposingCommanders[1].sendMessage(loggingPrefix + ChatColor.AQUA + "Fly around and command your MobSquadrons!");
            }
        , 20L);

        //Assign Squads
        ArrayList<Player>[] squadPlayerLists = new ArrayList[]{new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()};
        battlePlayers.forEach(
            player -> {
                //if(!(player.equals(opposingCommanders[0]) || player.equals(opposingCommanders[1])))
                    if (battlePlayers.indexOf(player) < 4)
                        squadPlayerLists[0].add(player);
                    else if (battlePlayers.indexOf(player) > 4 && battlePlayers.indexOf(player) < 8)
                        squadPlayerLists[1].add(player);
                    else if (battlePlayers.indexOf(player) > 8 && battlePlayers.indexOf(player) < 12)
                        squadPlayerLists[2].add(player);
                    else if(battlePlayers.indexOf(player) > 12 && battlePlayers.indexOf(player) < 16)
                        squadPlayerLists[3].add(player);
            }
        );

        //Assign squad spawn location
        Location[] squadLocations = new Location[]{
            new Location(squadPlayerLists[0].get(0).getWorld(), telearea.getX() + 100, (double) squadPlayerLists[0].get(0).getWorld().getHighestBlockYAt((int)telearea.getX() + 100,(int) telearea.getZ() - 5), telearea.getZ() - 5),
            commanderLocations[0].add(0, 0, 4).subtract(2, 0, 0),
            commanderLocations[0].subtract(2, 0, 4),
            commanderLocations[1].add(0, 0, 4).subtract(2, 0, 0),
            commanderLocations[1].subtract(2, 0, 4)
        };
        for(int i = 0; i < squadLocations.length; i++)
            squadLocations[i].setYaw(270);

        //Create Squads
        for(int i = 0; i < squadPlayerLists.length; i++)
            if(i < 1)
                squads.add(new Squad(opposingCommanders[0], squadPlayerLists[i], squadLocations[i], i));
            else
                squads.add(new Squad(opposingCommanders[1], squadPlayerLists[i], squadLocations[i], i));
        //Register function events
        mainClass.getServer().getPluginManager().registerEvents(new EventPlayerMoveOut(), mainClass);
        mainClass.getServer().getPluginManager().registerEvents(new EventCommanderPickSquad(), mainClass);
        mainClass.getServer().getPluginManager().registerEvents(new EventCommanderAction(), mainClass);
    }

    //Get the players in an instance
    public static ArrayList<Player> getBattlePlayers(Battle b){ return b.battlePlayers; }

    //Get which Battle the player l in
    public static Battle getPlayerBattle(Player p){
        Battle battle = currbattles.get(0);
            for(int i = 0; i < currbattles.size(); i++)
                for(int i2 = 0; i2 < BattleManager.getBattle(i).battlePlayers.size(); i2++)
                    if(BattleManager.getBattle(i).battlePlayers.contains(p))
                        battle = BattleManager.getBattle(i);
        return battle;
    }

    //Get if the player is in a Battle
    public static boolean isPlayerInBattle(Player p){
        boolean b = false;
        for(int i = 0; i < currbattles.size(); i++){
            if(Battle.getBattlePlayers(currbattles.get(i)).contains(p))
                b = true;
            else
                b = false;
        }
        return b;
    }
    
}
