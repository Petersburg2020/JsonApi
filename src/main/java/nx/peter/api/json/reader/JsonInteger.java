package nx.peter.api.json.reader;

public interface JsonInteger extends JsonNumber, nx.peter.api.json.core.JsonInteger {
    default Integer getInteger() { return isInteger() && isNotNull() ? (Integer) get() : null; }
}
