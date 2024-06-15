package nx.peter.api.json.io;

import org.jetbrains.annotations.NotNull;

public interface Printer {
    @NotNull String print();

    enum Type {
        PrettyPrinter,
        TextPrinter
    }
}
