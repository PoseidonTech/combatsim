package com.rpg.combatsim.simulation;

import com.rpg.combatsim.domain.Combatant;

import java.util.List;
import java.util.Map;

public interface ISimulation {
    void handleDeath(List<Combatant> livingCombatantList, Combatant attacker, Map<Combatant, Map<Combatant, Integer>> threatMap);
    void takeTurn(List<Combatant> livingCombatantList, Combatant attacker, Map<Combatant, Map<Combatant, Integer>> threatMap);
    void runSim(List<Combatant> combatantList);
}
