package me.kall.ezunclear.data;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.kall.ezunclear.EzUnclear;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod.EventBusSubscriber(modid = EzUnclear.MOD_ID)
public class PendingMeltdown {
    public static final List<Runnable> MELT_DOWNS = new ObjectArrayList<>();
    private static final Logger LOGGER = LogManager.getLogger(PendingMeltdown.class);

    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        Component goWrong = Component.translatable("info.ezunclear.interact");
        if (event.getRawText().equals(goWrong.getString())) {
            synchronized (MELT_DOWNS) {
                MELT_DOWNS.forEach(task -> {
                    try {
                        task.run();
                    } catch (Exception exception) {
                        LOGGER.error("Error running meltdown task", exception);
                    }
                });
                MELT_DOWNS.clear();
            }
        }
    }
}
