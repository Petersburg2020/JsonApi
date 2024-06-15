package nx.peter.api.json.writer;

public interface JsonFloat extends JsonNumber, nx.peter.api.json.core.JsonFloat {
    /**
     * Set the float value
     * @param value float value
     * @return true if set was successful or false if otherwise
     */
    default boolean setFloat(Float value) { return setNumber(value); }
}
