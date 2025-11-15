package me.kall.ezunclear.data;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.kall.ezunclear.EzUnclear;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = EzUnclear.MOD_ID)
public class PendingMeltdown {
    public static final List<Runnable> MELT_DOWNS = new ObjectArrayList<>();
    public static final Set<BlockPos> POSITIONS = new ObjectOpenHashSet<>();
    private static final Logger LOGGER = LogManager.getLogger(PendingMeltdown.class);

    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        if (event.getRawText().equals(I18n.get("info.ezunclear.interact"))) {
            synchronized (MELT_DOWNS) {
                MELT_DOWNS.forEach(task -> {
                    try {
                        task.run();
                    } catch (Exception exception) {
                        LOGGER.error("Error running meltdown task", exception);
                    }
                });
                MELT_DOWNS.clear();
                POSITIONS.clear();
            }
        }
    }
}
