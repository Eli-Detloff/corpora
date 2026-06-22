package corpora.modid.component.cardinal.components;


import corpora.modid.component.cardinal.interfaces.CurrentShellComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

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
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        if (tag.contains("name")) {
            this.name = tag.getString("name");
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        if (this.name != null) {
            tag.putString("name", name);
        }

    }
}