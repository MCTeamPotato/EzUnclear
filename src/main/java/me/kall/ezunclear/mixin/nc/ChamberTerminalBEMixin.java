package me.kall.ezunclear.mixin.nc;

import igentuman.nc.block.kugelblitz.entity.ChamberTerminalBE;
import igentuman.nc.multiblock.kugelblitz.KugelblitzMultiblock;
import me.kall.ezunclear.EzUnclear;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static igentuman.nc.block.kugelblitz.entity.BlackHoleBE.MAX_MASS;

@Mixin(value = ChamberTerminalBE.class)
public abstract class ChamberTerminalBEMixin {
    @Shadow(remap = false) public long mass;
    @Shadow(remap = false) public abstract KugelblitzMultiblock getMultiblock();
    @Shadow(remap = false) public abstract boolean hasBlackhole();
    @Shadow(remap = false) protected abstract void handleMeltdown();

    @Redirect(method = "tickServer", remap = false, at = @At(value = "INVOKE", remap = false, target = "Ligentuman/nc/block/kugelblitz/entity/ChamberTerminalBE;handleMeltdown()V"))
    private void onMeltdown(ChamberTerminalBE instance) {
        if (mass > MAX_MASS && getMultiblock().isFormed() && hasBlackhole() && instance.getLevel() instanceof ServerLevel) {
            EzUnclear.broadcast((ServerLevel) instance.getLevel(), this::handleMeltdown, instance.getBlockPos());
        }
    }
}
