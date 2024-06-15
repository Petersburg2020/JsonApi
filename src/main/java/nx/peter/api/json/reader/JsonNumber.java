package nx.peter.api.json.reader;

public interface JsonNumber extends JsonNative, nx.peter.api.json.core.JsonNumber {
    default Number getNumber() { return isNumber() && isNotNull() ? (Number) get() : null; }
}
