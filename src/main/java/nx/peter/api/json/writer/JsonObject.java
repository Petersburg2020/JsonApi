package nx.peter.api.json.writer;

import nx.peter.api.json.core.JsonNull;
import nx.peter.api.json.data.Duo;
import nx.peter.api.json.reader.JsonArray;
import nx.peter.api.json.reader.JsonBoolean;
import nx.peter.api.json.reader.JsonDouble;
import nx.peter.api.json.reader.JsonFloat;
import nx.peter.api.json.reader.JsonInteger;
import nx.peter.api.json.reader.JsonLong;
import nx.peter.api.json.reader.JsonString;
import org.jetbrains.annotations.NotNull;

/**
 * Writeable json object
 * @version 1.0
 */
public interface JsonObject extends JsonElement, nx.peter.api.json.core.JsonObject {
    /**
     * Add a new value to the provided name
     * @param name name of value
     * @param value value
     * @return true & json value if name exists or false & json null if name does not exists
     */
    Duo<Boolean, JsonValue> put(@NotNull CharSequence name, Object value);

    /**
     * Remove the value attached to the given name and the name itself
     * from this object
     * @param name provided name
     * @return true & json value if name exists or false & json null if name does not exists
     */
    Duo<Boolean, JsonValue> removeName(@NotNull CharSequence name);

    JsonValue writeValue(@NotNull CharSequence name);
    default JsonLong writeLong(@NotNull CharSequence name) { return isDouble() && isNotNull() ? (JsonLong) writeValue(name) : JsonNull.INSTANCE; }
    default JsonDouble writeDouble(@NotNull CharSequence name) { return isDouble() && isNotNull() ? (JsonDouble) writeValue(name) : JsonNull.INSTANCE; }
    default JsonFloat writeFloat(@NotNull CharSequence name) { return isDouble() && isNotNull() ? (JsonFloat) writeValue(name) : JsonNull.INSTANCE; }
    default JsonInteger writeInteger(@NotNull CharSequence name) { return isInteger() && isNotNull() ? (JsonInteger) writeValue(name) : JsonNull.INSTANCE; }
    default JsonString writeString(@NotNull CharSequence name) { return isString() && isNotNull() ? (JsonString) writeValue(name) : JsonNull.INSTANCE; }
    default JsonBoolean writeBoolean(@NotNull CharSequence name) { return isBoolean() && isNotNull() ? (JsonBoolean) writeValue(name) : JsonNull.INSTANCE; }
    default JsonArray writeArray(@NotNull CharSequence name) { return isArray() && isNotNull() ? (JsonArray) writeValue(name) : JsonNull.INSTANCE; }
    default JsonObject writeObject(@NotNull CharSequence name) { return isObject() ? (JsonObject) writeValue(name) : JsonNull.INSTANCE; }
}
