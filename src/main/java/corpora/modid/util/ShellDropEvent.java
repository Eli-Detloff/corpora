package corpora.modid.util;

import corpora.modid.Corpora;
import corpora.modid.init.ModCardinalComponents;
import corpora.modid.init.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class ShellDropEvent {
    public static void init() {

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {

            if (!(entity instanceof ServerPlayerEntity player)) {
                return;
            }

            String component = ModCardinalComponents.CURRENT_SHELL_COMPONENT.get(player).get();

            if (component == null) {
                Corpora.LOGGER.info("no component found for death of {}", player.getPlayerListName());
                return;
            }

            // Create broken robot item
            ItemStack brokenShell = new ItemStack(ModItems.SHELL_ITEM_BROKEN);


            // Drop at death location
            ItemEntity itemEntity = new ItemEntity(
                    player.getWorld(),
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    brokenShell
            );

            player.getWorld().spawnEntity(itemEntity);
            ModCardinalComponents.CURRENT_SHELL_COMPONENT.get(player).set(null);

        });
    }
}
