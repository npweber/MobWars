package nathan.apes.mobwars.battle;

import java.util.*;

import nathan.apes.mobwars.util.BattleManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

//The Squad Object: Handles all Squads in all Battles

public class Squad {

    //Players in Squad
    public ArrayList<Player> squadPlayers = new ArrayList<>();

    //Owner of Squad
    public Player squadowner;

    //Index
    public int squadindex = -1;

    //Init Squad
    public Squad(Player owner, Location spawnloc, int index){
        squadowner = owner;
        squadindex = index;

        spawnSquad(squadPlayers, spawnloc, squadindex);
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
}
