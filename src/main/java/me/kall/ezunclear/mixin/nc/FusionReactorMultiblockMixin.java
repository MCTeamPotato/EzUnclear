package me.kall.ezunclear.mixin.nc;

import igentuman.nc.block.fusion.entity.FusionCoreBE;
import igentuman.nc.multiblock.fusion.FusionReactorMultiblock;
import me.kall.ezunclear.EzUnclear;
import me.kall.ezunclear.mixin.nc.access.AbstractMultiblockInvoker;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FusionReactorMultiblock.class)
public abstract class FusionReactorMultiblockMixin {
    @Shadow(remap = false) protected abstract FusionCoreBE controllerBE();

    @Shadow(remap = false) protected abstract void handleMeltdown();

    @Redirect(method = "validate", remap = false, at = @At(value = "INVOKE", remap = false, target = "Ligentuman/nc/multiblock/fusion/FusionReactorMultiblock;handleMeltdown()V"))
    private void onMeltdown(FusionReactorMultiblock instance) {
        if (instance.validationResult.isValid && controllerBE().plasmaTemperature > 100000 && ((AbstractMultiblockInvoker)instance).getLevel() instanceof ServerLevel) {
            EzUnclear.broadcast((ServerLevel) ((AbstractMultiblockInvoker)instance).getLevel(), this::handleMeltdown, instance.getCenterBlock());
        }
    }
}
