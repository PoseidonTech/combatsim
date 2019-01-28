package com.rpg.combatsim.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Weapon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "weapon_id", columnDefinition = "BIGINT(20)")
    private Integer weaponId;

    @Column
    private String name;

    @Column(name = "weapon_type")
    private String weaponType;

    @Column(name = "dice_to_roll")
    private Integer diceToRoll;

    @Column(name = "dice_to_keep")
    private Integer diceToKeep;

    @Column(name = "add_to_keep_sum")
    private Integer addToKeepSum;

    @NotNull
    @Column(name = "max_ammo", nullable = false)
    private Integer maxAmmo;

    private Integer currentAmmo;

    public Weapon() {//default constructor
    }

    private Weapon(String name, String weaponType, Integer diceToRoll, Integer diceToKeep, Integer addToKeepSum, Integer maxAmmo) {
        this.name = name;
        this.weaponType = weaponType;
        this.diceToRoll = diceToRoll;
        this.diceToKeep = diceToKeep;
        this.addToKeepSum = addToKeepSum;
        this.maxAmmo = maxAmmo;
        this.currentAmmo = maxAmmo;
    }

    public Integer getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(Integer weaponId) {
        this.weaponId = weaponId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(String weaponType) {
        this.weaponType = weaponType;
    }

    public Integer getDiceToRoll() {
        return diceToRoll;
    }

    public void setDiceToRoll(Integer diceToRoll) {
        this.diceToRoll = diceToRoll;
    }

    public Integer getDiceToKeep() {
        return diceToKeep;
    }

    public void setDiceToKeep(Integer diceToKeep) {
        this.diceToKeep = diceToKeep;
    }

    public Integer getAddToKeepSum() {
        return addToKeepSum;
    }

    public void setAddToKeepSum(Integer addToKeepSum) {
        this.addToKeepSum = addToKeepSum;
    }

    public Integer getMaxAmmo() {
        return maxAmmo;
    }

    public void setMaxAmmo(Integer maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    public Integer getCurrentAmmo() {
        return currentAmmo;
    }

    public void setCurrentAmmo(Integer currentAmmo) {
        this.currentAmmo = currentAmmo;
    }

    //builder
    public static class Builder {
        private String name;
        private String weaponType;
        private Integer diceToRoll;
        private Integer diceToKeep;
        private Integer addToKeepSum;
        private Integer maxAmmo;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setWeaponType(String weaponType) {
            this.weaponType = weaponType;
            return this;
        }

        public Builder setDiceToRoll(Integer diceToRoll) {
            this.diceToRoll = diceToRoll;
            return this;
        }

        public Builder setDiceToKeep(Integer diceToKeep) {
            this.diceToKeep = diceToKeep;
            return this;
        }

        public Builder setAddToKeepSum(Integer addToKeepSum) {
            this.addToKeepSum = addToKeepSum;
            return this;
        }

        public Builder setMaxAmmo(Integer maxAmmo) {
            this.maxAmmo = maxAmmo;
            return this;
        }

        public Builder() {//default constructor
        }

        public Weapon build() {
            return new Weapon(this.name, this.weaponType, this.diceToRoll, this.diceToKeep, this.addToKeepSum, this.maxAmmo);
        }
    }
}
