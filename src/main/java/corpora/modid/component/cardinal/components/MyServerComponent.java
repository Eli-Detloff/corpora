package corpora.modid.component.cardinal.components;


import corpora.modid.component.cardinal.interfaces.ServerComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;


public class MyServerComponent implements ServerComponent {

    private BlockPos serverPos;
    private ResourceKey<Level> serverDimension;


    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
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
            ResourceLocation id = ResourceLocation.parse(tag.getString("serverDimension"));

            this.serverDimension = ResourceKey.create(
                    Registries.DIMENSION,
                    ResourceLocation.parse(tag.getString("serverDimension"))
            );
        }


    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        if (this.serverPos != null) {
            int[] tagPos = new int[3];
            tagPos[0] = this.serverPos.getX();
            tagPos[1] = this.serverPos.getY();
            tagPos[2] = this.serverPos.getZ();

            tag.putIntArray("serverPos", tagPos);
        }

        if (this.serverDimension != null) {
            tag.putString("serverDimension", serverDimension.location().toString());
        }
    }

    @Override
    public void setPos(BlockPos pos) {
        this.serverPos = pos;
    }

    @Override
    public void setDimension(ResourceKey<Level> dimension) {
        this.serverDimension = dimension;
    }

    @Override
    public BlockPos getPos() {
        return serverPos;
    }

    @Override
    public ResourceKey<Level> getDimension() {
        return serverDimension;
    }
}
