package com.narxoz.rpg.arena;

public class ArenaFighter {
    private final String name;
    private int health;
    private final int maxHealth;
    private double dodgeChance;
    private final int blockRating;
    private final int armorValue;
    private final int attackPower;
    private int healPotions;

    public ArenaFighter(String name, int health, double dodgeChance,
                        int blockRating, int armorValue, int attackPower, int healPotions) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.dodgeChance = dodgeChance;
        this.blockRating = blockRating;
        this.armorValue = armorValue;
        this.attackPower = attackPower;
        this.healPotions = healPotions;
    }

    // ==================== GETTERS ====================
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public double getDodgeChance() {
        return dodgeChance;
    }

    public int getBlockRating() {
        return blockRating;
    }

    public int getArmorValue() {
        return armorValue;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getHealPotions() {
        return healPotions;
    }

    // ==================== MUTATION METHODS ====================
    public void takeDamage(int amount) {
        health = Math.max(0, health - amount);
    }

    public void heal(int amount) {
        if (healPotions > 0) {
            health = Math.min(maxHealth, health + amount);
            healPotions--;
        }
    }

    public void modifyDodgeChance(double delta) {
        dodgeChance += delta;
        dodgeChance = Math.max(0.0, Math.min(1.0, dodgeChance)); // clamp между 0 и 1
    }

    public boolean isAlive() {
        return health > 0;
    }
}