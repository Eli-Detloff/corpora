package corpora.modid.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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


}
