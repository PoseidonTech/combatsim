package com.rpg.combatsim.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "combatant_weapon")
public class CombatantWeapon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "combatant_weapon_id", columnDefinition = "BIGINT(20)")
    private Integer combatantWeaponId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combatant_id", columnDefinition = "BIGINT(20)")
    private Combatant combatant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weapon_id", columnDefinition = "BIGINT(20)")
    private Weapon weapon;

    CombatantWeapon() {//default constructor
    }

    CombatantWeapon(Combatant combatant, Weapon weapon) {
        this.setCombatant(combatant);
        this.setWeapon(weapon);
    }

    public Integer getCombatantWeaponId() {
        return combatantWeaponId;
    }

    public void setCombatantWeaponId(Integer combatantWeaponId) {
        this.combatantWeaponId = combatantWeaponId;
    }

    public Combatant getCombatant() {
        return combatant;
    }

    public void setCombatant(Combatant combatant) {
        this.combatant = combatant;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}
