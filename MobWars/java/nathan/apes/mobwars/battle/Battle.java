package nathan.apes.mobwars.battle;

import nathan.apes.mobwars.event.battle.*;
import nathan.apes.mobwars.util.*;

import java.util.*;

import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.util.Vector;

import static nathan.apes.mobwars.main.MobWars.loggingPrefix;
import static nathan.apes.mobwars.main.MobWars.mainClass;

//The Battle Object: Operates all battles in the system

public class Battle{

    //BattleIndex
    public int battleindex;

    //Stage
    public static int battlestage = -1;

    //Players in Battle
    private final ArrayList<Player> battlePlayers;

    //Squads in Battle
    public final ArrayList<Squad> squads = new ArrayList<>();

    //BattleGrounds
    public static Vector battlearea;

    //Init Battle
    public Battle(ArrayList<Player> players, Vector grounds, int index){

        battlePlayers = players;
        battlearea = grounds;
        battleindex = index;

        initBattle(battleindex, battlearea);
    }    

    //Init starting stage of the Battle
    private void initBattle(int ind, Vector telearea){

        //Players in battle
        ArrayList<Player> battlePlayers = Battle.getBattlePlayers(BattleManager.getBattle(ind));

        //All player tasks
        battlePlayers.forEach(
            player -> {
                player.setInvulnerable(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                player.sendMessage(loggingPrefix + ChatColor.GREEN + "Game started!");
            }
        );

        //Assign commanders
        Player[] opposingCommanders = new Player[]{battlePlayers.get(new Random().nextInt(battlePlayers.size() - 1)), battlePlayers.get(new Random().nextInt(battlePlayers.size() - 2))};

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
        ItemStack[] commandingItems = new ItemStack[]{ commandersBow, commandersBaton};
        opposingCommanders[0].getInventory().setContents(commandingItems);
        opposingCommanders[1].getInventory().setContents(commandingItems);

        //Assign Commander Variables
        World bw = Bukkit.getWorld("mw_BattleWorld");
        Location[] commanderLocations = new Location[]
        {new Location(bw, telearea.getX() + 100, bw.getHighestBlockYAt((int) telearea.getX() + 100, (int) telearea.getZ() + 5), telearea.getZ() + 5)
        , new Location(bw, telearea.getX() + 100, bw.getHighestBlockYAt((int) telearea.getX() + 100, (int) telearea.getZ() + 5), telearea.getZ() + 195)};

        //Teleport Opposing Commanders In
        opposingCommanders[0].teleport(commanderLocations[0]);
        opposingCommanders[1].teleport(commanderLocations[1]);

        //Tell commanders a tip
        opposingCommanders[0].sendMessage(loggingPrefix + ChatColor.AQUA + "Fly around and command your MobSquadrons!");
        opposingCommanders[1].sendMessage(loggingPrefix + ChatColor.AQUA + "Fly around and command your MobSquadrons!");

        //Assign Squads
        battlePlayers.remove(opposingCommanders[0]);
        battlePlayers.remove(opposingCommanders[1]);
        ArrayList<Player>[] squadPlayerLists = new ArrayList[]{new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()};
        battlePlayers.forEach(
            player ->{
                if(battlePlayers.indexOf(player) < 4)
                    squadPlayerLists[0].add(player);
                else if(battlePlayers.indexOf(player) > 4 && battlePlayers.indexOf(player) < 8)
                    squadPlayerLists[1].add(player);
                else if(battlePlayers.indexOf(player) > 8 && battlePlayers.indexOf(player) < 12)
                    squadPlayerLists[2].add(player);
                else
                    squadPlayerLists[3].add(player);
            }
        );

        //Assign squad spawn location
        Location[] squadLocations = new Location[]{commanderLocations[0].subtract(0, 2, 0).add(4, 0, 0),
        commanderLocations[0].subtract(4, 2, 0), commanderLocations[1].subtract(0, 2, 0).add(4, 0, 0),
        commanderLocations[0].subtract(4, 2, 0)};

        //Create Squads
        for(int i = 0; i < squadPlayerLists.length; i++)
            if(i < 2)
                squads.add(new Squad(opposingCommanders[0], squadLocations[i], i));
            else
                squads.add(new Squad(opposingCommanders[1], squadLocations[i], i));

        //mainClass.getServer().getPluginManager().registerEvents(new EventPlayerMoveOut(), mainClass);
        //Enable after Beta...
    }

    //Get the players in an instance
    public static ArrayList<Player> getBattlePlayers(Battle b){ return b.battlePlayers; }

    //Get which Battle the player l in
    public static Battle getPlayerBattle(Player p){
        Battle battle = BattleManager.currbattles.get(0);
            for(int i = 0; i < BattleManager.currbattles.size(); i++)
                for(int i2 = 0; i2 < BattleManager.getBattle(i).battlePlayers.size(); i2++)
                    if(BattleManager.getBattle(i).battlePlayers.get(i2).equals(p))
                        battle = BattleManager.getBattle(i);
        return battle;
    }

    //Get if the player is in a Battle
    public static boolean isPlayerInBattle(Player p){
        boolean b = false;
        for(int i = 0; i < BattleManager.currbattles.size(); i++){
            if(Battle.getBattlePlayers(BattleManager.currbattles.get(i)).contains(p))
                b = true;
            else
                b = false;
        }
        return b;
    }
    
}
