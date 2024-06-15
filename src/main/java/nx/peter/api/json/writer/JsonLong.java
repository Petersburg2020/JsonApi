package nx.peter.api.json.writer;

public interface JsonLong extends JsonNumber, nx.peter.api.json.core.JsonLong {
    default boolean setLong(Long value) { return setNumber(value); }
}
