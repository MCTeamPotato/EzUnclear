package me.kall.ezunclear.mixin.nc;

import igentuman.nc.block.fusion.entity.FusionCoreBE;
import me.kall.ezunclear.EzUnclear;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FusionCoreBE.class)
public abstract class FusionCoreBEMixin {
    @Shadow(remap = false)
    protected abstract void handleMeltdown();

    @Shadow(remap = false) public double reactorHeat;

    @Shadow(remap = false) public abstract double getMaxHeat();

    @Shadow(remap = false) public long plasmaTemperature;

    @Redirect(method = "tickServer", remap = false, at = @At(value = "INVOKE", remap = false, target = "Ligentuman/nc/block/fusion/entity/FusionCoreBE;handleMeltdown()V"))
    private void onMeltdown(FusionCoreBE be) {
        if (be.isCasingValid && this.reactorHeat > this.getMaxHeat() && this.plasmaTemperature > 10000L && be.getLevel() instanceof ServerLevel) {
            EzUnclear.broadcast((ServerLevel) be.getLevel(), this::handleMeltdown, be.getBlockPos());
        }
    }
}
