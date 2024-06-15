package nx.peter.api.json.reader;

import nx.peter.api.json.core.JsonNull;

public interface JsonArray extends JsonElement, nx.peter.api.json.core.JsonArray {
    JsonValue readValue(int index);
    default JsonLong readLong(int index) { return isDouble() && isNotNull() ? (JsonLong) readValue(index) : JsonNull.INSTANCE; }
    default JsonDouble readDouble(int index) { return isDouble() && isNotNull() ? (JsonDouble) readValue(index) : JsonNull.INSTANCE; }
    default JsonFloat readFloat(int index) { return isDouble() && isNotNull() ? (JsonFloat) readValue(index) : JsonNull.INSTANCE; }
    default JsonInteger readInteger(int index) { return isInteger() && isNotNull() ? (JsonInteger) readValue(index) : JsonNull.INSTANCE; }
    default JsonString readString(int index) { return isString() && isNotNull() ? (JsonString) readValue(index) : JsonNull.INSTANCE; }
    default JsonBoolean readBoolean(int index) { return isBoolean() && isNotNull() ? (JsonBoolean) readValue(index) : JsonNull.INSTANCE; }
    default JsonArray readArray(int index) { return isArray() && isNotNull() ? (JsonArray) readValue(index) : JsonNull.INSTANCE; }
    default JsonObject readObject(int index) { return isObject() ? (JsonObject) readValue(index) : JsonNull.INSTANCE; }
}
