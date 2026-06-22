package corpora.modid.block;

import com.mojang.serialization.MapCodec;
import corpora.modid.Corpora;
import corpora.modid.init.ModCardinalComponents;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;


public class ServerBlock extends Block {
    public static final MapCodec<ServerBlock> CODEC = simpleCodec(ServerBlock::new);
    public static final BooleanProperty POWERED;

    static {
        POWERED = RedstoneTorchBlock.LIT;
    }

    public ServerBlock(Properties settings) {
        super(settings);
    }

    public MapCodec<ServerBlock> codec() {
        return CODEC;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        if (ctx.getLevel().hasNeighborSignal(ctx.getClickedPos())) {
            ctx.getLevel().scheduleTick(ctx.getClickedPos(), this, 1);
        }
        return this.defaultBlockState().setValue(POWERED, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos,
                                   Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClientSide) {
            boolean powered = world.hasNeighborSignal(pos);

            if (powered != state.getValue(POWERED)) {
                world.setBlock(pos, state.setValue(POWERED, powered), 2);

                if (powered) {
                    world.scheduleTick(pos, this, 1);
                }
            }
        }
    }

    public static boolean isMyOrigin(Player player) {
        OriginComponent component = ModComponents.ORIGIN.get(player);

        for (Origin origin : component.getOrigins().values()) {
            Corpora.LOGGER.info("Origin found: {}", origin.getId());
        }

        ResourceLocation myOriginId = ResourceLocation.fromNamespaceAndPath("uiorigin", "ui_origin");

        boolean result = component.getOrigins()
                .values()
                .stream()
                .anyMatch(origin -> origin.getId().equals(myOriginId));

        Corpora.LOGGER.info("isMyOrigin = {}", result);

        return result;
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED)) {
            if (world.hasNeighborSignal(pos)) {

                world.playSound(
                        null,
                        pos,
                        SoundEvents.BEACON_AMBIENT,
                        SoundSource.BLOCKS,
                        0.5f,
                        1.0f
                );

                // Play again in 40 ticks (2 seconds)
                world.scheduleTick(pos, this, 40);

            } else {
                world.setBlock(pos, state.setValue(POWERED, false), 2);
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide) {
            if (!isMyOrigin(player)) {
                Corpora.LOGGER.info("Denied access");
                return InteractionResult.FAIL;
            }

            Corpora.LOGGER.info("Allowed access");


            ServerLevel serverWorld = (ServerLevel) world;

            if (ModCardinalComponents.SERVERCOMP.get(player).getPos() != null) {
                if (ModCardinalComponents.SERVERCOMP.get(player).getPos().equals(pos)) {
                    ModCardinalComponents.SERVERCOMP.get(player).setPos(null);
                    ModCardinalComponents.SERVERCOMP.get(player).setDimension(null);
                    world.playSound(
                            null,
                            pos,
                            SoundEvents.RESPAWN_ANCHOR_SET_SPAWN,
                            SoundSource.BLOCKS,
                            0.5f,
                            1.0f
                    );
                    serverWorld.sendParticles(
                            new DustParticleOptions(
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
                    ModCardinalComponents.SERVERCOMP.get(player).setDimension(world.dimension());
                    world.playSound(
                            null,
                            pos,
                            SoundEvents.RESPAWN_ANCHOR_CHARGE,
                            SoundSource.BLOCKS,
                            0.5f,
                            1.0f
                    );
                    serverWorld.sendParticles(
                            new DustParticleOptions(
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
                ModCardinalComponents.SERVERCOMP.get(player).setDimension(world.dimension());
                world.playSound(
                        null,
                        pos,
                        SoundEvents.RESPAWN_ANCHOR_CHARGE,
                        SoundSource.BLOCKS,
                        0.5f,
                        1.0f
                );
                serverWorld.sendParticles(
                        new DustParticleOptions(
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
        return super.useWithoutItem(state, world, pos, player, hit);
    }
}
