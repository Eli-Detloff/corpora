package corpora.modid.init;

import corpora.modid.Corpora;
import corpora.modid.item.ShellItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems {

    public static final Item SHELL_ITEM = registerItem("shell_item", new ShellItem(new Item.Properties()));
    public static final Item SHELL_ITEM_BROKEN = registerItem("shell_item_broken", new Item(new Item.Properties()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Corpora.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Corpora.LOGGER.info("Registering Mod Items for " + Corpora.MOD_ID);
    }
}
