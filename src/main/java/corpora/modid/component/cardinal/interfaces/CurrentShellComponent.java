package corpora.modid.component.cardinal.interfaces;


import org.ladysnake.cca.api.v3.component.Component;

public interface CurrentShellComponent extends Component {
    String get();

    void set(String name);

    void clear();
}
