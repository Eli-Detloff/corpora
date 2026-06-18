package corpora.modid.component.cardinal.interfaces;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.Component;

public interface ServerComponent extends Component {


    void setPos(BlockPos pos);

    void setDimension(RegistryKey<World> dimension);

    BlockPos getPos();

    RegistryKey<World> getDimension();
}
