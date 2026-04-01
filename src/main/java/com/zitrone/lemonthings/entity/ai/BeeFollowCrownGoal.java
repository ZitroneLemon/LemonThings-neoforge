package com.zitrone.lemonthings.entity.ai;

import com.zitrone.lemonthings.item.ModItems;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.EnumSet;
import java.util.List;

public class BeeFollowCrownGoal extends Goal {

    private final Bee bee;
    private Player targetPlayer;
    private int ticksFollowing;
    private static final int MAX_FOLLOW_TICKS = 600; // 30 секунд максимум

    public BeeFollowCrownGoal(Bee bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // Не приманиваем злых пчёл и тех кто несёт нектар
        if (bee.isAngry()) return false;
        if (bee.hasNectar()) return false;
        if (bee.level().isClientSide()) return false;

        // Ищем ближайшего игрока с венком в радиусе 12 блоков
        List<Player> players = bee.level().getEntitiesOfClass(
                Player.class,
                bee.getBoundingBox().inflate(12.0)
        );

        for (Player player : players) {
            if (wearingFlowerCrown(player)) {
                targetPlayer = player;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (targetPlayer == null) return false;
        if (!targetPlayer.isAlive()) return false;
        if (bee.isAngry()) return false;
        if (bee.hasNectar()) return false;
        if (ticksFollowing >= MAX_FOLLOW_TICKS) return false;
        if (bee.distanceTo(targetPlayer) > 20.0) return false;
        if (!wearingFlowerCrown(targetPlayer)) return false;
        return true;
    }

    @Override
    public void start() {
        ticksFollowing = 0;
    }

    @Override
    public void stop() {
        targetPlayer = null;
        ticksFollowing = 0;
        bee.getNavigation().stop();
    }

    @Override
    public void tick() {
        ticksFollowing++;

        // Смотрим на игрока
        bee.getLookControl().setLookAt(
                targetPlayer,
                30.0f,
                30.0f
        );

        // Летим к голове игрока
        double headY = targetPlayer.getY() + targetPlayer.getEyeHeight() + 0.5;

        bee.getNavigation().moveTo(
                targetPlayer.getX(),
                headY,
                targetPlayer.getZ(),
                1.2 // скорость
        );

        // Если пчела очень близко к голове — кружимся рядом
        double dist = bee.distanceTo(targetPlayer);
        if (dist < 2.0) {
            bee.getNavigation().stop();
            // Небольшое круговое движение вокруг головы
            double angle = (ticksFollowing * 5) * Math.PI / 180.0;
            bee.setDeltaMovement(
                    Math.cos(angle) * 0.15,
                    0.05,
                    Math.sin(angle) * 0.15
            );
        }
    }

    private boolean wearingFlowerCrown(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(inv -> inv.getStacksHandler("head"))
                .map(handler -> handler
                        .map(h -> !h.getStacks().getStackInSlot(0).isEmpty() &&
                                h.getStacks().getStackInSlot(0).is(
                                        ModItems.FLOWER_CROWN.get()))
                        .orElse(false))
                .orElse(false);
    }
}