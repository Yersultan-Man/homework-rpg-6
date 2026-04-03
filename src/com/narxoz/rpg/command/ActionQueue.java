package com.narxoz.rpg.command;

import java.util.ArrayList;
import java.util.List;

public class ActionQueue {
    private final List<ActionCommand> queue = new ArrayList<>();

    public void enqueue(ActionCommand cmd) {
        queue.add(cmd);
    }

    public void undoLast() {
        if (!queue.isEmpty()) {
            queue.remove(queue.size() - 1); // просто удаляем, не вызываем undo()
        }
    }

    public void executeAll() {
        for (ActionCommand cmd : queue) {
            cmd.execute();
        }
        queue.clear(); // обязательно очищаем очередь после выполнения
    }

    public List<String> getCommandDescriptions() {
        List<String> descriptions = new ArrayList<>();
        for (ActionCommand cmd : queue) {
            descriptions.add(cmd.getDescription());
        }
        return descriptions; // возвращаем копию списка (независимую)
    }
}