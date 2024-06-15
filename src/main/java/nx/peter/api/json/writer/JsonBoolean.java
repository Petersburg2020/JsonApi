package nx.peter.api.json.writer;

public interface JsonBoolean extends JsonNative, nx.peter.api.json.core.JsonBoolean {
    default boolean setBoolean(Boolean value) { return set(value); }
}
