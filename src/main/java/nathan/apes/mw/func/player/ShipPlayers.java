package nathan.apes.mw.func.player;

import nathan.apes.mw.func.world.battle.InitBattle;
import nathan.apes.mw.util.PlayerQueue;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ShipPlayers {
    
    private int Xbndarena;
    private int Zbndarena;
    
    private ArrayList<Vector> arenas = new ArrayList<Vector>();
    
    public ArrayList<Player> battlepls = new ArrayList<Player>();
    
    public Vector currvect;
    
    public Location p1tele;
    
    public Location p2tele;
    
    public ShipPlayers(){}
    
    public Vector getRandomCords(){
        
        Xbndarena = new Random().nextInt(100000000);
        
        Zbndarena = Xbndarena + 200;         
        
        Block yb = new InitBattle().battlew.getHighestBlockAt(Xbndarena, Zbndarena);
        
        double arenay = (double) yb.getY();
        
        Vector arenabnds = new Vector((double) Xbndarena, arenay, (double) Zbndarena);
        
        arenas.add(arenabnds);
        
        if(arenas.size() > 1){
        
            for(int i = 1; i < arenas.size(); i++){
            
                double x = arenas.get(i).getX();
                double z = arenas.get(i).getZ();
                
                double xcurr = arenabnds.getX();
                double zcurr = arenabnds.getZ();
                
                if((xcurr >= x && xcurr <= x + 600.0) && (zcurr >= z && zcurr <= z + 600.0)){
                
                    getRandomCords();
                    
                }
                
            }
        
        }        
        
        return arenabnds;
    }
    
    public void shipPlayers(){
        
        Player pl1 = new PlayerQueue().playerpair.get(0);
        Player pl2 = new PlayerQueue().playerpair.get(1);
        
        Vector arenabnds = getRandomCords();
        
        currvect = arenabnds;

        double plstrx = (double) arenabnds.getX() + 100.0;
        double plstrz = (double) arenabnds.getZ() + 10.0;
        
        Block yb = new InitBattle().battlew.getHighestBlockAt( (int) plstrx, (int) plstrz);
        double plstry = (double) yb.getY() + 2.0;
        
        Location pl1tp = new Location(new InitBattle().battlew, plstrx, plstry, plstrz);
        
        p1tele = pl1tp;
        
        Block yb2 = new InitBattle().battlew.getHighestBlockAt( (int) plstrx, (int) plstrz + 180);
        double plstry2 = (double) yb2.getY() + 2.0;
        
        Location pl2tp = new Location(new InitBattle().battlew, plstrx, plstry2, plstrz + 180.0);
        
        p2tele = pl2tp;
        
        pl1.teleport(pl1tp);
        pl2.teleport(pl2tp);
        
        new PlayerGiveItems().giveBattleItems();
        
        battlepls.add(pl1);
        battlepls.add(pl2);
        
    }
    
}
