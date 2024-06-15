package nx.peter.api.json.reader;

public interface JsonString extends JsonNative, nx.peter.api.json.core.JsonString {
    default String getString() { return isString() && isNotNull() ? (String) get() : null; }
}
