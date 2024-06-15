package nx.peter.api.json.writer;

public interface JsonFloat extends JsonNumber, nx.peter.api.json.core.JsonFloat {
    default boolean setFloat(Float value) { return setNumber(value); }
}
