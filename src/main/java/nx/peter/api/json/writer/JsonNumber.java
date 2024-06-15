package nx.peter.api.json.writer;

public interface JsonNumber extends JsonNative, nx.peter.api.json.core.JsonNumber {
    default boolean setNumber(Number value) { return set(value); }
}
