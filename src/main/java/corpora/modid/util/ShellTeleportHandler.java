package corpora.modid.util;

import corpora.modid.Corpora;
import corpora.modid.entity.ModEntities;
import corpora.modid.entity.custom.ShellEntity;
import corpora.modid.init.ModCardinalComponents;
import corpora.modid.init.ModSounds;
import corpora.modid.util.config.ConfigManager;
import io.github.apace100.apoli.Apoli;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

import java.util.UUID;

import static io.github.apace100.apoli.Apoli.server;

public class ShellTeleportHandler {
    public void ShellTeleport(
            ServerPlayerEntity player,
            UUID target
    ) {
        player.sendMessage(Text.literal("Teleporting..."));


        //find selected shell and load data
        ShellDataComponent targetShell = findShell(target);
        if (targetShell == null) {
            player.sendMessage(Text.literal("can not find shell of uuid " + target));
            Corpora.LOGGER.info("can not find shell of uuid " + target);
            return;
        }


        //need to add actual teleport logic

        if (ModCardinalComponents.CURRENT_SHELL_COMPONENT.get(player).get() != null) {
            ShellEntity entity =
                    new ShellEntity(ModEntities.SHELL, player.getWorld());

            entity.setPos(player.getX(), player.getY(), player.getZ());
            entity.setYaw(player.getYaw());
            entity.setPitch(player.getPitch());
            entity.setBodyYaw(player.getBodyYaw());
            entity.setHeadYaw(player.getHeadYaw());
            entity.setCustomName(Text.of(ModCardinalComponents.CURRENT_SHELL_COMPONENT.get(player).get()));
            entity.setHealth(player.getHealth());
            ModCardinalComponents.SHELL_OWNER_COMPONENT.get(entity).set(player.getUuid());
            entity.storePlayerInventory(player);

            player.getWorld().spawnEntity(entity);
        } else {
            player.getInventory().dropAll();
        }

        ServerWorld targetWorld = Apoli.server.getWorld(targetShell.dimension());

        assert targetWorld != null;
        player.teleport(
                targetWorld,
                targetShell.pos().getX(),
                targetShell.pos().getY(),
                targetShell.pos().getZ(),
                targetShell.yaw(),
                targetShell.pitch()
        );

        //debug things
        player.sendMessage(Text.literal(
                "shell " + targetShell.name() + " has " + targetShell.health() + " health"
        ), false);

        targetWorld.playSound(
                null,
                targetShell.pos(),
                ModSounds.SHELL_POWER_UP,
                SoundCategory.BLOCKS,
                0.5f,
                1.0f
        );


        player.setHealth(targetShell.health());

        ModCardinalComponents.CURRENT_SHELL_COMPONENT.get(player).set(targetShell.name());

        player.addExperienceLevels(-ConfigManager.getConfig().tpExperienceLevelCost);


        ShellRemoval.add(targetWorld, targetShell.uuid(), 200, player);

    }


    private ShellDataComponent findShell(UUID selectedUuid) {
        for (var world : server.getWorlds()) {

            EntityRegistryState state = EntityRegistryState.get(world);
            if (state.get(selectedUuid) != null) {
                return state.get(selectedUuid);
            }
        }
        return null;
    }
}
