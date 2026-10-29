package net.sashakyotoz.common.items.combat;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

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
        NeoForge.EVENT_BUS.addListener((LevelTickEvent.Post event) -> {
            if (event.getLevel() instanceof ServerLevel level) {
                tick(level);
            }
        });
        NeoForge.EVENT_BUS.addListener((LevelEvent.Unload event) -> {
            if (event.getLevel() instanceof ServerLevel level) {
                TASKS.remove(level.dimension());
            }
        });
        NeoForge.EVENT_BUS.addListener((ServerStoppedEvent event) -> TASKS.clear());
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
