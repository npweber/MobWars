package nathan.apes.mobwars.world;

import java.util.*;

import org.bukkit.*;

//CheckWorld: WorldChecker Util

public class CheckWorld {

    //World
    public static boolean hasw = false;

    //WorldChecker
    public CheckWorld(String name){

        //Get all worlds
        List<World> serverws = Bukkit.getWorlds();

        //Check each world for the target world
        for(int i = 0; i < serverws.size(); i++){
            if(serverws.get(i).getName().equalsIgnoreCase(name)){
                hasw = true;
            }
        }
    }
}