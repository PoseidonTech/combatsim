package com.rpg.combatsim.utility;

import com.rpg.combatsim.domain.Combatant;
import com.rpg.combatsim.domain.Weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CombatantGenerationUtils {

    public static List<Combatant> generateBaseLindaList() {

        //create weapons
        Weapon bareFist = new Weapon.Builder()
                .setName("Bare Fist").setWeaponType("Unarmed").setDiceToRoll(1).setDiceToKeep(1).setAddToKeepSum(2).setMaxAmmo(-1).build();
        Weapon kitchenKnife = new Weapon.Builder()
                .setName("Kitchen Knife").setWeaponType("Melee").setDiceToRoll(2).setDiceToKeep(1).setAddToKeepSum(1).setMaxAmmo(-1).build();
        Weapon baretta = new Weapon.Builder()
                .setName("Baretta 9mm").setWeaponType("Ranged").setDiceToRoll(2).setDiceToKeep(1).setAddToKeepSum(3).setMaxAmmo(12).build();
        Weapon doubleBarrelShotgun = new Weapon.Builder()
                .setName("Double-Barrel Shotgun").setWeaponType("Ranged").setDiceToRoll(3).setDiceToKeep(2).setAddToKeepSum(2).setMaxAmmo(2).build();

        //create combatants
        //  start with 10 stat points, Strength Perception Endurance Agility
        Combatant arron = new Combatant.Builder()//5 1 3 1
                .setName("Arron").setStrength(5).setPerception(1).setEndurance(3).setAgility(1).build();
        Combatant lucas = new Combatant.Builder()//2 2 2 4
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

        //create and fill weaponList
        List<Weapon> weaponList = new ArrayList<>();
        weaponList.add(bareFist);
        weaponList.add(kitchenKnife);
        weaponList.add(baretta);
        weaponList.add(doubleBarrelShotgun);
        Collections.shuffle(weaponList);

        //assign random weapons
        for (int i = 0; i < combatantList.size(); i++) {
            combatantList.get(i).setMainWeapon(weaponList.get(i));
        }

        return combatantList;
    }
}
