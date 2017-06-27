package nathan.apes.mobwars.command;

//GameCommand: Creates a game environment for Beta purposes

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static nathan.apes.mobwars.main.MobWars.loggingPrefix;
import static nathan.apes.mobwars.util.BattleManager.battlePlayers;

public class GameCommand implements CommandExecutor{

    //Run command, starts the game
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mw")) {
            if (sender.hasPermission("mobwars.game")){
                if(Bukkit.getOnlinePlayers().size() == 4) {
                    //Add players to queue, starting game
                    battlePlayers.addAll(Bukkit.getOnlinePlayers());
                }
                else
                    //Tell the sender they do not have players
                    sender.sendMessage(loggingPrefix + ChatColor.DARK_RED + "You need to have players in order to start a game.");
            }
            return true;
        }
        return false;
    }
}