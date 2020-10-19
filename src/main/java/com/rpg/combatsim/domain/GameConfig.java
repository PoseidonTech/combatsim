package com.rpg.combatsim.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "gameConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class GameConfig {
    @XmlElementWrapper(name="combatants")
    @XmlElements(@XmlElement(name = "combatant", type = Combatant.class))
    private List<Combatant> combatants;

    @XmlElementWrapper(name="weapons")
    @XmlElements(@XmlElement(name = "weapon", type = Weapon.class))
    private List<Weapon> weapons;

    public List<Combatant> getCombatants() {
        return combatants;
    }
    public void setCombatants(List<Combatant> combatants) {
        this.combatants = combatants;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }
    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }
}
