package com.narxoz.rpg.tournament;

import com.narxoz.rpg.arena.ArenaFighter;
import com.narxoz.rpg.arena.ArenaOpponent;
import com.narxoz.rpg.arena.TournamentResult;
import com.narxoz.rpg.chain.ArmorHandler;
import com.narxoz.rpg.chain.BlockHandler;
import com.narxoz.rpg.chain.DefenseHandler;
import com.narxoz.rpg.chain.DodgeHandler;
import com.narxoz.rpg.chain.HpHandler;
import com.narxoz.rpg.command.ActionQueue;
import com.narxoz.rpg.command.AttackCommand;
import com.narxoz.rpg.command.DefendCommand;
import com.narxoz.rpg.command.HealCommand;

import java.util.Random;

public class TournamentEngine {
    private final ArenaFighter hero;
    private final ArenaOpponent opponent;
    private Random random = new Random(1L);   // seed по умолчанию

    public TournamentEngine(ArenaFighter hero, ArenaOpponent opponent) {
        this.hero = hero;
        this.opponent = opponent;
    }

    public TournamentEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public TournamentResult runTournament() {
        TournamentResult result = new TournamentResult();
        int round = 0;
        final int maxRounds = 20;

        // 1. Строим цепочку защиты (Dodge → Block → Armor → HP)
        DefenseHandler dodge = new DodgeHandler(hero.getDodgeChance(), random.nextLong());
        DefenseHandler block = new BlockHandler(hero.getBlockRating() / 100.0);
        DefenseHandler armor = new ArmorHandler(hero.getArmorValue());
        DefenseHandler hp    = new HpHandler();

        // fluent chaining
        dodge.setNext(block).setNext(armor).setNext(hp);

        // 2. Создаём очередь команд
        ActionQueue actionQueue = new ActionQueue();

        // 3. Основной цикл турнира
        while (hero.isAlive() && opponent.isAlive() && round < maxRounds) {
            round++;

            // Очередь действий героя на этот раунд
            actionQueue.enqueue(new AttackCommand(opponent, hero.getAttackPower()));
            actionQueue.enqueue(new HealCommand(hero, 25));           // 25 HP heal
            actionQueue.enqueue(new DefendCommand(hero, 0.15));      // +15% dodge

            // Печатаем, что находится в очереди
            System.out.println("\n[Round " + round + "] Queued actions:");
            for (String desc : actionQueue.getCommandDescriptions()) {
                System.out.println("  • " + desc);
            }

            // Выполняем все действия героя
            actionQueue.executeAll();

            // Атака оппонента через цепочку защиты
            if (opponent.isAlive()) {
                int attackPower = opponent.getAttackPower();
                System.out.println("[Opponent Attack] " + opponent.getName() +
                        " attacks for " + attackPower + " damage!");

                dodge.handle(attackPower, hero);
            }

            // Логируем состояние после раунда
            String logLine = "[Round " + round + "] " +
                    opponent.getName() + " HP: " + opponent.getHealth() +
                    " | " + hero.getName() + " HP: " + hero.getHealth();
            System.out.println(logLine);
            result.addLine(logLine);
        }

        // Определяем победителя
        String winner = hero.isAlive() ? hero.getName() : opponent.getName();
        result.setWinner(winner);
        result.setRounds(round);

        System.out.println("\n=== Tournament finished! Winner: " + winner + " after " + round + " rounds ===\n");

        return result;
    }
}