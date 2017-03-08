package nathan.apes.mw.main;

import nathan.apes.mw.func.world.lobby.InitLobby;
import nathan.apes.mw.func.world.battle.InitBattle;
import nathan.apes.mw.util.*;

import org.bukkit.plugin.java.JavaPlugin;

public class MobWars extends JavaPlugin{
    
    public void onEnable(){
        
        this.getLogger().info("Enabling MobWars...REMEMBER YOUR STRATS!");
        
        new InitLobby().lobbyinit();
        new InitBattle().initBattleWorld();
        
        new PlayerQueue().startqueue();
        
    }
    
    public void onDisable(){
        
        this.getLogger().info("Disabling MobWars...TRY AGAIN TOMMORROW!");        
        
    }
    
}
