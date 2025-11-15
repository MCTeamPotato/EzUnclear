package me.kall.ezunclear.mixin;

import com.brandon3055.brandonscore.blocks.TileBCore;
import com.brandon3055.draconicevolution.blocks.reactor.ProcessExplosion;
import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorCore;
import me.kall.ezunclear.data.PendingMeltdown;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileReactorCore.class)
public abstract class TileReactorCoreMixin extends TileBCore {
    @Shadow(remap = false) private ProcessExplosion explosionProcess;

    public TileReactorCoreMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "updateCriticalState", remap = false, at = @At(value = "INVOKE", remap = false, target = "Lcom/brandon3055/draconicevolution/blocks/reactor/ProcessExplosion;detonate()Z"), cancellable = true)
    private void onBigExplode(@NotNull CallbackInfo ci) {
        TileReactorCore core = (TileReactorCore) (Object) this;
        if (PendingMeltdown.POSITIONS.contains(core.getBlockPos())) return;
        ci.cancel();
        if (this.level != null) {
            Component ezUnclear = Component.translatable("info.ezunclear");
            this.level.players().forEach(player -> player.displayClientMessage(ezUnclear, false));
            synchronized (PendingMeltdown.MELT_DOWNS) {
                PendingMeltdown.MELT_DOWNS.add(() -> {
                    this.explosionProcess.detonate();
                    this.level.removeBlock(this.worldPosition, false);
                });
                PendingMeltdown.POSITIONS.add(core.getBlockPos());
            }
        }
    }
}
