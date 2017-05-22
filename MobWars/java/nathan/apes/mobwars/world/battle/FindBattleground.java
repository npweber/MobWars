package nathan.apes.mobwars.world.battle;

import java.util.*;

import org.bukkit.util.Vector;

//FindBattleground: Utility that finds an open Battleground for each Match

public class FindBattleground{

    //Previously taken Battlegrounds not to be slain on
    private final ArrayList<Vector> areas = new ArrayList<>();

    public Vector findBattleground(){

        //Choose a random area
        int xBnd = new Random().nextInt(20000000);
        int zBnd = xBnd + 200;
        double y = InitBattleWorld.battlew.getHighestBlockAt(xBnd, zBnd).getY();
        Vector battlebnds = new Vector((double) xBnd, y, (double) zBnd);
        areas.add(battlebnds);

        //Check if the area has been a previous battleground
        if(areas.size() > 1){
            for(int i = 1; i < areas.size(); i++){
                double x = areas.get(i).getX();
                double z = areas.get(i).getZ();
                
                double xcurr = battlebnds.getX();
                double zcurr = battlebnds.getZ();

                //Choose another area if it has indeed served other opponents
                if((xcurr >= x && xcurr <= x + 600.0) && (zcurr >= z && zcurr <= z + 600.0))
                    findBattleground();
            }
        }
        return battlebnds;
    }
    
}
