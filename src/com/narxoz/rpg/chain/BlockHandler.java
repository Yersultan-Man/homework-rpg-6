package com.narxoz.rpg.chain;

import com.narxoz.rpg.arena.ArenaFighter;

public class BlockHandler extends DefenseHandler {
    private final double blockPercent;

    public BlockHandler(double blockPercent) {
        this.blockPercent = blockPercent;
    }

    @Override
    public void handle(int incomingDamage, ArenaFighter target) {
        int blocked = (int) (incomingDamage * blockPercent);
        int remainingDamage = incomingDamage - blocked;

        System.out.println("[Block] Blocked " + blocked + "% (" + blocked +
                " damage), remaining: " + remainingDamage);

        // Всегда передаём дальше, даже если remainingDamage == 0
        passToNext(remainingDamage, target);
    }
}