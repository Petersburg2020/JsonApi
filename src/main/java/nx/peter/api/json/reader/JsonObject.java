package nx.peter.api.json.reader;

import nx.peter.api.json.core.JsonNull;
import org.jetbrains.annotations.NotNull;

public interface JsonObject extends JsonElement, nx.peter.api.json.core.JsonObject {
    Names getNames();
    boolean hasName(@NotNull CharSequence name);
    JsonValue readValue(@NotNull CharSequence name);
    default JsonLong readLong(@NotNull CharSequence name) { return isDouble() && isNotNull() ? (JsonLong) readValue(name) : JsonNull.INSTANCE; }
    default JsonDouble readDouble(@NotNull CharSequence name) { return isDouble() && isNotNull() ? (JsonDouble) readValue(name) : JsonNull.INSTANCE; }
    default JsonFloat readFloat(@NotNull CharSequence name) { return isDouble() && isNotNull() ? (JsonFloat) readValue(name) : JsonNull.INSTANCE; }
    default JsonInteger readInteger(@NotNull CharSequence name) { return isInteger() && isNotNull() ? (JsonInteger) readValue(name) : JsonNull.INSTANCE; }
    default JsonString readString(@NotNull CharSequence name) { return isString() && isNotNull() ? (JsonString) readValue(name) : JsonNull.INSTANCE; }
    default JsonBoolean readBoolean(@NotNull CharSequence name) { return isBoolean() && isNotNull() ? (JsonBoolean) readValue(name) : JsonNull.INSTANCE; }
    default JsonArray readArray(@NotNull CharSequence name) { return isArray() && isNotNull() ? (JsonArray) readValue(name) : JsonNull.INSTANCE; }
    default JsonObject readObject(@NotNull CharSequence name) { return isObject() ? (JsonObject) readValue(name) : JsonNull.INSTANCE; }
}
