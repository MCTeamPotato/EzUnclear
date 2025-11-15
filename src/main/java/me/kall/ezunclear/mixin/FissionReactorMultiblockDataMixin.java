package me.kall.ezunclear.mixin;

import me.kall.ezunclear.data.PendingMeltdown;
import mekanism.generators.common.content.fission.FissionReactorMultiblockData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FissionReactorMultiblockData.class)
public abstract class FissionReactorMultiblockDataMixin {
    @Shadow(remap = false) protected abstract void createMeltdown(Level world);

    @Redirect(method = "handleDamage", remap = false, at = @At(value = "INVOKE", remap = false, target = "Lmekanism/generators/common/content/fission/FissionReactorMultiblockData;createMeltdown(Lnet/minecraft/world/level/Level;)V"))
    private void onMeltDown(FissionReactorMultiblockData instance, @NotNull Level world) {
        FissionReactorMultiblockData data = (FissionReactorMultiblockData) (Object) this;
        if (PendingMeltdown.POSITIONS.contains(data.getMinPos())) return;
        Component ezUnclear = Component.translatable("info.ezunclear");
        world.players().forEach(player -> player.displayClientMessage(ezUnclear, false));
        synchronized (PendingMeltdown.MELT_DOWNS) {
            PendingMeltdown.MELT_DOWNS.add(() -> this.createMeltdown(world));
            PendingMeltdown.POSITIONS.add(data.getMinPos());
        }
    }
}
