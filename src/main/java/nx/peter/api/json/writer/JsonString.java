package nx.peter.api.json.writer;

public interface JsonString extends JsonNative, nx.peter.api.json.core.JsonString {
    default boolean setString(CharSequence value) { return set(value != null ? value.toString() : null); }
}
