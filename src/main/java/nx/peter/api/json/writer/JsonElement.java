package nx.peter.api.json.writer;

import org.jetbrains.annotations.NotNull;

public interface JsonElement extends JsonValue, nx.peter.api.json.core.JsonElement {

    /**
     * Remove the value from this object
     * @param value given value
     * @return true if value was removed
     */
    boolean removeValue(@NotNull Object value);
    boolean setValue(nx.peter.api.json.core.JsonValue value);
    boolean clear();
}
