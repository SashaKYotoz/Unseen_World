package net.sashakyotoz.common.entities.bosses;

public interface BossState {
    boolean isAttack();

    default int duration() {
        return 0;
    }
}
