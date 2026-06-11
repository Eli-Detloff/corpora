package corpora.modid.item;

import corpora.modid.Corpora;
import corpora.modid.CorporaDataGenerator;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Corpora.MOD_ID, name), item);
    }


    public static void registerModItems() {
        Corpora.LOGGER.info("Registering Mod Items for " + Corpora.MOD_ID);
    }
}
