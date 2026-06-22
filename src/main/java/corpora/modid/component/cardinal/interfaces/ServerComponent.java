package corpora.modid.component.cardinal.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.ladysnake.cca.api.v3.component.Component;

public interface ServerComponent extends Component {


    void setPos(BlockPos pos);

    void setDimension(ResourceKey<Level> dimension);

    BlockPos getPos();

    ResourceKey<Level> getDimension();
}
