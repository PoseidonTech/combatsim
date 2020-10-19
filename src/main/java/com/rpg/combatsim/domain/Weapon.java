package com.rpg.combatsim.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name = "weapon")
@XmlAccessorType(XmlAccessType.FIELD)
public class Weapon {

    private Integer weaponId;
    private String name;
    private String weaponType;
    private Integer diceToRoll;
    private Integer diceToKeep;
    private Integer addToKeepSum;
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

    @Override
    public String toString() {
        return "Weapon{" +
                "weaponId=" + weaponId +
                ", name='" + name + '\'' +
                ", weaponType='" + weaponType + '\'' +
                ", diceToRoll=" + diceToRoll +
                ", diceToKeep=" + diceToKeep +
                ", addToKeepSum=" + addToKeepSum +
                ", maxAmmo=" + maxAmmo +
                ", currentAmmo=" + currentAmmo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weapon weapon = (Weapon) o;
        return name.equals(weapon.name) &&
                weaponType.equals(weapon.weaponType) &&
                diceToRoll.equals(weapon.diceToRoll) &&
                diceToKeep.equals(weapon.diceToKeep) &&
                addToKeepSum.equals(weapon.addToKeepSum) &&
                maxAmmo.equals(weapon.maxAmmo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weaponType, diceToRoll, diceToKeep, addToKeepSum, maxAmmo);
    }
}
