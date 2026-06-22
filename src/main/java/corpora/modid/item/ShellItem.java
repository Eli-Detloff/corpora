package corpora.modid.item;

import corpora.modid.Corpora;
import corpora.modid.entity.ModEntities;
import corpora.modid.init.ModCardinalComponents;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ShellItem extends Item {

    public ShellItem(Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!(world instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {

            assert context.getPlayer() != null;
            OriginComponent component = ModComponents.ORIGIN.get(context.getPlayer());

            for (Origin origin : component.getOrigins().values()) {
                Corpora.LOGGER.info("Origin found: {}", origin.getId());
            }

            ResourceLocation myOriginId = ResourceLocation.fromNamespaceAndPath("uiorigin", "ui_origin");

            boolean result = component.getOrigins()
                    .values()
                    .stream()
                    .anyMatch(origin -> origin.getId().equals(myOriginId));

            if (!result) {
                return InteractionResult.FAIL;
            }


            ItemStack itemStack = context.getItemInHand();
            BlockPos blockPos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockState = world.getBlockState(blockPos);
            BlockPos blockPos2;
            if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
                blockPos2 = blockPos;
            } else {
                blockPos2 = blockPos.relative(direction);
            }

            EntityType<?> entityType = ModEntities.SHELL;
            Entity entity = entityType.spawn(
                    (ServerLevel) world,
                    itemStack,
                    context.getPlayer(),
                    blockPos2,
                    MobSpawnType.SPAWN_EGG,
                    true,
                    !Objects.equals(blockPos, blockPos2) && direction == Direction.UP);

            if (entity != null) {
                assert context.getPlayer() != null;
                Player player = context.getPlayer();
                ModCardinalComponents.SHELL_OWNER_COMPONENT.get(entity).set(player.getUUID());
                double dx = player.getX() - entity.getX();
                double dz = player.getZ() - entity.getZ();

                float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);

                entity.setYRot(yaw);
                entity.setYBodyRot(yaw);
                entity.setYHeadRot(yaw); //make mob face you
            }

            itemStack.shrink(1);
            world.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
            return InteractionResult.CONSUME;
        }
    }
}
