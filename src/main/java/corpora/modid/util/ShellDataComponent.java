package corpora.modid.util;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ShellDataComponent(
        UUID uuid,
        UUID owner,
        BlockPos pos,
        float yaw,
        float pitch,
        float headYaw,
        float bodyYaw,
        RegistryKey<World> dimension,
        String name,
        float health
) {

    public static final PacketCodec<RegistryByteBuf, ShellDataComponent> CODEC =
            PacketCodec.of(
                    (value, buf) -> {
                        Uuids.PACKET_CODEC.encode(buf, value.uuid());
                        Uuids.PACKET_CODEC.encode(buf, value.owner());
                        BlockPos.PACKET_CODEC.encode(buf, value.pos());
                        PacketCodecs.FLOAT.encode(buf, value.yaw());
                        PacketCodecs.FLOAT.encode(buf, value.pitch());
                        PacketCodecs.FLOAT.encode(buf, value.headYaw());
                        PacketCodecs.FLOAT.encode(buf, value.bodyYaw());
                        RegistryKey.createPacketCodec(RegistryKeys.WORLD)
                                .encode(buf, value.dimension());
                        PacketCodecs.STRING.encode(buf, value.name());
                        PacketCodecs.FLOAT.encode(buf, value.health());
                    },
                    buf -> new ShellDataComponent(
                            Uuids.PACKET_CODEC.decode(buf),
                            Uuids.PACKET_CODEC.decode(buf),
                            BlockPos.PACKET_CODEC.decode(buf),
                            PacketCodecs.FLOAT.decode(buf),
                            PacketCodecs.FLOAT.decode(buf),
                            PacketCodecs.FLOAT.decode(buf),
                            PacketCodecs.FLOAT.decode(buf),
                            RegistryKey.createPacketCodec(RegistryKeys.WORLD).decode(buf),
                            PacketCodecs.STRING.decode(buf),
                            PacketCodecs.FLOAT.decode(buf)
                    )
            );

    public static final PacketCodec<RegistryByteBuf, List<ShellDataComponent>> LIST_CODEC =
            PacketCodecs.collection(
                    ArrayList::new,
                    CODEC
            );
}