package corpora.modid.util;

import corpora.modid.Corpora;
import corpora.modid.entity.ModEntities;
import corpora.modid.entity.custom.ShellEntity;
import corpora.modid.init.ModCardinalComponents;
import corpora.modid.init.ModSounds;
import corpora.modid.util.config.ConfigManager;
import io.github.apace100.apoli.Apoli;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

import java.util.UUID;

import static io.github.apace100.apoli.Apoli.server;

public class ShellTeleportHandler {
    public void ShellTeleport(
            ServerPlayer player,
            UUID target
    ) {
        player.sendSystemMessage(Component.literal("Teleporting..."));


        //find selected shell and load data
        ShellDataComponent targetShell = findShell(target);
        if (targetShell == null) {
            player.sendSystemMessage(Component.literal("can not find shell of uuid " + target));
            Corpora.LOGGER.info("can not find shell of uuid " + target);
            return;
        }


        //need to add actual teleport logic

        if (ModCardinalComponents.CURRENT_SHELL_COMPONENT.get(player).get() != null) {
            ShellEntity entity =
                    new ShellEntity(ModEntities.SHELL, player.level());

            entity.setPosRaw(player.getX(), player.getY(), player.getZ());
            entity.setYRot(player.getYRot());
            entity.setXRot(player.getXRot());
            entity.setYBodyRot(player.getVisualRotationYInDegrees());
            entity.setYHeadRot(player.getYHeadRot());
            entity.setCustomName(Component.nullToEmpty(ModCardinalComponents.CURRENT_SHELL_COMPONENT.get(player).get()));
            entity.setHealth(player.getHealth());
            ModCardinalComponents.SHELL_OWNER_COMPONENT.get(entity).set(player.getUUID());
            entity.storePlayerInventory(player);

            player.level().addFreshEntity(entity);
        } else {
            player.getInventory().dropAll();
        }

        ServerLevel targetWorld = Apoli.server.getLevel(targetShell.dimension());

        assert targetWorld != null;
        player.teleportTo(
                targetWorld,
                targetShell.pos().getX(),
                targetShell.pos().getY(),
                targetShell.pos().getZ(),
                targetShell.yaw(),
                targetShell.pitch()
        );

        //debug things
        player.displayClientMessage(Component.literal(
                "shell " + targetShell.name() + " has " + targetShell.health() + " health"
        ), false);

        targetWorld.playSound(
                null,
                targetShell.pos(),
                ModSounds.SHELL_POWER_UP,
                SoundSource.BLOCKS,
                0.5f,
                1.0f
        );


        player.setHealth(targetShell.health());

        ModCardinalComponents.CURRENT_SHELL_COMPONENT.get(player).set(targetShell.name());

        player.giveExperienceLevels(-ConfigManager.getConfig().tpExperienceLevelCost);


        ShellRemoval.add(targetWorld, targetShell.uuid(), 200, player);

    }


    private ShellDataComponent findShell(UUID selectedUuid) {
        for (var world : server.getAllLevels()) {

            EntityRegistryState state = EntityRegistryState.get(world);
            if (state.get(selectedUuid) != null) {
                return state.get(selectedUuid);
            }
        }
        return null;
    }
}
