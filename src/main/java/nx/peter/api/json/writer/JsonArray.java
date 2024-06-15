package nx.peter.api.json.writer;

import nx.peter.api.json.core.JsonNull;
import nx.peter.api.json.data.Duo;
import nx.peter.api.json.reader.JsonBoolean;
import nx.peter.api.json.reader.JsonDouble;
import nx.peter.api.json.reader.JsonFloat;
import nx.peter.api.json.reader.JsonInteger;
import nx.peter.api.json.reader.JsonLong;
import nx.peter.api.json.reader.JsonObject;
import nx.peter.api.json.reader.JsonString;

public interface JsonArray extends JsonElement, nx.peter.api.json.core.JsonArray {
    /**
     * Add a new value at the provided index
     * @param index index of value
     * @param value value
     * @return true & json value if index exists or false & json null if index does not exists
     */
    Duo<Boolean, JsonValue> put(int index, Object value);

    /**
     * Remove the value at the given index
     * from this array
     * @param index provided index
     * @return true & json value if index exists or false & json null if index does not exists
     */
    Duo<Boolean, JsonValue> removeIndex(int index);

    /**
     * Add a new value to this array
     * @param value given value
     * @return true if added & false if not added
     */
    boolean put(Object value);

    JsonValue writeValue(int index);
    default JsonLong writeLong(int index) { return isDouble() && isNotNull() ? (JsonLong) writeValue(index) : JsonNull.INSTANCE; }
    default JsonDouble writeDouble(int index) { return isDouble() && isNotNull() ? (JsonDouble) writeValue(index) : JsonNull.INSTANCE; }
    default JsonFloat writeFloat(int index) { return isDouble() && isNotNull() ? (JsonFloat) writeValue(index) : JsonNull.INSTANCE; }
    default JsonInteger writeInteger(int index) { return isInteger() && isNotNull() ? (JsonInteger) writeValue(index) : JsonNull.INSTANCE; }
    default JsonString writeString(int index) { return isString() && isNotNull() ? (JsonString) writeValue(index) : JsonNull.INSTANCE; }
    default JsonBoolean writeBoolean(int index) { return isBoolean() && isNotNull() ? (JsonBoolean) writeValue(index) : JsonNull.INSTANCE; }
    default JsonArray writeArray(int index) { return isArray() && isNotNull() ? (JsonArray) writeValue(index) : JsonNull.INSTANCE; }
    default JsonObject writeObject(int index) { return isObject() ? (JsonObject) writeValue(index) : JsonNull.INSTANCE; }
}
