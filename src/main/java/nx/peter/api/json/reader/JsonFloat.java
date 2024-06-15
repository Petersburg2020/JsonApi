package nx.peter.api.json.reader;

public interface JsonFloat extends JsonNumber, nx.peter.api.json.core.JsonFloat {
    default Float getFloat() { return isFloat() && isNotNull() ? (Float) get() : null; }
}
