package nx.peter.api.json.reader;

public interface JsonDouble extends JsonNumber, nx.peter.api.json.core.JsonDouble {
    default Double getDOuble() { return isDouble() ? (Double) get() : null; }
}
