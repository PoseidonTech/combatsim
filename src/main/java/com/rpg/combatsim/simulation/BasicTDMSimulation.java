package com.rpg.combatsim.simulation;

import com.rpg.combatsim.domain.Combatant;
import com.rpg.combatsim.domain.Weapon;
import com.rpg.combatsim.utility.SimulationUtils;
import org.apache.log4j.Logger;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BasicTDMSimulation implements ISimulation {

    private static final Logger LOGGER = Logger.getLogger(BasicTDMSimulation.class);

    private Map<String, List<Combatant>> teamsMap;

    @Override
    public void handleDeath(List<Combatant> livingCombatantList, Combatant attacker, Map<Combatant, Map<Combatant, Integer>> threatMap) {
        Combatant deadCombatant = attacker.getCurrentTarget();
        LOGGER.info(deadCombatant.getName() + " Died!");
        //remove dead combatant from livingCombatantList
        livingCombatantList.remove(deadCombatant);

        //remove dead combatant from base threatMap and all individual threat maps
        threatMap.remove(deadCombatant);
        for (Map<Combatant, Integer> thisThreatMap : threatMap.values()) {
            thisThreatMap.remove(deadCombatant);
        }

        //remove dead combatant from teamsMap and mark team for teamsMap if they are last remaining
        String teamToRemove = null;
        for (Map.Entry<String, List<Combatant>> entry : this.teamsMap.entrySet()) {
            if (entry.getValue().contains(deadCombatant)) {
                if (entry.getValue().size() > 1) {
                    entry.getValue().remove(deadCombatant);
                } else {
                    teamToRemove = entry.getKey();
                }
            }
        }
        if(teamToRemove != null) {//remove team if marked for removal
            teamsMap.remove(teamToRemove);
        }

        //redirect any combatants targeting the dead combatant
        for (Combatant livingCombatant : livingCombatantList) {
            if (livingCombatant.getCurrentTarget().getName().equals(deadCombatant.getName())) {
                livingCombatant.setCurrentTarget(SimulationUtils.getMaxThreat(threatMap.get(livingCombatant)));
            }
        }
    }

    @Override
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
                String verb = myMainWeapon.getWeaponType().equals("Ranged") ? " shot " : " hit ";
                LOGGER.info(attacker.getName() + verb + myTarget.getName() + " with a "
                        + myMainWeapon.getName() + " for " + damage + " damage");

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
                LOGGER.info(attacker.getName() + " missed");
            }//end hit or miss block
        } else {
            myMainWeapon.setCurrentAmmo(myMainWeapon.getMaxAmmo());
            LOGGER.info(attacker.getName() + " reloaded");
        }//end gun loaded check
    }
    //endregion

    public void setRandomTeamsMap(List<Combatant> combatantList, int numTeams) {
        this.teamsMap = new HashMap<>();
        Random random = new Random();
        List<Combatant> randomList = new ArrayList<>(combatantList);
        Collections.shuffle(randomList);

        if (numTeams <= randomList.size()) {

            int teamSize = randomList.size() / numTeams;
            int remainder = randomList.size() - teamSize * numTeams;
            for (int i = 0; i < numTeams; i++) {
                String teamName = "Team " + (i + 1);
                List<Combatant> thisTeam = teamsMap.computeIfAbsent(teamName, k -> new ArrayList<>());
                for (int j = 0; j < teamSize; j++) {
                    thisTeam.add(randomList.get(i + teamSize * j));
                }
            }

            //deal with remainders
            for (int i = 0; i < remainder; i++) {
                int teamNum = random.nextInt(numTeams) + 1;
                String teamName = "Team " + teamNum;
                teamsMap.get(teamName).add(combatantList.get(teamSize * numTeams + i));
            }
        } else {
            throw new InvalidParameterException("numTeams must be less than or equal to number of combatants (combatantList.size())");
        }

        for (Map.Entry<String, List<Combatant>> entry : teamsMap.entrySet()) {
            LOGGER.info(entry.getKey() + ": " + Arrays.toString(entry.getValue().stream().map(Combatant::getName).toArray()));
        }
    }

    public void genTDMSim(List<Combatant> combatantList, int numTeams) {
        setRandomTeamsMap(combatantList, numTeams);
        Map<Combatant, Map<Combatant, Integer>> threatMap = SimulationUtils.genInitThreatMapTeams(combatantList, this.teamsMap);
        SimulationUtils.setInitTargetsRandomGeneral(combatantList, threatMap);
        List<Combatant> livingCombatantList = new ArrayList<>(combatantList);

        LOGGER.info("Weapon Mapping:");
        LOGGER.info(Arrays.toString(combatantList.stream().map(x -> x.getName() + ":" + x.getMainWeapon().getName()).toArray()));

        int roundNum = 1;

        LOGGER.info("Health: " + Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentHealth()).toArray()));
        LOGGER.info("Targets: " + Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentTarget().getName()).toArray()));

        //end condition should be only one team not empty, also remove dead combatants from team map
        while (this.teamsMap.size() > 1) {
            LOGGER.info("Round " + roundNum);

            List<Combatant> thisRoundTurnOrderList = SimulationUtils.getRoundOrderList(livingCombatantList);
            LOGGER.info("Turn Order: " + Arrays.toString(thisRoundTurnOrderList.stream().map(Combatant::getName).toArray()));

            for (Combatant combatant : thisRoundTurnOrderList) {
                if (combatant.getCurrentHealth() > 0 && combatant.getCurrentTarget() != null) {//death and available target check
                    takeTurn(livingCombatantList, combatant, threatMap);
                }
            }//end combatant turn loop

            LOGGER.info("End of Round");

            //update round
            roundNum++;
            if (teamsMap.size() > 1) {
                LOGGER.info("Health: " + Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentHealth()).toArray()));
                LOGGER.info("Targets: " + Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentTarget().getName()).toArray()));
            }
        }//end combat while loop

        //declare winner
        LOGGER.info(teamsMap.entrySet().iterator().next().getKey() + " Wins!");
    }

    @Override
    public void runSim(List<Combatant> combatantList) {
        genTDMSim(combatantList, 2);
    }
}
