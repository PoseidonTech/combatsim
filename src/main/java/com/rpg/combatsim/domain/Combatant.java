package com.rpg.combatsim.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "combatant")
public class Combatant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "combatant_id", columnDefinition = "BIGINT(20)")
    private Integer combatantId;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Integer strength;   //melee damage

    @NotNull
    @Column(nullable = false)
    private Integer perception; //accuracy

    @NotNull
    @Column(nullable = false)
    private Integer endurance;  //health

    @NotNull
    @Column(nullable = false)
    private Integer agility;    //speed

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "main_weapon_id", referencedColumnName = "weapon_id", columnDefinition = "BIGINT(20)")
    private Weapon mainWeapon;

    @Column
    private Integer currentHealth;

    @ManyToOne
    @JoinColumn(name = "current_target_id", referencedColumnName = "combatant_id", columnDefinition = "BIGINT(20)")
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
        this.currentHealth = 10 + endurance*2;
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
        return 10 + endurance*2;
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
}
