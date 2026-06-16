package corpora.modid.component.cardinal.components;

import corpora.modid.component.cardinal.interfaces.ShellOwnerComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

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
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (tag.contains("owner")) {
            owner = tag.getUuid("owner");
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putUuid("owner", owner);
    }
}
