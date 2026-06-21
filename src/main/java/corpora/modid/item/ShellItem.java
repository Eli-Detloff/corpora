package corpora.modid.item;

import corpora.modid.Corpora;
import corpora.modid.entity.ModEntities;
import corpora.modid.init.ModCardinalComponents;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Objects;

public class ShellItem extends Item {

    public ShellItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!(world instanceof ServerWorld)) {
            return ActionResult.SUCCESS;
        } else {

            assert context.getPlayer() != null;
            OriginComponent component = ModComponents.ORIGIN.get(context.getPlayer());

            for (Origin origin : component.getOrigins().values()) {
                Corpora.LOGGER.info("Origin found: {}", origin.getId());
            }

            Identifier myOriginId = Identifier.of("uiorigin", "ui_origin");

            boolean result = component.getOrigins()
                    .values()
                    .stream()
                    .anyMatch(origin -> origin.getId().equals(myOriginId));

            if (!result) {
                return ActionResult.FAIL;
            }


            ItemStack itemStack = context.getStack();
            BlockPos blockPos = context.getBlockPos();
            Direction direction = context.getSide();
            BlockState blockState = world.getBlockState(blockPos);
            BlockPos blockPos2;
            if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
                blockPos2 = blockPos;
            } else {
                blockPos2 = blockPos.offset(direction);
            }

            EntityType<?> entityType = ModEntities.SHELL;
            Entity entity = entityType.spawnFromItemStack(
                    (ServerWorld) world,
                    itemStack,
                    context.getPlayer(),
                    blockPos2,
                    SpawnReason.SPAWN_EGG,
                    true,
                    !Objects.equals(blockPos, blockPos2) && direction == Direction.UP);

            if (entity != null) {
                assert context.getPlayer() != null;
                PlayerEntity player = context.getPlayer();
                ModCardinalComponents.SHELL_OWNER_COMPONENT.get(entity).set(player.getUuid());
                double dx = player.getX() - entity.getX();
                double dz = player.getZ() - entity.getZ();

                float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);

                entity.setYaw(yaw);
                entity.setBodyYaw(yaw);
                entity.setHeadYaw(yaw); //make mob face you
            }

            itemStack.decrement(1);
            world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
            return ActionResult.CONSUME;
        }
    }
}
