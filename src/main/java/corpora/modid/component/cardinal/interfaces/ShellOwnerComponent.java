package corpora.modid.component.cardinal.interfaces;


import org.ladysnake.cca.api.v3.component.Component;

import java.util.UUID;

public interface ShellOwnerComponent extends Component {
    void set(UUID owner);

    UUID get();
}
