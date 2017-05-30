package nathan.apes.mobwars.world.battle;

import java.util.*;

import nathan.apes.mobwars.main.MobWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static nathan.apes.mobwars.main.MobWars.config;

//FindBattleground: Utility that finds an open Battleground for each Match

public class FindBattleground{

    //Battlegrounds to be set
    private Vector battlebnds;

    //Previously taken Battlegrounds not to be slain on
    private final ArrayList<Vector> areas = new ArrayList<>();

    //Controls whether the battleground is suitable for battle
    public static boolean isSuitable = true;

    public Vector findBattleground(){
        //(Not able to be in this version of the game. For Beta purposes, the user will set the battlegrounds.)
        /*
        //Choose a random area
        int xBnd = new Random().nextInt(20000000);
        int zBnd = xBnd + 100;
        double y = InitBattleWorld.battlew.getHighestBlockAt(xBnd, zBnd).getY();
        battlebnds = new Vector((double) xBnd, y, (double) zBnd);

        //(Not needed for this stage in Development)
        //areas.add(battlebnds);
        //Change to config fields...

        //Check if the area has been a previous battleground
        if(areas.size() > 1) {
            for (int i = 1; i < areas.size(); i++) {
                double x = areas.get(i).getX();
                double z = areas.get(i).getZ();

                double xcurr = battlebnds.getX();
                double zcurr = battlebnds.getZ();

                //Choose another area if it has indeed served other opponents
                if ((xcurr >= x && xcurr <= x + 600.0) && (zcurr >= z && zcurr <= z + 600.0))
                    findBattleground();
            }
        }

        //Choose another area if it is not a suitable battleground
        //Expand to not choose ANY non-suitable battlegrounds
        World bw = Bukkit.getWorld("mw_BattleWorld");
        Location searchLocation = battlebnds.toLocation(bw);
        for(int i = 0; i < 100; i++) {
            for (int i2 = 0; i2 < 100; i2++) {
                searchLocation = battlebnds.toLocation(bw).add(xBnd + i, bw.getHighestBlockYAt(xBnd + i, (zBnd - 100) + i2),(zBnd - 100) + i2).subtract(0, 1, 0);
                if (!(searchLocation.getBlock().getBiome().equals(Biome.DESERT)
                        || searchLocation.getBlock().getBiome().equals(Biome.EXTREME_HILLS)
                        || searchLocation.getBlock().getBiome().equals(Biome.ICE_FLATS)
                        || searchLocation.getBlock().getBiome().equals(Biome.MESA)
                        || searchLocation.getBlock().getBiome().equals(Biome.PLAINS)
                        || searchLocation.getBlock().getBiome().equals(Biome.SAVANNA))
                        || searchLocation.getBlock().isLiquid()) {
                    isSuitable = false;
                    break;
                }
            }
        }
        if(!(isSuitable))
            findBattleground();
        */

        World bw = Bukkit.getWorld("mw_BattleWorld");
        battlebnds = new Vector(config.getDouble("battlelocationX"), bw.getHighestBlockYAt((int)config.getDouble("battlelocationX"), (int) config.getDouble("battlelocationZ")), config.getDouble("battlelocationZ"));
        return battlebnds;
    }
}