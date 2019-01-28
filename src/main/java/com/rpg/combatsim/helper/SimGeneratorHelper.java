package com.rpg.combatsim.helper;

import com.rpg.combatsim.domain.Combatant;
import com.rpg.combatsim.domain.Weapon;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class SimGeneratorHelper {

    public static final Logger LOGGER = Logger.getLogger(SimGeneratorHelper.class);

    public SimGeneratorHelper() {//default constructor
    }

    //region Generate Combat List methods
    public List<Combatant> generateBaseLindaList() {
        List<Combatant> combatantList = new ArrayList<>();

        Weapon bareFist = new Weapon.Builder()
                .setName("Bare Fist").setWeaponType("Unarmed").setDiceToRoll(1).setDiceToKeep(1).setAddToKeepSum(2).setMaxAmmo(-1).build();
        Weapon kitchenKnife = new Weapon.Builder()
                .setName("Kitchen Knife").setWeaponType("Melee").setDiceToRoll(2).setDiceToKeep(1).setAddToKeepSum(1).setMaxAmmo(-1).build();
        Weapon baretta = new Weapon.Builder()
                .setName("Baretta 9mm").setWeaponType("Ranged").setDiceToRoll(2).setDiceToKeep(1).setAddToKeepSum(3).setMaxAmmo(12).build();
        Weapon doubleBarrelShotgun = new Weapon.Builder()
                .setName("Double-Barrel Shotgun").setWeaponType("Ranged").setDiceToRoll(3).setDiceToKeep(2).setAddToKeepSum(2).setMaxAmmo(2).build();

        //start with 10 stat points, Strength Perception Endurance Agility
        Combatant arron = new Combatant.Builder()//5 1 3 1
                .setName("Arron").setStrength(5).setPerception(1).setEndurance(3).setAgility(1).build();
        Combatant lucas = new Combatant.Builder()//2 2 2 4
                .setName("Lucas").setStrength(3).setPerception(2).setEndurance(2).setAgility(3).build();
        Combatant brad = new Combatant.Builder()//3 2 3 2
                .setName("Brad").setStrength(3).setPerception(2).setEndurance(3).setAgility(2).build();
        Combatant linda = new Combatant.Builder()//2 1 6 1
                .setName("Linda").setStrength(2).setPerception(1).setEndurance(6).setAgility(1).build();

        combatantList.add(arron);
        combatantList.add(lucas);
        combatantList.add(brad);
        combatantList.add(linda);

        List<Weapon> weaponList = new ArrayList<>();
        weaponList.add(bareFist);
        weaponList.add(kitchenKnife);
        weaponList.add(baretta);
        weaponList.add(doubleBarrelShotgun);
        Collections.shuffle(weaponList);

        for (int i = 0; i < combatantList.size(); i++) {
            combatantList.get(i).setMainWeaponDirect(weaponList.get(i));
        }

        LOGGER.info("Weapon Mapping:");
        LOGGER.info(Arrays.toString(combatantList.stream().map(x -> x.getName() + ":" + x.getMainWeaponDirect().getName()).toArray()));

        return combatantList;
    }
    //endregion

    //region Simulation Utility Methods
    public List<Integer> rollDice(int numSides, int numRolls) {
        List<Integer> rollList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numRolls; i++) {
            rollList.add(random.nextInt(numSides) + 1);
        }
        return rollList;
    }

    public boolean hitRoll(Combatant attacker, Combatant defender) {
        List<Integer> rollList = rollDice(6, 2);
        Integer numToHit = (defender.getAgility() / 2) + rollList.get(0);
        Integer hitRoll = attacker.getPerception() + rollList.get(1);

        Weapon weapon = attacker.getMainWeaponDirect();
        if(weapon.getWeaponType().equals("Ranged")) {
            weapon.setCurrentAmmo(weapon.getCurrentAmmo() - 1);
        }

        return hitRoll > numToHit;
    }

    public Integer damageRoll(Combatant combatant) {
        Weapon weapon = combatant.getMainWeaponDirect();
        List<Integer> sortedRollList = rollDice(6, weapon.getDiceToRoll()).stream().sorted(Comparator.comparingInt(Integer::intValue).reversed()).collect(Collectors.toList());

        Integer damage = weapon.getAddToKeepSum();
        for (int i = 0; i < weapon.getDiceToKeep(); i++) {
            damage += sortedRollList.get(i);
        }

        //handle unarmed/melee case
        if (!weapon.getWeaponType().equals("Ranged")) {
            damage += combatant.getStrength();
        }

        return damage;
    }

    public Map<Combatant, Map<Combatant, Integer>> genInitThreatMap(List<Combatant> combatantList) {
        Map<Combatant, Map<Combatant, Integer>> initialThreatMap = new HashMap<>();

        for (Combatant combatant : combatantList) {
            Map<Combatant, Integer> myThreatMap = new HashMap<>();
            for (Combatant otherCombatant : combatantList) {
                if (!combatant.getName().equals(otherCombatant.getName())) {
                    myThreatMap.put(otherCombatant, 0);
                }
            }
            initialThreatMap.put(combatant, myThreatMap);
        }
        return initialThreatMap;
    }

    public void setInitTargetsRandom(List<Combatant> combatantList) {
        Random random = new Random();
        for (Combatant combatant : combatantList) {
            Combatant otherCombatant = combatantList.get(random.nextInt(combatantList.size()));
            while (otherCombatant.getName().equals(combatant.getName())) {
                otherCombatant = combatantList.get(random.nextInt(combatantList.size()));
            }
            combatant.setCurrentTarget(otherCombatant);
        }
    }

    public List<Combatant> getRoundOrderList(List<Combatant> combatantList) {
        Map<Integer, List<Combatant>> initiativeRollMap = new HashMap<>();
        //get base dice rolls for each combatant
        List<Integer> rollList = rollDice(6, combatantList.size());

        //calculate initiative rolls for each combatant and map values to combatants
        for (int i = 0; i < combatantList.size(); i++) {
            Integer initiativeRoll = combatantList.get(i).getAgility() + rollList.get(i);
            if (initiativeRollMap.get(initiativeRoll) == null) {
                List<Combatant> thisRollCombatantList = new ArrayList<>();
                thisRollCombatantList.add(combatantList.get(i));
                initiativeRollMap.put(initiativeRoll, thisRollCombatantList);
            } else {
                initiativeRollMap.get(initiativeRoll).add(combatantList.get(i));
            }
        }

        //sort initiative rolls
        List<Integer> sortedRollList = initiativeRollMap.entrySet().stream().map(Map.Entry::getKey)
                .sorted(Comparator.comparingInt(Integer::intValue).reversed()).collect(Collectors.toList());

        //create sorted combatant list from sorted initiative roll list
        List<Combatant> sortedCombatantList = new ArrayList<>();
        for (Integer integer : sortedRollList) {
            sortedCombatantList.addAll(initiativeRollMap.get(integer));
        }

        return sortedCombatantList;
    }

    public Combatant getMaxThreat(Map<Combatant, Integer> threatMap) {
        List<Combatant> biggestThreatList = new ArrayList<>();
        Integer maxThreat = Integer.MIN_VALUE;
        for (Map.Entry<Combatant, Integer> entry : threatMap.entrySet()) {
            if (entry.getValue() > maxThreat) {
                biggestThreatList.clear();
                maxThreat = entry.getValue();
                biggestThreatList.add(entry.getKey());
            } else if(entry.getValue().equals(maxThreat)) {
                biggestThreatList.add(entry.getKey());
            }
        }
        Collections.shuffle(biggestThreatList);
        return !biggestThreatList.isEmpty() ? biggestThreatList.get(0) : null;
    }
    //endregion

    public void genFFASim(List<Combatant> combatantList) {
        Map<Combatant, Map<Combatant, Integer>> threatMap = genInitThreatMap(combatantList);
        setInitTargetsRandom(combatantList);
        List<Combatant> livingCombatantList = new ArrayList<>(combatantList);

        int roundNum = 1;

        LOGGER.info("Health: " + Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentHealth()).toArray()));
        LOGGER.info("Targets: " + Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentTarget().getName()).toArray()));

        while (livingCombatantList.size() > 1) {
            LOGGER.info("Round " + roundNum);

            List<Combatant> thisRoundTurnOrderList = getRoundOrderList(livingCombatantList);
            LOGGER.info("Turn Order: " + Arrays.toString(thisRoundTurnOrderList.stream().map(Combatant::getName).toArray()));

            for (Combatant combatant : thisRoundTurnOrderList) {
                if(combatant.getCurrentHealth() > 0) {//death check

                    if (combatant.getMainWeaponDirect().getCurrentAmmo() == -1 || combatant.getMainWeaponDirect().getCurrentAmmo() > 0) {
                        //hit roll
                        boolean hit = hitRoll(combatant, combatant.getCurrentTarget());
                        if (hit) {
                            //damage roll
                            Integer damage = damageRoll(combatant);

                            //take damage
                            boolean targetDied = combatant.getCurrentTarget().takeDamage(damage);
                            String verb = combatant.getMainWeaponDirect().getWeaponType().equals("Ranged") ? " shot " : " hit ";
                            LOGGER.info(combatant.getName() + verb + combatant.getCurrentTarget().getName() + " with a "
                                    + combatant.getMainWeaponDirect().getName() + " for " + damage + " damage");

                            //update threat
                            Integer oldThreat = threatMap.get(combatant.getCurrentTarget()).get(combatant);
                            threatMap.get(combatant.getCurrentTarget()).put(combatant, oldThreat + damage);

                            //handle death
                            if (targetDied) {
                                Combatant deadCombatant = combatant.getCurrentTarget();
                                LOGGER.info(deadCombatant.getName() + " Died!");
                                //remove dead combatant from livingCombatantList
                                livingCombatantList.remove(deadCombatant);

                                //remove dead combatant from base threatMap and all individual threat maps
                                threatMap.remove(combatant.getCurrentTarget());
                                for (Map<Combatant, Integer> thisThreatMap : threatMap.values()) {
                                    thisThreatMap.remove(deadCombatant);
                                }

                                //redirect any combatants targeting the dead combatant
                                for (Combatant livingCombatant : livingCombatantList) {
                                    if (livingCombatant.getCurrentTarget().getName().equals(deadCombatant.getName())) {
                                        livingCombatant.setCurrentTarget(getMaxThreat(threatMap.get(livingCombatant)));
                                    }
//                                    if(livingCombatantList.size() > 1) {
//                                        LOGGER.info("Targets: " + Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentTarget().getName()).toArray()));
//                                        if(livingCombatant.getCurrentTarget().getName().equals(deadCombatant.getName())) {
//                                            Map<Combatant, Integer> lcThreatMap = threatMap.get(livingCombatant);
//                                            Combatant wtf = getMaxThreat(lcThreatMap);
//                                            livingCombatant.setCurrentTarget(wtf);
//                                        }
//                                    }
                                }
                            }
                        } else {
                            LOGGER.info(combatant.getName() + " missed");
                        }//end hit or miss block
                    } else {
                        combatant.getMainWeaponDirect().setCurrentAmmo(combatant.getMainWeaponDirect().getMaxAmmo());
                        LOGGER.info(combatant.getName() + " reloaded");
                    }
                }//end current combatant alive check
            }//end combatant turn loop

            //update targets
            for (Combatant livingCombatant : livingCombatantList) {
                livingCombatant.setCurrentTarget(getMaxThreat(threatMap.get(livingCombatant)));
            }

            LOGGER.info("End of Round");

            //update round
            roundNum++;
            if(livingCombatantList.size() > 1) {
                LOGGER.info("Health: " + Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentHealth()).toArray()));
                LOGGER.info("Targets: " + Arrays.toString(livingCombatantList.stream().map(x -> x.getName() + ":" + x.getCurrentTarget().getName()).toArray()));
            }
        }//end while loop

        //declare winner
        LOGGER.info(livingCombatantList.get(0).getName() + " Wins!");
    }
}
