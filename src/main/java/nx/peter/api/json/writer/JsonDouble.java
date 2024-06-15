package nx.peter.api.json.writer;

public interface JsonDouble extends JsonNumber, nx.peter.api.json.core.JsonDouble {
    default boolean setDouble(Double value) { return setNumber(value); }
}
