package nathan.apes.mw.event.battle;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class EventPlayerMoveOut implements Listener{

    @EventHandler
    public void onPlayerMoveOutofBounds(PlayerMoveEvent pme){
        
        Player pl = pme.getPlayer();
        
        //Keep the player from leaving the Battle
    
    }

}
