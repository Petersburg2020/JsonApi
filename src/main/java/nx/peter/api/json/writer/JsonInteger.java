package nx.peter.api.json.writer;

public interface JsonInteger extends JsonNumber, nx.peter.api.json.core.JsonInteger {
    default boolean setInteger(Integer value) { return setNumber(value); }
}
