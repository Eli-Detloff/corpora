package corpora.modid.networking.custom;


import corpora.modid.Corpora;
import corpora.modid.util.ShellDataComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ShellScreenS2CPacket(
        List<ShellDataComponent> shells
) implements CustomPacketPayload {

    public static final Type<ShellScreenS2CPacket> ID =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Corpora.MOD_ID, "shells"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShellScreenS2CPacket> CODEC =
            StreamCodec.composite(

                    ShellDataComponent.LIST_CODEC,
                    ShellScreenS2CPacket::shells,

                    ShellScreenS2CPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}