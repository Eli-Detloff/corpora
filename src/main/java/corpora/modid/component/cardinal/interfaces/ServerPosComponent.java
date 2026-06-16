package corpora.modid.component.cardinal.interfaces;

import net.minecraft.util.math.BlockPos;
import org.ladysnake.cca.api.v3.component.Component;

public interface ServerPosComponent extends Component {
    void set(BlockPos pos);

    BlockPos get();
}
