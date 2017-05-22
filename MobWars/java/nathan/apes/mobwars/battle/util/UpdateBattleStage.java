package nathan.apes.mobwars.battle.util;

import nathan.apes.mobwars.util.BattleManager;

import static nathan.apes.mobwars.util.BattleManager.battlesEnabled;

//Updater: Updates all running BattleStage's

public class UpdateBattleStage {

    //Updater
    public UpdateBattleStage(){

        do{
            //Go through battles and test for certain conditions that indicate stage
            BattleManager.currbattles.forEach(
                battle -> {
                    //if()
                    //(Temp) <Put condition here>
                }
            );
        } while (battlesEnabled);
    }
    
}
