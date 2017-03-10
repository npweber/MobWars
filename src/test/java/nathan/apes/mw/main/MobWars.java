package nathan.apes.mw.main;

import nathan.apes.mw.func.world.lobby.InitLobbyWorld;
import nathan.apes.mw.func.world.battle.InitBattleWorld;
import nathan.apes.mw.util.*;

import org.bukkit.plugin.java.JavaPlugin;

public class MobWars extends JavaPlugin{
    
    public void onEnable(){
        
        this.getLogger().info("Enabling MobWars...");
        
        new InitLobbyWorld();
        new InitBattleWorld();
        
        new PlayerQueue();
        
    }
    
    public void onDisable(){
        
        this.getLogger().info("Disabling MobWars...");
        
        this.getServer().getScheduler().cancelTasks(this);
        
    }
    
}
