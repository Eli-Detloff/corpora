package corpora.modid.component.cardinal.components;


import corpora.modid.component.cardinal.interfaces.CurrentShellComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class MyCurrentShellComponent implements CurrentShellComponent {
    private String name = null;

    @Override
    public String get() {
        return name;
    }

    @Override
    public void set(String name) {
        this.name = name;
    }

    @Override
    public void clear() {
        this.name = null;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (tag.contains("name")) {
            this.name = tag.getString("name");
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (this.name != null) {
            tag.putString("name", name);
        }

    }
}