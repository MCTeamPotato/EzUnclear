package me.kall.ezunclear;

import me.kall.ezunclear.data.PendingMeltdown;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.common.Mod;

@Mod(EzUnclear.MOD_ID)
public final class EzUnclear {
    public static final String MOD_ID = "ezunclear";

    public static boolean broadcast(ServerLevel level, Runnable meltdown, BlockPos pos) {
        if (PendingMeltdown.POSITIONS.contains(pos)) return false;
        Component ezUnclear = Component.translatable("info.ezunclear");
        level.players().forEach(player -> player.displayClientMessage(ezUnclear, false));
        synchronized (PendingMeltdown.MELT_DOWNS) {
            PendingMeltdown.MELT_DOWNS.add(meltdown);
            return PendingMeltdown.POSITIONS.add(pos);
        }
    }
}
