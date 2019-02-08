package com.rpg.combatsim.utility;

import com.rpg.combatsim.domain.Combatant;

import java.util.ArrayList;
import java.util.List;

public class CombatantGenerationUtils {

    private CombatantGenerationUtils() {//private constructor to hide default public
    }

    public static List<Combatant> generateBaseLindaList() {
        //create combatants
        //  start with 10 stat points, Strength Perception Endurance Agility
        Combatant arron = new Combatant.Builder()//5 1 3 1
                .setName("Arron").setStrength(5).setPerception(1).setEndurance(3).setAgility(1).build();
        Combatant lucas = new Combatant.Builder()//3 2 2 3
                .setName("Lucas").setStrength(3).setPerception(2).setEndurance(2).setAgility(3).build();
        Combatant brad = new Combatant.Builder()//3 2 3 2
                .setName("Brad").setStrength(3).setPerception(2).setEndurance(3).setAgility(2).build();
        Combatant linda = new Combatant.Builder()//2 1 6 1
                .setName("Linda").setStrength(2).setPerception(1).setEndurance(6).setAgility(1).build();

        //create and fill combatantList
        List<Combatant> combatantList = new ArrayList<>();
        combatantList.add(arron);
        combatantList.add(lucas);
        combatantList.add(brad);
        combatantList.add(linda);

        return combatantList;
    }

    public static List<Combatant> generateBaseEnemiesList() {
        //create combatants
        //  start with 10 stat points, Strength Perception Endurance Agility
        Combatant officerZink = new Combatant.Builder()//5 2 2 1
                .setName("Officer Zink").setStrength(5).setPerception(2).setEndurance(2).setAgility(1).build();
        Combatant chrisDad = new Combatant.Builder()//2 2 2 4
                .setName("Chris' Dad").setStrength(2).setPerception(2).setEndurance(2).setAgility(4).build();
        Combatant pam = new Combatant.Builder()//2 1 1 6
                .setName("Pam").setStrength(2).setPerception(1).setEndurance(1).setAgility(6).build();
        Combatant christine = new Combatant.Builder()//1 4 4 2
                .setName("Christine").setStrength(1).setPerception(4).setEndurance(4).setAgility(2).build();

        //create and fill combatantList
        List<Combatant> combatantList = new ArrayList<>();
        combatantList.add(officerZink);
        combatantList.add(chrisDad);
        combatantList.add(pam);
        combatantList.add(christine);

        return combatantList;
    }
}
