package corpora.modid;

import corpora.modid.entity.ModEntities;
import corpora.modid.entity.client.ShellModel;
import corpora.modid.entity.client.ShellRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class CorporaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(ShellModel.SHELL, ShellModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.SHELL, ShellRenderer::new);
    }
}
