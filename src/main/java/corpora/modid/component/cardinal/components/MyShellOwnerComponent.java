package corpora.modid.component.cardinal.components;

import corpora.modid.component.cardinal.interfaces.ShellOwnerComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class MyShellOwnerComponent implements ShellOwnerComponent {
    private UUID owner;

    @Override
    public void set(UUID owner) {
        this.owner = owner;
    }

    @Override
    public UUID get() {
        return owner;
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        if (tag.contains("owner")) {
            owner = tag.getUUID("owner");
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        if (owner != null) {
            tag.putUUID("owner", owner);
        }

    }
}
