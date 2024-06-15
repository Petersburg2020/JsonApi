package nx.peter.api.json.reader;

public interface JsonBoolean extends JsonNative, nx.peter.api.json.core.JsonBoolean {
    default Boolean getBoolean() { return isBoolean() && isNotNull() ? (Boolean) get() : null; }
}
