package nathan.apes.mobwars.world.battle;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.Vector;

import static nathan.apes.mobwars.main.MobWars.xBndDatabase;

//FindBattleground: Utility that finds an open Battleground for each Match

public class FindBattleground{

    //Battlegrounds to be set
    private Vector battlebnds;

    private ArrayList<Integer> areas;

    //Controls whether the battleground is suitable for battle
    private boolean isSuitable = true;

    public Vector findBattleground(){

        //Choose a random area
        int xBnd = new Random().nextInt(20000000);
        int zBnd = xBnd + 100;
        double y = Bukkit.getWorld("mw_BattleWorld").getHighestBlockAt(xBnd, zBnd).getY();
        battlebnds = new Vector((double) xBnd, y, (double) zBnd);

        //Control suitability
        isSuitable = true;

        //Check if the area has been a previous battleground
        areas = new ArrayList<>();
        String[] xValues = {};
        if(!xBndDatabase.getString("takenbattlegroundXValues").isEmpty())
            xValues = xBndDatabase.getString("takenbattlegroundXValues").split(",");
        for(int i = 0; i < xValues.length; i++)
            areas.add(Integer.parseInt(xValues[i]));
        for (int i = 0; i < areas.size(); i++) {
            if ((xBnd < areas.get(i) + 400 && xBnd > areas.get(i) - 200))
                isSuitable = false;
        }

        //Choose another area if it is not a suitable battleground
        //Expand to not choose ANY non-suitable battlegrounds
        World bw = Bukkit.getWorld("mw_BattleWorld");
        Location searchLocation = battlebnds.toLocation(bw);
        int[] checkpointMarkerX = {0, 50, 100, 0, 50, 100, 0, 50, 100};
        int[] checkpointMarkerZ = {0, 0, 0, 50, 50, 50, 100, 100, 100};
        for(int i = 0; i < 9; i++)
            searchLocation = searchLocation.add(checkpointMarkerX[i], 0, checkpointMarkerZ[i]);
            if (!((searchLocation.getBlock().getBiome().equals(Biome.DESERT)
                    || searchLocation.getBlock().getBiome().equals(Biome.DESERT_HILLS)
                    || searchLocation.getBlock().getBiome().equals(Biome.EXTREME_HILLS)
                    || searchLocation.getBlock().getBiome().equals(Biome.EXTREME_HILLS_WITH_TREES)
                    || searchLocation.getBlock().getBiome().equals(Biome.ICE_FLATS)
                    || searchLocation.getBlock().getBiome().equals(Biome.ICE_MOUNTAINS)
                    || searchLocation.getBlock().getBiome().equals(Biome.SMALLER_EXTREME_HILLS)
                    || searchLocation.getBlock().getBiome().equals(Biome.MESA_CLEAR_ROCK)
                    || searchLocation.getBlock().getBiome().equals(Biome.MESA_ROCK)
                    || searchLocation.getBlock().getBiome().equals(Biome.MUTATED_DESERT)
                    || searchLocation.getBlock().getBiome().equals(Biome.MUTATED_EXTREME_HILLS)
                    || searchLocation.getBlock().getBiome().equals(Biome.MUTATED_EXTREME_HILLS_WITH_TREES)
                    || searchLocation.getBlock().getBiome().equals(Biome.MUTATED_ICE_FLATS)
                    || searchLocation.getBlock().getBiome().equals(Biome.MUTATED_MESA)
                    || searchLocation.getBlock().getBiome().equals(Biome.MUTATED_SAVANNA)
                    || searchLocation.getBlock().getBiome().equals(Biome.MUTATED_SAVANNA_ROCK)
                    || searchLocation.getBlock().getBiome().equals(Biome.SAVANNA_ROCK)
                    || searchLocation.getBlock().getBiome().equals(Biome.MUTATED_PLAINS)
                    || searchLocation.getBlock().getBiome().equals(Biome.MUTATED_MESA_ROCK)
                    || searchLocation.getBlock().getBiome().equals(Biome.MUTATED_MESA_CLEAR_ROCK)
                    || searchLocation.getBlock().getBiome().equals(Biome.MESA)
                    || searchLocation.getBlock().getBiome().equals(Biome.PLAINS)
                    || searchLocation.getBlock().getBiome().equals(Biome.SAVANNA)))) {
                isSuitable = false;
            }

        //Re-Choose if the battleground is indeed not for use
        if(!(isSuitable))
            findBattleground();

        //Save the taken location to the database
        if(xBndDatabase.getString("takenbattlegroundXValues").equalsIgnoreCase(""))
            xBndDatabase.set("takenbattlegroundXValues", xBnd);
        else
            xBndDatabase.set("takenbattlegroundXValues", xBndDatabase.getString("takenbattlegroundXValues") + "," + xBnd);

        return battlebnds;
    }
}