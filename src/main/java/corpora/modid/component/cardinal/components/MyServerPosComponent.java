package corpora.modid.component.cardinal.components;


import corpora.modid.component.cardinal.interfaces.ServerPosComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;


public class MyServerPosComponent implements ServerPosComponent {

    private BlockPos serverPos;


    @Override
    public void set(BlockPos pos) {
        this.serverPos = pos;
    }

    @Override
    public BlockPos get() {
        return this.serverPos;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (tag.contains("serverPos")) {
            int[] tagPos = tag.getIntArray("serverPos");
            this.serverPos = new BlockPos(
                    tagPos[0],
                    tagPos[1],
                    tagPos[2]
            );
        } else {
            this.serverPos = new BlockPos(0, 0, 0);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (this.serverPos != null) {
            int[] tagPos = new int[3];
            tagPos[0] = this.serverPos.getX();
            tagPos[1] = this.serverPos.getY();
            tagPos[2] = this.serverPos.getZ();

            tag.putIntArray("serverPos", tagPos);
        }
    }
}
