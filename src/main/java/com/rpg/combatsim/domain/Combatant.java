package com.rpg.combatsim.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name = "combatant")
@XmlAccessorType(XmlAccessType.FIELD)
public class Combatant {

    private Integer combatantId;
    private String name;
    private Integer strength;   //melee damage
    private Integer perception; //accuracy
    private Integer endurance;  //health
    private Integer agility;    //speed

    private Weapon mainWeapon;
    private Integer currentHealth;
    private Combatant currentTarget;

    public Combatant() {//default constructor
    }

    private Combatant(String name, Integer strength, Integer perception, Integer endurance, Integer agility, Weapon mainWeapon) {
        this.name = name;
        this.strength = strength;
        this.perception = perception;
        this.endurance = endurance;
        this.agility = agility;
        this.mainWeapon = mainWeapon;
        this.currentHealth = 10 + (endurance * 2);
    }

    public Integer getCombatantId() {
        return combatantId;
    }
    public void setCombatantId(Integer combatantId) {
        this.combatantId = combatantId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getStrength() {
        return strength;
    }
    public void setStrength(Integer strength) {
        this.strength = strength;
    }
    public Integer getPerception() {
        return perception;
    }
    public void setPerception(Integer perception) {
        this.perception = perception;
    }
    public Integer getEndurance() {
        return endurance;
    }
    public void setEndurance(Integer endurance) {
        this.endurance = endurance;
    }
    public Integer getAgility() {
        return agility;
    }
    public void setAgility(Integer agility) {
        this.agility = agility;
    }
    public Weapon getMainWeapon() {
        return this.mainWeapon;
    }
    public void setMainWeapon(Weapon mainWeapon) {
        this.mainWeapon = mainWeapon;
    }
    public Integer getCurrentHealth() {
        return currentHealth;
    }
    public void setCurrentHealth(Integer currentHealth) {
        this.currentHealth = currentHealth;
    }
    public Combatant getCurrentTarget() {
        return currentTarget;
    }
    public void setCurrentTarget(Combatant currentTarget) {
        this.currentTarget = currentTarget;
    }

    //utility methods
    public Integer getMaxHealth() {
        return 10 + (endurance * 2);
    }

    public boolean takeDamage(Integer damage) {
        this.currentHealth -= damage;
        return this.currentHealth < 1;
    }

    //builder
    public static class Builder {
        private String name;
        private Integer strength;
        private Integer perception;
        private Integer endurance;
        private Integer agility;
        private Weapon mainWeapon;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setStrength(Integer strength) {
            this.strength = strength;
            return this;
        }

        public Builder setPerception(Integer perception) {
            this.perception = perception;
            return this;
        }

        public Builder setEndurance(Integer endurance) {
            this.endurance = endurance;
            return this;
        }

        public Builder setAgility(Integer agility) {
            this.agility = agility;
            return this;
        }

        public Builder setMainWeapon(Weapon mainWeapon) {
            this.mainWeapon = mainWeapon;
            return this;
        }

        public Combatant build() {
            return new Combatant(this.name, this.strength, this.perception, this.endurance, this.agility, this.mainWeapon);
        }

        public Builder() {//default constructor
        }
    }

    @Override
    public String toString() {
        return "Combatant{" +
                "combatantId=" + combatantId +
                ", name='" + name + '\'' +
                ", strength=" + strength +
                ", perception=" + perception +
                ", endurance=" + endurance +
                ", agility=" + agility +
                ", mainWeapon=" + mainWeapon +
                ", currentHealth=" + currentHealth +
                ", currentTarget=" + currentTarget +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Combatant combatant = (Combatant) o;
        return name.equals(combatant.name) &&
                strength.equals(combatant.strength) &&
                perception.equals(combatant.perception) &&
                endurance.equals(combatant.endurance) &&
                agility.equals(combatant.agility);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, strength, perception, endurance, agility);
    }
}
