package me.kall.ezunclear.mixin.nc.access;

import igentuman.nc.multiblock.AbstractMultiblock;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = AbstractMultiblock.class)
public interface AbstractMultiblockInvoker {
    @Invoker(value = "getLevel", remap = false)
    Level getLevel();
}
