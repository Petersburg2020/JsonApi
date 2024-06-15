package nx.peter.api.json.core;

import java.io.Serializable;

/**
 * Json value holder.
 */
public interface JsonValue extends Serializable, Comparable<Object> {
    /**
     * ID is either the index of an array item or the name of an object item.
     * @return null if value is not in a parent and vice versa
     */
    Object getId();

    /**
     * Get the parent of this value.
     * @return value's parent
     */
    JsonElement getParent();

    /**
     * Check if it is the root of this json file
     * @return true if it is the root element (object | array)
     */
    default boolean isRootElement() { return !hasParent() && isElement() && isNotNull(); }
    default boolean hasParent() { return getParent().isNotNull(); }
    default boolean isWritable() { return false; }
    default boolean isReadable() { return false; }
    default boolean isArray() { return this instanceof JsonArray && isNotNull(); }
    default boolean isObject() { return this instanceof JsonObject && isNotNull(); }
    default boolean isElement() { return this instanceof JsonElement && isNotNull(); }
    default boolean isBoolean() { return this instanceof JsonBoolean && isNotNull(); }
    default boolean isInteger() { return this instanceof JsonInteger && isNotNull(); }
    default boolean isString() { return this instanceof JsonString && isNotNull(); }
    default boolean isDouble() { return this instanceof JsonInteger && isNotNull(); }
    default boolean isFloat() { return this instanceof JsonInteger && isNotNull(); }
    default boolean isLong() { return this instanceof JsonInteger && isNotNull(); }
    default boolean isNative() { return this instanceof JsonNative && isNotNull(); }
    default boolean isNumber() { return this instanceof JsonNumber && isNotNull(); }
    default boolean isNull() { return this instanceof JsonNull; }
    default boolean isNotNull() { return !isNull(); }
}
