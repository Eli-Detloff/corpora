package corpora.modid.networking.custom;


import corpora.modid.Corpora;
import corpora.modid.util.ShellDataComponent;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;

public record ShellScreenS2CPacket(
        List<ShellDataComponent> shells
) implements CustomPayload {

    public static final Id<ShellScreenS2CPacket> ID =
            new Id<>(Identifier.of(Corpora.MOD_ID, "shells"));

    public static final PacketCodec<RegistryByteBuf, ShellScreenS2CPacket> CODEC =
            PacketCodec.tuple(

                    ShellDataComponent.LIST_CODEC,
                    ShellScreenS2CPacket::shells,

                    ShellScreenS2CPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}