package me.kall.ezunclear.mixin.nc;

import igentuman.nc.block.fission.entity.FissionControllerBE;
import me.kall.ezunclear.EzUnclear;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FissionControllerBE.class)
public abstract class FissionControllerBEMixin {
    @Shadow(remap = false) protected abstract void handleMeltdown();

    @Redirect(method = "tickServer", remap = false, at = @At(value = "INVOKE", remap = false, target = "Ligentuman/nc/block/fission/entity/FissionControllerBE;handleMeltdown()V"))
    private void onMeltdown(FissionControllerBE be) {
        if (be.heat > be.getMaxHeat() && be.getLevel() instanceof ServerLevel) {
            EzUnclear.broadcast((ServerLevel) be.getLevel(), this::handleMeltdown, be.getBlockPos());
        }
    }
}
