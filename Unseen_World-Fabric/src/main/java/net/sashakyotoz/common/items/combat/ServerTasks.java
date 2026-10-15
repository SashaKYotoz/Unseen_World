package net.sashakyotoz.common.items.combat;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ServerTasks {
    private static final Map<ResourceKey<Level>, List<Task>> TASKS = new HashMap<>();
    private static boolean registered;

    private ServerTasks() {
    }

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;
        ServerTickEvents.END_WORLD_TICK.register(ServerTasks::tick);
        ServerWorldEvents.UNLOAD.register((server, level) -> TASKS.remove(level.dimension()));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> TASKS.clear());
    }

    public static void schedule(ServerLevel level, Task task) {
        TASKS.computeIfAbsent(level.dimension(), key -> new ArrayList<>()).add(task);
    }

    private static void tick(ServerLevel level) {
        List<Task> tasks = TASKS.get(level.dimension());
        if (tasks == null || tasks.isEmpty()) {
            return;
        }
        List<Task> running = new ArrayList<>(tasks);
        tasks.clear();
        for (Task task : running) {
            if (!task.tick(level)) {
                tasks.add(task);
            }
        }
    }

    @FunctionalInterface
    public interface Task {
        boolean tick(ServerLevel level);
    }
}
