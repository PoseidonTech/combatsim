package com.rpg.combatsim.simulation;

import com.rpg.combatsim.domain.Combatant;
import com.rpg.combatsim.domain.Weapon;
import com.rpg.combatsim.utility.SimulationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BasicFFASimulation implements ISimulation {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicFFASimulation.class);

    public BasicFFASimulation() { // default constructor
    }

    public void handleDeath(List<Combatant> livingCombatantList, Combatant attacker, Map<Combatant, Map<Combatant, Integer>> threatMap) {
        Combatant deadCombatant = attacker.getCurrentTarget();
        LOGGER.info("{} Died!", deadCombatant.getName());
        //remove dead combatant from livingCombatantList
        livingCombatantList.remove(deadCombatant);

        //remove dead combatant from base threatMap and all individual threat maps
        threatMap.remove(deadCombatant);
        for (Map<Combatant, Integer> thisThreatMap : threatMap.values()) {
            thisThreatMap.remove(deadCombatant);
        }

        //redirect any combatants targeting the dead combatant
        for (Combatant livingCombatant : livingCombatantList) {
            if (livingCombatant.getCurrentTarget().getName().equals(deadCombatant.getName())) {
                livingCombatant.setCurrentTarget(SimulationUtils.getMaxThreat(threatMap.get(livingCombatant)));
            }
        }
    }

    public void takeTurn(List<Combatant> livingCombatantList, Combatant attacker, Map<Combatant, Map<Combatant, Integer>> threatMap) {
        Weapon myMainWeapon = attacker.getMainWeapon();
        Combatant myTarget = attacker.getCurrentTarget();

        if (myMainWeapon.getCurrentAmmo() == -1 || myMainWeapon.getCurrentAmmo() > 0) {
            //hit roll
            boolean hit = SimulationUtils.hitRoll(attacker);
            if (hit) {
                //damage roll
                Integer damage = SimulationUtils.damageRoll(attacker);

                //take damage
                boolean targetDied = myTarget.takeDamage(damage);
                String verb = myMainWeapon.getWeaponType().equals("Ranged") ? "shot" : "hit";
                LOGGER.info("{} {} {} with a {} for {} damage",
                        attacker.getName(), verb, myTarget.getName(), myMainWeapon.getName(), damage);

                //update threat
                Integer oldThreat = threatMap.get(myTarget).get(attacker);
                threatMap.get(myTarget).put(attacker, oldThreat + damage);

                //handle death
                if (targetDied) {
                    handleDeath(livingCombatantList, attacker, threatMap);
                } else {
                    myTarget.setCurrentTarget(SimulationUtils.getMaxThreat(threatMap.get(myTarget)));
                }//end target died/lived block
            } else {
                LOGGER.info("{} missed", attacker.getName());
            }//end hit or miss block
        } else {
            myMainWeapon.setCurrentAmmo(myMainWeapon.getMaxAmmo());
            LOGGER.info("{} reloaded", attacker.getName());
        }//end gun loaded check
    }
    //endregion

    public void genFFASim(List<Combatant> combatantList) {
        Map<Combatant, Map<Combatant, Integer>> threatMap = SimulationUtils.genInitThreatMapFFA(combatantList);
        SimulationUtils.setInitTargetsRandom(combatantList);
        List<Combatant> livingCombatantList = new ArrayList<>(combatantList);

        LOGGER.info("Weapon Mapping:");
        LOGGER.info("{}", Arrays.toString(combatantList.stream().map(x -> x.getName() + ":" + x.getMainWeapon().getName()).toArray()));

        int roundNum = 1;

        LOGGER.info("Health: {}", Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentHealth()).toArray()));
        LOGGER.info("Targets: {}", Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentTarget().getName()).toArray()));

        while (livingCombatantList.size() > 1) {
            LOGGER.info("Round {}", roundNum);

            List<Combatant> thisRoundTurnOrderList = SimulationUtils.getRoundOrderList(livingCombatantList);
            LOGGER.info("Turn Order: {}", Arrays.toString(thisRoundTurnOrderList.stream().map(Combatant::getName).toArray()));

            for (Combatant combatant : thisRoundTurnOrderList) {
                if(combatant.getCurrentHealth() > 0) {//death check
                    takeTurn(livingCombatantList, combatant, threatMap);
                }
            }//end combatant turn loop

            LOGGER.info("End of Round");

            //update round
            roundNum++;
            if(livingCombatantList.size() > 1) {
                LOGGER.info("Health: {}", Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentHealth()).toArray()));
                LOGGER.info("Targets: {}", Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentTarget().getName()).toArray()));
            }
        }//end combat while loop

        //declare winner
        LOGGER.info("{} Wins!", livingCombatantList.get(0).getName());
    }

    public void runSim(List<Combatant> combatantList) {
        genFFASim(combatantList);
    }
}
