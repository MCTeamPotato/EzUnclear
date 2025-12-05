package me.kall.ezunclear.mixin;

import com.brandon3055.brandonscore.blocks.TileBCore;
import com.brandon3055.draconicevolution.blocks.reactor.ProcessExplosion;
import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorCore;
import me.kall.ezunclear.EzUnclear;
import me.kall.ezunclear.data.PendingMeltdown;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TileReactorCore.class)
public abstract class TileReactorCoreMixin extends TileBCore {
    @Shadow(remap = false) private ProcessExplosion explosionProcess;

    public TileReactorCoreMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Redirect(method = "updateCriticalState", remap = false, at = @At(value = "INVOKE", remap = false, target = "Lcom/brandon3055/draconicevolution/blocks/reactor/ProcessExplosion;detonate()Z"))
    private boolean onBigExplode(ProcessExplosion instance) {
        TileReactorCore core = (TileReactorCore) (Object) this;
        if (PendingMeltdown.POSITIONS.contains(core.getBlockPos())) return false;
        if (this.level instanceof ServerLevel) {
            return EzUnclear.broadcast((ServerLevel) this.level, () -> {
                this.explosionProcess.detonate();
                this.level.removeBlock(this.worldPosition, false);
            }, core.getBlockPos());
        }
        return false;
    }

    @Redirect(method = "updateCriticalState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
    private boolean onRemove(Level instance, BlockPos blockPos, boolean pos) {
        return false;
    }
}
