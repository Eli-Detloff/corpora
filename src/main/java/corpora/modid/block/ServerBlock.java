package corpora.modid.block;

import com.mojang.serialization.MapCodec;
import corpora.modid.Corpora;
import corpora.modid.init.ModCardinalComponents;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;


public class ServerBlock extends Block {
    public static final MapCodec<ServerBlock> CODEC = createCodec(ServerBlock::new);
    public static final BooleanProperty POWERED;

    static {
        POWERED = RedstoneTorchBlock.LIT;
    }

    public ServerBlock(Settings settings) {
        super(settings);
    }

    public MapCodec<ServerBlock> getCodec() {
        return CODEC;
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if (ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos())) {
            ctx.getWorld().scheduleBlockTick(ctx.getBlockPos(), this, 1);
        }
        return this.getDefaultState().with(POWERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos,
                                  Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            boolean powered = world.isReceivingRedstonePower(pos);

            if (powered != state.get(POWERED)) {
                world.setBlockState(pos, state.with(POWERED, powered), 2);

                if (powered) {
                    world.scheduleBlockTick(pos, this, 1);
                }
            }
        }
    }

    public static boolean isMyOrigin(PlayerEntity player) {
        OriginComponent component = ModComponents.ORIGIN.get(player);

        for (Origin origin : component.getOrigins().values()) {
            Corpora.LOGGER.info("Origin found: {}", origin.getId());
        }

        Identifier myOriginId = Identifier.of("uiorigin", "ui_origin");

        boolean result = component.getOrigins()
                .values()
                .stream()
                .anyMatch(origin -> origin.getId().equals(myOriginId));

        Corpora.LOGGER.info("isMyOrigin = {}", result);

        return result;
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(POWERED)) {
            if (world.isReceivingRedstonePower(pos)) {

                world.playSound(
                        null,
                        pos,
                        SoundEvents.BLOCK_BEACON_AMBIENT,
                        SoundCategory.BLOCKS,
                        0.5f,
                        1.0f
                );

                // Play again in 40 ticks (2 seconds)
                world.scheduleBlockTick(pos, this, 40);

            } else {
                world.setBlockState(pos, state.with(POWERED, false), 2);
            }
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }


    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            if (!isMyOrigin(player)) {
                Corpora.LOGGER.info("Denied access");
                return ActionResult.FAIL;
            }

            Corpora.LOGGER.info("Allowed access");


            ServerWorld serverWorld = (ServerWorld) world;

            if (ModCardinalComponents.SERVERCOMP.get(player).getPos() != null) {
                if (ModCardinalComponents.SERVERCOMP.get(player).getPos().equals(pos)) {
                    ModCardinalComponents.SERVERCOMP.get(player).setPos(null);
                    ModCardinalComponents.SERVERCOMP.get(player).setDimension(null);
                    world.playSound(
                            null,
                            pos,
                            SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN,
                            SoundCategory.BLOCKS,
                            0.5f,
                            1.0f
                    );
                    serverWorld.spawnParticles(
                            new DustParticleEffect(
                                    new Vector3f(1.0f, 0.0f, 0.0f), // red RGB
                                    1.0f                            // size
                            ),
                            pos.getX() + 0.5,
                            pos.getY() + 0.5,
                            pos.getZ() + 0.5,
                            8,      // particle count
                            0.25,   // spread X
                            0.25,   // spread Y
                            0.25,   // spread Z
                            0.0     // speed
                    );

                } else {
                    ModCardinalComponents.SERVERCOMP.get(player).setPos(pos);
                    ModCardinalComponents.SERVERCOMP.get(player).setDimension(world.getRegistryKey());
                    world.playSound(
                            null,
                            pos,
                            SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE,
                            SoundCategory.BLOCKS,
                            0.5f,
                            1.0f
                    );
                    serverWorld.spawnParticles(
                            new DustParticleEffect(
                                    new Vector3f(0.0f, 1.0f, 0.0f), // red RGB
                                    1.0f                            // size
                            ),
                            pos.getX() + 0.5,
                            pos.getY() + 0.5,
                            pos.getZ() + 0.5,
                            8,      // particle count
                            0.25,   // spread X
                            0.25,   // spread Y
                            0.25,   // spread Z
                            0.0     // speed
                    );
                }
            } else {
                ModCardinalComponents.SERVERCOMP.get(player).setPos(pos);
                ModCardinalComponents.SERVERCOMP.get(player).setDimension(world.getRegistryKey());
                world.playSound(
                        null,
                        pos,
                        SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE,
                        SoundCategory.BLOCKS,
                        0.5f,
                        1.0f
                );
                serverWorld.spawnParticles(
                        new DustParticleEffect(
                                new Vector3f(0.0f, 1.0f, 0.0f), // red RGB
                                1.0f                            // size
                        ),
                        pos.getX() + 0.75,
                        pos.getY() + 0.75,
                        pos.getZ() + 0.75,
                        8,      // particle count
                        0.25,   // spread X
                        0.25,   // spread Y
                        0.25,   // spread Z
                        0.0     // speed
                );
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }
}
