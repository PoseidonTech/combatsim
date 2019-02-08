package com.rpg.combatsim.utility;

import com.rpg.combatsim.domain.Weapon;

import java.util.ArrayList;
import java.util.List;

public class WeaponGenerationUtils {

    public static final String MELEE = "Melee";
    public static final String RANGED = "Ranged";

    private WeaponGenerationUtils() {//private constructor to hide default public
    }

    public static List<Weapon> generateBaseLindaWeaponList() {
        Weapon bareFist = new Weapon.Builder()
                .setName("Bare Fist").setWeaponType("Unarmed").setDiceToRoll(1).setDiceToKeep(1).setAddToKeepSum(2).setMaxAmmo(-1).build();
        Weapon kitchenKnife = new Weapon.Builder()
                .setName("Kitchen Knife").setWeaponType(MELEE).setDiceToRoll(2).setDiceToKeep(1).setAddToKeepSum(1).setMaxAmmo(-1).build();
        Weapon baretta = new Weapon.Builder()
                .setName("Baretta 9mm").setWeaponType(RANGED).setDiceToRoll(2).setDiceToKeep(1).setAddToKeepSum(3).setMaxAmmo(12).build();
        Weapon doubleBarrelShotgun = new Weapon.Builder()
                .setName("Double-Barrel Shotgun").setWeaponType(RANGED).setDiceToRoll(3).setDiceToKeep(2).setAddToKeepSum(2).setMaxAmmo(2).build();

        List<Weapon> weaponList = new ArrayList<>();
        weaponList.add(bareFist);
        weaponList.add(kitchenKnife);
        weaponList.add(baretta);
        weaponList.add(doubleBarrelShotgun);

        return weaponList;
    }

    public static List<Weapon> generateBaseLindaCombinedWeaponList() {
        Weapon bareFist = new Weapon.Builder()
                .setName("Bare Fist").setWeaponType("Unarmed").setDiceToRoll(1).setDiceToKeep(1).setAddToKeepSum(2).setMaxAmmo(-1).build();
        Weapon kitchenKnife = new Weapon.Builder()
                .setName("Kitchen Knife").setWeaponType(MELEE).setDiceToRoll(2).setDiceToKeep(1).setAddToKeepSum(1).setMaxAmmo(-1).build();
        Weapon baretta = new Weapon.Builder()
                .setName("Baretta 9mm").setWeaponType(RANGED).setDiceToRoll(2).setDiceToKeep(1).setAddToKeepSum(3).setMaxAmmo(12).build();
        Weapon doubleBarrelShotgun = new Weapon.Builder()
                .setName("Double-Barrel Shotgun").setWeaponType(RANGED).setDiceToRoll(3).setDiceToKeep(2).setAddToKeepSum(2).setMaxAmmo(2).build();

        Weapon plunger = new Weapon.Builder()
                .setName("Plunger").setWeaponType(MELEE).setDiceToRoll(2).setDiceToKeep(1).setAddToKeepSum(1).setMaxAmmo(-1).build();
        Weapon brokenCrackPipe = new Weapon.Builder()
                .setName("Broken Crack Pipe").setWeaponType(MELEE).setDiceToRoll(2).setDiceToKeep(1).setAddToKeepSum(2).setMaxAmmo(-1).build();
        Weapon airsoftPistol = new Weapon.Builder()
                .setName("Airsoft Pistol").setWeaponType(RANGED).setDiceToRoll(1).setDiceToKeep(1).setAddToKeepSum(3).setMaxAmmo(6).build();
        Weapon huntingRifle = new Weapon.Builder()
                .setName("Hunting Rifle").setWeaponType(RANGED).setDiceToRoll(2).setDiceToKeep(2).setAddToKeepSum(2).setMaxAmmo(4).build();

        List<Weapon> weaponList = new ArrayList<>();
        weaponList.add(bareFist);
        weaponList.add(kitchenKnife);
        weaponList.add(baretta);
        weaponList.add(doubleBarrelShotgun);
        weaponList.add(plunger);
        weaponList.add(brokenCrackPipe);
        weaponList.add(airsoftPistol);
        weaponList.add(huntingRifle);

        return weaponList;
    }
}
