package corpora.modid.networking.custom;


import corpora.modid.Corpora;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record SelectShellC2SPayload(UUID shellId) implements CustomPayload {

    public static final Id<SelectShellC2SPayload> ID =
            new Id<>(Identifier.of(Corpora.MOD_ID, "select_shell"));

    public static final PacketCodec<RegistryByteBuf, SelectShellC2SPayload> CODEC =
            PacketCodec.tuple(
                    Uuids.PACKET_CODEC,
                    SelectShellC2SPayload::shellId,

                    SelectShellC2SPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}