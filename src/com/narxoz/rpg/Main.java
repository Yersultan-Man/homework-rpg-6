package com.narxoz.rpg;

import com.narxoz.rpg.arena.ArenaFighter;
import com.narxoz.rpg.arena.ArenaOpponent;
import com.narxoz.rpg.arena.TournamentResult;
import com.narxoz.rpg.chain.*;
import com.narxoz.rpg.command.*;
import com.narxoz.rpg.tournament.TournamentEngine;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== RPG Grand Arena Tournament ===\n");

        // === PHASE 2: CHAIN OF RESPONSIBILITY DEMO ===
        System.out.println("=== 1. CHAIN OF RESPONSIBILITY DEMO ===");
        chainDemo();
        System.out.println("\n" + "=".repeat(50) + "\n");

        // === PHASE 3: COMMAND PATTERN DEMO ===
        System.out.println("=== 2. COMMAND PATTERN DEMO ===");
        commandDemo();
        System.out.println("\n" + "=".repeat(50) + "\n");

        // === PHASE 5-6: FULL TOURNAMENT ===
        System.out.println("=== 3. FULL TOURNAMENT ===");
        tournamentDemo();

        System.out.println("\n=== Все демо завершены! ===");
    }

    private static void chainDemo() {
        ArenaFighter hero = new ArenaFighter("Hero", 100, 0.3, 40, 15, 25, 3);

        // Строим цепочку вручную
        DefenseHandler dodge = new DodgeHandler(hero.getDodgeChance(), 42L);
        DefenseHandler block = new BlockHandler(hero.getBlockRating() / 100.0);
        DefenseHandler armor = new ArmorHandler(hero.getArmorValue());
        DefenseHandler hp = new HpHandler();

        dodge.setNext(block).setNext(armor).setNext(hp);

        System.out.println("Hero HP before: " + hero.getHealth());
        System.out.println("Incoming damage: 50\n");

        dodge.handle(50, hero);

        System.out.println("\nHero HP after: " + hero.getHealth());
    }

    private static void commandDemo() {
        ArenaOpponent enemy = new ArenaOpponent("Goblin", 80, 20);
        ArenaFighter hero = new ArenaFighter("Hero", 100, 0.25, 30, 10, 25, 2);

        ActionQueue queue = new ActionQueue();

        queue.enqueue(new AttackCommand(enemy, hero.getAttackPower()));
        queue.enqueue(new HealCommand(hero, 30));
        queue.enqueue(new DefendCommand(hero, 0.20));

        System.out.println("Queue before undo:");
        queue.getCommandDescriptions().forEach(desc -> System.out.println("  • " + desc));

        queue.undoLast(); // убираем Defend

        System.out.println("\nQueue after undoLast():");
        queue.getCommandDescriptions().forEach(desc -> System.out.println("  • " + desc));

        System.out.println("\nExecuting remaining commands...");
        queue.executeAll();

        System.out.println("Enemy HP after attack: " + enemy.getHealth());
    }

    private static void tournamentDemo() {
        ArenaFighter hero = new ArenaFighter("Aibol", 120, 0.25, 35, 12, 28, 3);
        ArenaOpponent opponent = new ArenaOpponent("Dark Knight", 100, 22);

        TournamentEngine engine = new TournamentEngine(hero, opponent);
        // engine.setRandomSeed(12345L); // можно раскомментировать для повторяемости

        TournamentResult result = engine.runTournament();

        System.out.println("WINNER: " + result.getWinner());
        System.out.println("Rounds played: " + result.getRounds());
        System.out.println("\n=== Tournament Log ===");
        result.getLog().forEach(System.out::println);
    }
}