package nx.peter.api.json.reader;

public interface JsonLong extends JsonNumber, nx.peter.api.json.core.JsonLong {
    default Long getLong() { return isLong() && isNotNull() ? (Long) get() : null; }
}
