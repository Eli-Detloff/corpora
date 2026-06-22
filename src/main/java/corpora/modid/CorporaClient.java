package corpora.modid;

import corpora.modid.entity.ModEntities;
import corpora.modid.entity.client.ShellModel;
import corpora.modid.entity.client.ShellRenderer;
import corpora.modid.gui.ShellGui;
import corpora.modid.networking.custom.ShellScreenS2CPacket;
import corpora.modid.util.ShellDataComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.network.chat.Component;

import java.util.List;

public class CorporaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //registry
        EntityModelLayerRegistry.registerModelLayer(ShellModel.SHELL, ShellModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.SHELL, ShellRenderer::new);

        //packets
        ClientPlayNetworking.registerGlobalReceiver(ShellScreenS2CPacket.ID,
                (payload, context) -> {


                    List<ShellDataComponent> shells = payload.shells();

                    if (shells.isEmpty()) {
                        context.player().sendSystemMessage(Component.nullToEmpty("List empty"));
                        return;
                    }
                    context.client().setScreen(new ShellGui(shells));

                });
    }
}
