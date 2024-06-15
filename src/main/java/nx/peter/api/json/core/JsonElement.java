package nx.peter.api.json.core;

public interface JsonElement extends JsonValue {
    int size();
    boolean hasValue(Object value);
    default boolean isEmpty() { return size() == 0; }
    default boolean isNotEmpty() { return !isEmpty(); }
}
