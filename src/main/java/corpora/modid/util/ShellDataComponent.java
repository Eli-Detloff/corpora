package corpora.modid.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

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
        ResourceKey<Level> dimension,
        String name,
        float health
) {

    public static final StreamCodec<RegistryFriendlyByteBuf, ShellDataComponent> CODEC =
            StreamCodec.ofMember(
                    (value, buf) -> {
                        UUIDUtil.STREAM_CODEC.encode(buf, value.uuid());
                        UUIDUtil.STREAM_CODEC.encode(buf, value.owner());
                        BlockPos.STREAM_CODEC.encode(buf, value.pos());
                        ByteBufCodecs.FLOAT.encode(buf, value.yaw());
                        ByteBufCodecs.FLOAT.encode(buf, value.pitch());
                        ByteBufCodecs.FLOAT.encode(buf, value.headYaw());
                        ByteBufCodecs.FLOAT.encode(buf, value.bodyYaw());
                        ResourceKey.streamCodec(Registries.DIMENSION)
                                .encode(buf, value.dimension());
                        ByteBufCodecs.STRING_UTF8.encode(buf, value.name());
                        ByteBufCodecs.FLOAT.encode(buf, value.health());
                    },
                    buf -> new ShellDataComponent(
                            UUIDUtil.STREAM_CODEC.decode(buf),
                            UUIDUtil.STREAM_CODEC.decode(buf),
                            BlockPos.STREAM_CODEC.decode(buf),
                            ByteBufCodecs.FLOAT.decode(buf),
                            ByteBufCodecs.FLOAT.decode(buf),
                            ByteBufCodecs.FLOAT.decode(buf),
                            ByteBufCodecs.FLOAT.decode(buf),
                            ResourceKey.streamCodec(Registries.DIMENSION).decode(buf),
                            ByteBufCodecs.STRING_UTF8.decode(buf),
                            ByteBufCodecs.FLOAT.decode(buf)
                    )
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<ShellDataComponent>> LIST_CODEC =
            ByteBufCodecs.collection(
                    ArrayList::new,
                    CODEC
            );
}