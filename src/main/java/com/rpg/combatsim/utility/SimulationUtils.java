package com.rpg.combatsim.utility;

import com.rpg.combatsim.domain.Combatant;
import com.rpg.combatsim.domain.Weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class SimulationUtils {

    private SimulationUtils() {//private constructor to hide default public
    }

    //region general utility methods
    public static List<Integer> rollDice(int numSides, int numRolls) {
        List<Integer> rollList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numRolls; i++) {
            rollList.add(random.nextInt(numSides) + 1);
        }
        return rollList;
    }

    public static boolean hitRoll(Combatant attacker) {
        List<Integer> rollList = rollDice(6, 2);
        Integer numToHit = (attacker.getCurrentTarget().getAgility() / 2) + rollList.get(0);
        Integer hitRoll = attacker.getPerception() + rollList.get(1);

        Weapon weapon = attacker.getMainWeapon();
        if (weapon.getWeaponType().equals("Ranged")) {
            weapon.setCurrentAmmo(weapon.getCurrentAmmo() - 1);
        }

        return hitRoll > numToHit;
    }

    public static Integer damageRoll(Combatant combatant) {
        Weapon weapon = combatant.getMainWeapon();
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

    public static List<Combatant> getRoundOrderList(List<Combatant> combatantList) {
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
        //endregion

        //sort initiative rolls
        List<Integer> sortedRollList = initiativeRollMap.keySet().stream()
                .sorted(Comparator.comparingInt(Integer::intValue).reversed()).collect(Collectors.toList());

        //create sorted combatant list from sorted initiative roll list
        List<Combatant> sortedCombatantList = new ArrayList<>();
        for (Integer integer : sortedRollList) {
            sortedCombatantList.addAll(initiativeRollMap.get(integer));
        }

        return sortedCombatantList;
    }

    //region threat map generators
    public static Map<Combatant, Map<Combatant, Integer>> genInitThreatMapFFA(List<Combatant> combatantList) {
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

    public static Map<Combatant, Map<Combatant, Integer>> genInitThreatMapTeams(List<Combatant> combatantList, Map<String, List<Combatant>> teamsMap) {
        Map<Combatant, Map<Combatant, Integer>> initialThreatMap = new HashMap<>();
        for (Combatant combatant : combatantList) {
            String team = teamsMap.entrySet().stream().filter(x -> x.getValue().contains(combatant)).collect(Collectors.toList()).get(0).getKey();
            Map<Combatant, Integer> myThreatMap = new HashMap<>();
            for (Map.Entry<String, List<Combatant>> entry : teamsMap.entrySet()) {
                if (!entry.getKey().equals(team)) {
                    for (Combatant otherCombatant : entry.getValue()) {
                        myThreatMap.put(otherCombatant, 0);
                    }
                }
            }
            initialThreatMap.put(combatant, myThreatMap);
        }
        return initialThreatMap;
    }
    //endregion

    //region targeting utils
    public static void setInitTargetsRandom(List<Combatant> combatantList) {
        Random random = new Random();
        for (Combatant combatant : combatantList) {
            Combatant otherCombatant = combatantList.get(random.nextInt(combatantList.size()));
            while (otherCombatant.getName().equals(combatant.getName())) {
                otherCombatant = combatantList.get(random.nextInt(combatantList.size()));
            }
            combatant.setCurrentTarget(otherCombatant);
        }
    }

    public static void setInitTargetsRandomGeneral(List<Combatant> combatantList, Map<Combatant, Map<Combatant, Integer>> threatMap) {
        Random random = new Random();
        for (Combatant combatant : combatantList) {
            Map<Combatant, Integer> myThreatMap = threatMap.get(combatant);
            Iterator<Map.Entry<Combatant, Integer>> targetIterator = myThreatMap.entrySet().iterator();
            int targetIndex = random.nextInt(myThreatMap.size());
            int i = 0;
            while(targetIterator.hasNext()) {
                Combatant target = targetIterator.next().getKey();
                if(i == targetIndex) {
                    combatant.setCurrentTarget(target);
                    break;
                } else {
                    i++;
                }
            }
        }
    }

    public static Combatant getMaxThreat(Map<Combatant, Integer> threatMap) {
        List<Combatant> biggestThreatList = new ArrayList<>();
        Integer maxThreat = Integer.MIN_VALUE;
        for (Map.Entry<Combatant, Integer> entry : threatMap.entrySet()) {
            if (entry.getValue() > maxThreat) {
                biggestThreatList.clear();
                maxThreat = entry.getValue();
                biggestThreatList.add(entry.getKey());
            } else if (entry.getValue().equals(maxThreat)) {
                biggestThreatList.add(entry.getKey());
            }
        }
        Collections.shuffle(biggestThreatList);
        return !biggestThreatList.isEmpty() ? biggestThreatList.get(0) : null;
    }
    //endregion

    //region pre-round methods
    public static void grabRandomWeapons(List<Combatant> combatantList, List<Weapon> weaponList) {
        Collections.shuffle(weaponList);

        //assign random weapons
        for (int i = 0; i < combatantList.size(); i++) {
            combatantList.get(i).setMainWeapon(weaponList.get(i));
        }
    }

    public static void unassignWeapons(List<Combatant> combatantList) {
        for (Combatant combatant : combatantList) {
            combatant.setMainWeapon(null);
        }
    }
    //endregion

    //region post-round methods
    public static void resetHealthAndReloadWeapons(List<Combatant> combatantList) {
        for(Combatant combatant : combatantList) {
            combatant.setCurrentHealth(combatant.getMaxHealth());
            Weapon weapon = combatant.getMainWeapon();
            weapon.setCurrentAmmo(weapon.getMaxAmmo());
        }
    }
    //endregion
}
