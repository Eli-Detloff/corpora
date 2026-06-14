package corpora.modid.block;

import com.mojang.serialization.MapCodec;
import corpora.modid.Corpora;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class ServerBlock extends Block {
    public ServerBlock(Settings settings) {
        super(settings);
    }
    public static final MapCodec<ServerBlock> CODEC = createCodec(ServerBlock::new);
    public static boolean ISACTIVE;

    public static final List<BlockPos> COOLING_OFFSET = BlockPos.stream(-1, -1, -1, 1, 1, 1)
            .filter((blockPos) -> Math.abs(blockPos.getX()) == 1 || Math.abs(blockPos.getZ()) == 1).map(BlockPos::toImmutable).toList();


    public static boolean isValidCooling(World world, BlockPos blockPos, BlockPos blockPos2) {
        return world.getBlockState(blockPos.add(blockPos2)).isIn(BlockTags.ICE);
    }

    public static int countCoolingBlocks(World world, BlockPos pos) {

        int count = 0;
        for (BlockPos offset : COOLING_OFFSET) {
            if (isValidCooling(world, pos, offset)) {
                count++;
            }
        }
        return count;
    }

    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (countCoolingBlocks(world, pos) >=9){
            ISACTIVE = true;
        }else{
            ISACTIVE = false;
        };


        if(ISACTIVE){
            Corpora.LOGGER.info("is powered at " + pos);
        }
    }
}
