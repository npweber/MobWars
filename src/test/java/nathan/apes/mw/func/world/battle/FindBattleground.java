package nathan.apes.mw.func.world.battle;

import java.util.*;

import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class FindBattleground{
    
    public Vector findBattleground(){
        
        ArrayList<Vector> areas = new ArrayList<Vector>();
        
        int Xbndarena = new Random().nextInt(100000000);
        
        int Zbndarena = Xbndarena + 200;         
        
        Block yb = InitBattleWorld.battlew.getHighestBlockAt(Xbndarena, Zbndarena);
        
        double arenay = (double) yb.getY();
        
        Vector battlebnds = new Vector((double) Xbndarena, arenay, (double) Zbndarena);
        
        areas.add(battlebnds);
        
        if(areas.size() > 1){
        
            for(int i = 1; i < areas.size(); i++){
            
                double x = areas.get(i).getX();
                double z = areas.get(i).getZ();
                
                double xcurr = battlebnds.getX();
                double zcurr = battlebnds.getZ();
                
                if((xcurr >= x && xcurr <= x + 600.0) && (zcurr >= z && zcurr <= z + 600.0)){
                
                    findBattleground();
                    
                }
                
            }
        
        }        
        
        return battlebnds;
    }
    
}
