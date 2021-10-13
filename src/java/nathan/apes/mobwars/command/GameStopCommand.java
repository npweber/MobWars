package nathan.apes.mobwars.command;

//GameStopCommand: Disables all MobWars games

import nathan.apes.mobwars.battle.Battle;
import nathan.apes.mobwars.battle.Squad;
import nathan.apes.mobwars.world.lobby.InitLobbyWorld;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static nathan.apes.mobwars.main.MobWars.disabling;
import static nathan.apes.mobwars.main.MobWars.loggingPrefix;

public class GameStopCommand implements CommandExecutor {

    //Run command, starts the game
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mwdisable")) {
            if (sender.hasPermission("mobwars.game")){
                //Disable all games
                disabling = true;
                Battle.getAllSquads().forEach(battleSquads -> battleSquads.forEach(
                    squad -> {
                        Squad.setDisabled(Battle.getAllSquads().indexOf(battleSquads), battleSquads.indexOf(squad), true);
                        Squad.setInForm(false, Battle.getAllSquads().indexOf(battleSquads), battleSquads.indexOf(squad));
                        Squad.setHealth(0.0, Battle.getAllSquads().indexOf(battleSquads), battleSquads.indexOf(squad));
                    }
                ));
                //Disable future games
                InitLobbyWorld.setMatchMakingEnabled(false);
                //Log Disable
                sender.sendMessage(loggingPrefix + ChatColor.GOLD + "You disabled all current battles.");
            }
            return true;
        }
        return false;
    }
}
