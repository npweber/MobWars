package nathan.apes.mobwars.battle;

import nathan.apes.mobwars.event.battle.EventCombat;
import nathan.apes.mobwars.event.battle.EventCommanderAction;
import nathan.apes.mobwars.event.battle.EventPlayerMoveOut;
import nathan.apes.mobwars.main.MobWars;
import nathan.apes.mobwars.util.*;

import java.util.*;

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
        initBattle(battleindex, battlearea);
    }    

    //Init starting stage of the Battle
    private void initBattle(int ind, Vector telearea) {

        //All player tasks
        battlePlayers.forEach(
            player -> {
                scheduler.scheduleSyncDelayedTask(mainClass,
                    () -> {
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        player.sendMessage(loggingPrefix + ChatColor.GREEN + "Game started!");
                    }
                , 100L);
            }
        );

        //Assign commanders
        ArrayList<Player> commanderChoosingList = new ArrayList<>();
        commanderChoosingList.addAll(battlePlayers);
        Player[] opposingCommanders = new Player[2];
        opposingCommanders[0] = commanderChoosingList.get(new Random().nextInt(commanderChoosingList.size()));
        commanderChoosingList.remove(opposingCommanders[0]);
        opposingCommanders[1] = commanderChoosingList.get(new Random().nextInt(commanderChoosingList.size()));

        //Assign commanding properties
        opposingCommanders[0].setAllowFlight(true);
        opposingCommanders[0].setFlying(true);
        opposingCommanders[1].setAllowFlight(true);
        opposingCommanders[1].setFlying(true);
        opposingCommanders[0].setInvulnerable(true);
        opposingCommanders[1].setInvulnerable(true);

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
        for(int i = 0; i < 1; i++)
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
                if(!(player.equals(opposingCommanders[0]) || player.equals(opposingCommanders[1]))){
                    if (battlePlayers.indexOf(player) < 4)
                        squadPlayerLists[0].add(player);
                    else if (battlePlayers.indexOf(player) > 4 && battlePlayers.indexOf(player) < 8)
                        squadPlayerLists[1].add(player);
                    else if (battlePlayers.indexOf(player) > 8 && battlePlayers.indexOf(player) < 12)
                        squadPlayerLists[2].add(player);
                    else if(battlePlayers.indexOf(player) > 12 && battlePlayers.indexOf(player) < 16)
                        squadPlayerLists[3].add(player);
                }
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
            if(i < 2)
                squads.add(new Squad(opposingCommanders[0], squadPlayerLists[i], squadLocations[i], i));
            else
                squads.add(new Squad(opposingCommanders[1], squadPlayerLists[i], squadLocations[i], i));

        //Control Squads
        scheduler.scheduleSyncRepeatingTask(mainClass,
            () -> {
                squads.forEach(squad -> squad.move(squads.indexOf(squad)));
                for(int i = 0; i < squads.size(); i++)
                    Squad.setSquadLocation(Squad.getSquadPlayers(i).get(0).getLocation(), i);
            }
        , 0L, 40L);

        scheduler.scheduleSyncRepeatingTask(mainClass, () -> squads.forEach(squad -> Squad.squadPlayers.forEach(playerList -> playerList.forEach(player -> player.sendMessage(ChatColor.BLUE + "Your Squad's Health: " + Squad.getHealth(squads.indexOf(squad)) + "HP")))), 0L, 500L);

        //Register function events
        mainClass.getServer().getPluginManager().registerEvents(new EventPlayerMoveOut(), mainClass);
        mainClass.getServer().getPluginManager().registerEvents(new EventCommanderAction(), mainClass);
        mainClass.getServer().getPluginManager().registerEvents(new EventCombat(), mainClass);
    }

    //Get the players in an instance
    public static ArrayList<Player> getBattlePlayers(Battle b){ return b.battlePlayers; }

    //Get the players in an instance
    public static ArrayList<Squad> getBattleSquads(Battle b){ return b.squads; }

    public static int getSquadIndex(Player squadPlayer, Squad squad){ return getBattleSquads(getPlayerBattle(squadPlayer)).indexOf(squad); }

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
