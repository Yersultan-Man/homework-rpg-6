package com.narxoz.rpg.command;

import com.narxoz.rpg.arena.ArenaOpponent;

public class AttackCommand implements ActionCommand {
    private final ArenaOpponent target;
    private final int attackPower;
    private int damageDealt = 0;

    public AttackCommand(ArenaOpponent target, int attackPower) {
        this.target = target;
        this.attackPower = attackPower;
    }

    @Override
    public void execute() {
        damageDealt = attackPower; // сохраняем сколько реально нанесли
        target.takeDamage(attackPower);
        // Если противник имел меньше HP, takeDamage уже зажмёт до 0
    }

    @Override
    public void undo() {
        if (damageDealt > 0) {
            target.restoreHealth(damageDealt);
        }
    }

    @Override
    public String getDescription() {
        return "Attack for " + attackPower + " damage";
    }
}