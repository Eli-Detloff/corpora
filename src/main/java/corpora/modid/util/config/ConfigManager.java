package corpora.modid.util.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting().create();

    private static final Path CONFIG_PATH =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("corpora.json");

    private static ModConfig config;

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                config = GSON.fromJson(
                        Files.readString(CONFIG_PATH),
                        ModConfig.class
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (config == null) {
            config = new ModConfig();
            save();
        }
    }

    public static void save() {
        try {
            Files.writeString(
                    CONFIG_PATH,
                    GSON.toJson(config)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ModConfig getConfig() {
        return config;
    }
}