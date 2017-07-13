package nathan.apes.mobwars.world.battle;

import java.util.*;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.util.Vector;

import static nathan.apes.mobwars.main.MobWars.bw;
import static nathan.apes.mobwars.main.MobWars.xBndDatabase;

//FindBattleground: Utility that finds an open Battleground for each Match

public class FindBattleground{

    //Battlegrounds to be set
    private Vector battlebnds;
    private int xBnd;
    private int zBnd;
    private double y;

    //Scanner var through config areas
    private ArrayList<Integer> areas;

    //Controls whether the battleground is suitable for battle
    private boolean isSuitable;

    public Vector findBattleground(){

        //Cycle through possible locations until the suitable one is found
        do {
            //Choose a random area
            xBnd = new Random().nextInt(20000000);
            zBnd = xBnd + 100;
            y = bw.getHighestBlockAt(xBnd, zBnd).getY();
            battlebnds = new Vector((double) xBnd, y, (double) zBnd);

            //Check if the area has been a previous battleground
            areas = new ArrayList<>();
            String[] xValues = {};
            if (!xBndDatabase.getString("takenbattlegroundXValues").isEmpty())
                xValues = xBndDatabase.getString("takenbattlegroundXValues").split(",");
            for (int i = 0; i < xValues.length; i++)
                areas.add(Integer.parseInt(xValues[i]));
            for (int i = 0; i < areas.size(); i++) {
                if ((xBnd < areas.get(i) + 400 && xBnd > areas.get(i) - 200))
                    isSuitable = false;
                else isSuitable = true;
            }

            //Choose another area if it is not a suitable battleground
            Location searchLocation = battlebnds.toLocation(bw);
            int[] checkpointMarkerX = {0, 25, 50, 100, 0, 25, 50, 100, 0, 25, 50, 100, 0, 25, 50, 100};
            int[] checkpointMarkerZ = {0, 0, 0, 0, 25, 25, 25, 25, 50, 50, 50, 50, 100, 100, 100, 100};
            int successcheckpointCounter = 0;
            for (int i = 0; i < 16; i++) {
                searchLocation = searchLocation.add(checkpointMarkerX[i], 0, checkpointMarkerZ[i]);
                if (searchLocation.getBlock().getBiome().equals(Biome.DESERT)
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
                        || searchLocation.getBlock().getBiome().equals(Biome.SAVANNA)) {
                    successcheckpointCounter++;
                }
            }
            if(successcheckpointCounter == 16)
                isSuitable = true;
            else isSuitable = false;
        } while (!isSuitable);

        //Save the taken location to the database
        if(xBndDatabase.getString("takenbattlegroundXValues").equalsIgnoreCase(""))
            xBndDatabase.set("takenbattlegroundXValues", xBnd);
        else
            xBndDatabase.set("takenbattlegroundXValues", xBndDatabase.getString("takenbattlegroundXValues") + "," + xBnd);

        return battlebnds;
    }
}