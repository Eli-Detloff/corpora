package corpora.modid.networking.custom;


import corpora.modid.Corpora;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record SelectShellC2SPayload(UUID shellId) implements CustomPacketPayload {

    public static final Type<SelectShellC2SPayload> ID =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Corpora.MOD_ID, "select_shell"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SelectShellC2SPayload> CODEC =
            StreamCodec.composite(
                    UUIDUtil.STREAM_CODEC,
                    SelectShellC2SPayload::shellId,

                    SelectShellC2SPayload::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}