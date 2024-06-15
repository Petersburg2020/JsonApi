package nx.peter.api.json.core;

public interface JsonNative extends JsonValue {
    @Override
    default boolean isNative() {
        return true;
    }
}
