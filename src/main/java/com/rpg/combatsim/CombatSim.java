package com.rpg.combatsim;

import com.rpg.combatsim.domain.Combatant;
import com.rpg.combatsim.domain.GameConfig;
import com.rpg.combatsim.domain.Weapon;
import com.rpg.combatsim.simulation.BasicFFASimulation;
import com.rpg.combatsim.simulation.BasicTDMSimulation;
import com.rpg.combatsim.simulation.ISimulation;
import com.rpg.combatsim.utility.CombatantGenerationUtils;
import com.rpg.combatsim.utility.FileImportManager;
import com.rpg.combatsim.utility.SimulationUtils;
import com.rpg.combatsim.utility.WeaponGenerationUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CombatSim {
    public static void main(String[] args) {

        if(System.getProperty("gameConfig") == null) {
            String path = CombatSim.class.getResource("/gameConfig.xml").getPath();
            System.setProperty("gameConfig", path);
        }

        GameConfig gameConfig = FileImportManager.gameConfigFileImport();

        //combatant lists
//        List<Combatant> baseLindaList = CombatantGenerationUtils.generateBaseLindaList();
//        List<Combatant> baseEnemiesList = CombatantGenerationUtils.generateBaseEnemiesList();
//        List<Combatant> combinedLindaList = new ArrayList<>(baseLindaList);
//        combinedLindaList.addAll(baseEnemiesList);

        //weapon lists
//        List<Weapon> baseLindaWeaponList = WeaponGenerationUtils.generateBaseLindaWeaponList();
//        List<Weapon> combinedLindaWeaponList = WeaponGenerationUtils.generateBaseLindaCombinedWeaponList();


        //simulations
//        BasicFFASimulation basicFFASimulation = new BasicFFASimulation();

        BasicTDMSimulation randomTdmSim = new BasicTDMSimulation();

//        BasicTDMSimulation fourVfourTdmSim = new BasicTDMSimulation();
//        Map<String, List<Combatant>> lindaVenemiesTeamMap = new HashMap<>();
//        lindaVenemiesTeamMap.put("Team Linda", baseLindaList);
//        lindaVenemiesTeamMap.put("Team Enemies", baseEnemiesList);
//        fourVfourTdmSim.setTeamsMap(lindaVenemiesTeamMap);

//        List<Combatant> chosenCombatantList = combinedLindaList;
//        List<Weapon> chosenWeaponList = combinedLindaWeaponList;
        List<Combatant> chosenCombatantList = gameConfig.getCombatants();
        List<Weapon> chosenWeaponList = gameConfig.getWeapons();
        SimulationUtils.grabRandomWeapons(chosenCombatantList, chosenWeaponList);

        //gen teams list for randomTdmSim case
        randomTdmSim.setTeamsMap(BasicTDMSimulation.generateRandomTeamsMap(chosenCombatantList, 3));
        SimulationUtils.resetHealthAndReloadWeapons(chosenCombatantList);

        ISimulation chosenSim = randomTdmSim;

        chosenSim.runSim(chosenCombatantList);

        Scanner sc = new Scanner(System.in);
        System.out.println("Again? (1 or 0)");
        int choice = sc.nextInt();
        while(choice != 0) {
            //gen teams list for randomTdmSim case
            randomTdmSim.setTeamsMap(BasicTDMSimulation.generateRandomTeamsMap(chosenCombatantList, 2));

            SimulationUtils.resetHealthAndReloadWeapons(chosenCombatantList);
            chosenSim.runSim(chosenCombatantList);
            System.out.println("Again? (1 or 0)");
            choice = sc.nextInt();
        }
    }
}