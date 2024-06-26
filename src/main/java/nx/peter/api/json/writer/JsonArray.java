package nx.peter.api.json.writer;

import nx.peter.api.json.core.JsonNull;
import nx.peter.api.json.data.Duo;

/**
 * This is the writable class for {@link nx.peter.api.json.core.JsonArray JsonArray}
 */
public interface JsonArray extends JsonElement, nx.peter.api.json.core.JsonArray {
    /**
     * Add a new value at the provided index
     *
     * @param index index of value
     * @param value value
     * @return true & json value if index exists or false & json null if index does not exists
     */
    Duo<Boolean, JsonValue> put(int index, Object value);

    /**
     * Remove the value at the given index
     * from this array
     *
     * @param index provided index
     * @return true & json value if index exists or false & json null if index does not exists
     */
    Duo<Boolean, JsonValue> removeIndex(int index);

    /**
     * Add a new value to this array
     *
     * @param value given value
     * @return true if added & false if not added
     */
    boolean put(Object value);

    /**
     * Get writable {@link JsonValue JsonValue} at given index
     *
     * @param index given index
     * @return JsonValue
     */
    JsonValue writeValue(int index);

    default JsonLong writeLong(int index) {
        return isDouble() && isNotNull() ? (JsonLong) writeValue(index) : JsonNull.INSTANCE;
    }

    /**
     * Get writable {@link JsonDouble JsonDouble} at given index
     *
     * @param index given index
     * @return JsonDouble
     */
    default JsonDouble writeDouble(int index) {
        return isDouble() && isNotNull() ? (JsonDouble) writeValue(index) : JsonNull.INSTANCE;
    }

    /**
     * Get writable {@link JsonFloat JsonFloat} at given index
     *
     * @param index given index
     * @return JsonFloat
     */
    default JsonFloat writeFloat(int index) {
        return isDouble() && isNotNull() ? (JsonFloat) writeValue(index) : JsonNull.INSTANCE;
    }

    /**
     * Get writable {@link JsonInteger JsonInteger} at given index
     *
     * @param index given index
     * @return JsonInteger
     */
    default JsonInteger writeInteger(int index) {
        return isInteger() && isNotNull() ? (JsonInteger) writeValue(index) : JsonNull.INSTANCE;
    }

    /**
     * Get writable {@link JsonString JsonString} at given index
     *
     * @param index given index
     * @return JsonString
     */
    default JsonString writeString(int index) {
        return isString() && isNotNull() ? (JsonString) writeValue(index) : JsonNull.INSTANCE;
    }

    /**
     * Get writable {@link JsonBoolean JsonBoolean} at given index
     *
     * @param index given index
     * @return JsonBoolean
     */
    default JsonBoolean writeBoolean(int index) {
        return isBoolean() && isNotNull() ? (JsonBoolean) writeValue(index) : JsonNull.INSTANCE;
    }

    /**
     * Get writable {@link JsonArray JsonArray} at given index
     *
     * @param index given index
     * @return JsonArray
     */
    default JsonArray writeArray(int index) {
        return isArray() && isNotNull() ? (JsonArray) writeValue(index) : JsonNull.INSTANCE;
    }

    /**
     * Get writable {@link JsonObject JsonObject} at given index
     *
     * @param index given index
     * @return JsonObject
     */
    default JsonObject writeObject(int index) {
        return isObject() ? (JsonObject) writeValue(index) : JsonNull.INSTANCE;
    }
}
