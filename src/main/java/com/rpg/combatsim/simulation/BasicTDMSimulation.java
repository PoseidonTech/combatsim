package com.rpg.combatsim.simulation;

import com.rpg.combatsim.domain.Combatant;
import com.rpg.combatsim.domain.Weapon;
import com.rpg.combatsim.utility.SimulationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BasicTDMSimulation implements ISimulation {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicTDMSimulation.class);

    private Map<String, List<Combatant>> teamsMap;
    private Map<String, List<Combatant>> livingTeamsMap;

    //region ISimulation override methods
    @Override
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

        //remove dead combatant from teamsMap and mark team for teamsMap if they are last remaining
        String teamToRemove = null;
        for (Map.Entry<String, List<Combatant>> entry : this.livingTeamsMap.entrySet()) {
            if (entry.getValue().contains(deadCombatant)) {
                if (entry.getValue().size() > 1) {
                    entry.getValue().remove(deadCombatant);
                } else {
                    teamToRemove = entry.getKey();
                }
            }
        }
        if(teamToRemove != null) {//remove team if marked for removal
            livingTeamsMap.remove(teamToRemove);
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
                String verb = myMainWeapon.getWeaponType().equals("Ranged") ? "shot" : "hit";
                LOGGER.info("{} {} {} with a {} for {} damage", attacker.getName(), verb, myTarget.getName(),
                        myMainWeapon.getName(), damage);

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

    //instance methods
    public void setTeamsMap(Map<String, List<Combatant>> teamsMap) {
        this.teamsMap = teamsMap;
    }

    private Map<String, List<Combatant>> shallowCopyMyTeamsMap() {
        Map<String, List<Combatant>> shallowTeamsMapCopy = new HashMap<>();
        for(Map.Entry<String, List<Combatant>> entry : this.teamsMap.entrySet()) {
            shallowTeamsMapCopy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return shallowTeamsMapCopy;
    }
    //endregion

    public void genTDMSim(List<Combatant> combatantList) {
        this.livingTeamsMap = shallowCopyMyTeamsMap();
        Map<Combatant, Map<Combatant, Integer>> threatMap = SimulationUtils.genInitThreatMapTeams(combatantList, this.livingTeamsMap);
        SimulationUtils.setInitTargetsRandomGeneral(combatantList, threatMap);
        List<Combatant> livingCombatantList = new ArrayList<>(combatantList);

        //log printoff of teams
        for (Map.Entry<String, List<Combatant>> entry : this.livingTeamsMap.entrySet()) {
            LOGGER.info("{}", entry.getKey() + ": " + Arrays.toString(entry.getValue().stream().map(Combatant::getName).toArray()));
        }

        LOGGER.info("Weapon Mapping:");
        LOGGER.info("{}", Arrays.toString(combatantList.stream().map(x -> x.getName() + ":" + x.getMainWeapon().getName()).toArray()));

        int roundNum = 1;

        LOGGER.info("Health: {}", Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentHealth()).toArray()));
        LOGGER.info("Targets: {}", Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentTarget().getName()).toArray()));

        //end condition should be only one team not empty, also remove dead combatants from team map
        while (this.livingTeamsMap.size() > 1) {
            LOGGER.info("Round {}", roundNum);

            List<Combatant> thisRoundTurnOrderList = SimulationUtils.getRoundOrderList(livingCombatantList);
            LOGGER.info("Turn Order: {}", Arrays.toString(thisRoundTurnOrderList.stream().map(Combatant::getName).toArray()));

            for (Combatant combatant : thisRoundTurnOrderList) {
                if (combatant.getCurrentHealth() > 0 && combatant.getCurrentTarget() != null) {//death and available target check
                    takeTurn(livingCombatantList, combatant, threatMap);
                }
            }//end combatant turn loop

            LOGGER.info("End of Round");

            //update round
            roundNum++;
            if (livingTeamsMap.size() > 1) {
                LOGGER.info("Health: {}", Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentHealth()).toArray()));
                LOGGER.info("Targets: {}", Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentTarget().getName()).toArray()));
            }
        }//end combat while loop

        //declare winner
        LOGGER.info("{} Wins!", livingTeamsMap.entrySet().iterator().next().getKey());
    }

    @Override
    public void runSim(List<Combatant> combatantList) {
        genTDMSim(combatantList);
    }

    //region static methods
    public static Map<String, List<Combatant>> generateRandomTeamsMap(List<Combatant> combatantList, int numTeams) {
        Map<String, List<Combatant>> randomTeamsMap = new HashMap<>();
        Random random = new Random();
        List<Combatant> randomList = new ArrayList<>(combatantList);
        Collections.shuffle(randomList);

        if (numTeams <= randomList.size()) {

            int teamSize = randomList.size() / numTeams;
            int remainder = randomList.size() - teamSize * numTeams;
            for (int i = 0; i < numTeams; i++) {
                String teamName = "Team " + (i + 1);
                List<Combatant> thisTeam = randomTeamsMap.computeIfAbsent(teamName, k -> new ArrayList<>());
                for (int j = 0; j < teamSize; j++) {
                    thisTeam.add(randomList.get(i + numTeams * j));
                }
            }

            //deal with remainders
            for (int i = 0; i < remainder; i++) {
                int teamNum = random.nextInt(numTeams) + 1;
                String teamName = "Team " + teamNum;
                randomTeamsMap.get(teamName).add(randomList.get(teamSize * numTeams + i));
            }
        } else {
            throw new InvalidParameterException("numTeams must be less than or equal to number of combatants (combatantList.size())");
        }

        return randomTeamsMap;
    }
    //endregion
}
