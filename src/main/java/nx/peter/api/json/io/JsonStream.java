package nx.peter.api.json.io;

import nx.peter.api.json.core.JsonArray;
import nx.peter.api.json.core.JsonElement;
import nx.peter.api.json.core.JsonObject;

public interface JsonStream<JE extends JsonElement, JA extends JsonArray, JO extends JsonObject> {
    JE openRootElement();
    JO openRootObject();
    JA openRootArray();
    boolean isRootElement();
    boolean isRootObject();
    boolean isRootArray();
    void close();
}
