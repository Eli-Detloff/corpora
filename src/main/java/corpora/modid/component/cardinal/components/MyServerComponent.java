package corpora.modid.component.cardinal.components;


import corpora.modid.component.cardinal.interfaces.ServerComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class MyServerComponent implements ServerComponent {

    private BlockPos serverPos;
    private RegistryKey<World> serverDimension;


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

        if (tag.contains("serverDimension")) {
            Identifier id = Identifier.of(tag.getString("serverDimension"));

            this.serverDimension = RegistryKey.of(
                    RegistryKeys.WORLD,
                    Identifier.of(tag.getString("serverDimension"))
            );
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

        if (this.serverDimension != null) {
            tag.putString("serverDimension", serverDimension.getValue().toString());
        }
    }

    @Override
    public void setPos(BlockPos pos) {
        this.serverPos = pos;
    }

    @Override
    public void setDimension(RegistryKey<World> dimension) {
        this.serverDimension = dimension;
    }

    @Override
    public BlockPos getPos() {
        return serverPos;
    }

    @Override
    public RegistryKey<World> getDimension() {
        return serverDimension;
    }
}
