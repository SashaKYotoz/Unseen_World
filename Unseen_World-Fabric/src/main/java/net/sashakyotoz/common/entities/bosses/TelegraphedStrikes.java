package net.sashakyotoz.common.entities.bosses;

import net.minecraft.world.phys.Vec3;

import java.util.*;

public final class TelegraphedStrikes {
    public interface Handler {
        void telegraph(Vec3 pos, boolean urgent);

        void strike(Vec3 pos);
    }

    private record Mark(Vec3 pos, int warnAt, int strikeAt) {
    }

    private final List<Mark> marks = new ArrayList<>();
    private final Set<UUID> hits = new HashSet<>();
    private final Handler handler;
    private int hitTick = Integer.MIN_VALUE;

    public TelegraphedStrikes(Handler handler) {
        this.handler = handler;
    }

    public void schedule(Vec3 pos, int now, int delay) {
        this.marks.add(new Mark(pos, now + delay / 2, now + delay));
        this.handler.telegraph(pos, false);
    }

    public void tick(int now) {
        Iterator<Mark> iterator = this.marks.iterator();
        while (iterator.hasNext()) {
            Mark mark = iterator.next();
            if (now == mark.warnAt()) {
                this.handler.telegraph(mark.pos(), true);
            }
            if (now >= mark.strikeAt()) {
                this.handler.strike(mark.pos());
                iterator.remove();
            }
        }
    }

    public boolean firstHit(UUID id, int now) {
        if (this.hitTick != now) {
            this.hitTick = now;
            this.hits.clear();
        }
        return this.hits.add(id);
    }

    public boolean isEmpty() {
        return this.marks.isEmpty();
    }

    public void clear() {
        this.marks.clear();
        this.hits.clear();
        this.hitTick = Integer.MIN_VALUE;
    }
}