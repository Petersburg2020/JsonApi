package nx.peter.api.json.reader;

import nx.peter.api.json.io.Printer;
import org.jetbrains.annotations.NotNull;

import static nx.peter.api.json.core.JsonNull.INSTANCE;

public interface JsonElement
        extends nx.peter.api.json.core.JsonElement, JsonValue, Iterable<JsonValue> {
    Values getValues();

    /** Refresh/restart next or previous iterator */
    void refresh();

    boolean hasNextValue();

    boolean hasPreviousValue();

    JsonValue readNextValue();

    JsonValue readPreviousValue();

    default JsonLong readPreviousLong() {
        while (hasPreviousValue()) if (readPreviousNumber() instanceof JsonLong val) return val;
        return INSTANCE;
    }

    default JsonFloat readPreviousFloat() {
        while (hasPreviousValue()) if (readPreviousNumber() instanceof JsonFloat val) return val;
        return INSTANCE;
    }

    default JsonDouble readPreviousDouble() {
        while (hasPreviousValue()) if (readPreviousNumber() instanceof JsonDouble val) return val;
        return INSTANCE;
    }

    default JsonString readPreviousString() {
        while (hasPreviousValue()) if (readPreviousNative() instanceof JsonString val) return val;
        return INSTANCE;
    }

    default JsonNative readPreviousNative() {
        while (hasPreviousValue()) if (readPreviousValue() instanceof JsonNative val) return val;
        return INSTANCE;
    }

    default JsonNumber readPreviousNumber() {
        while (hasPreviousValue()) if (readPreviousNative() instanceof JsonNumber val) return val;
        return INSTANCE;
    }

    default JsonInteger readPreviousInteger() {
        while (hasPreviousValue()) if (readPreviousNumber() instanceof JsonInteger val) return val;
        return INSTANCE;
    }

    default JsonBoolean readPreviousBoolean() {
        while (hasPreviousValue()) if (readPreviousNative() instanceof JsonBoolean val) return val;
        return INSTANCE;
    }

    default JsonArray readPreviousArray() {
        while (hasPreviousValue()) if (readPreviousElement() instanceof JsonArray arr) return arr;
        return INSTANCE;
    }

    default JsonObject readPreviousObject() {
        while (hasPreviousValue()) if (readPreviousElement() instanceof JsonObject obj) return obj;
        return INSTANCE;
    }

    default JsonElement readPreviousElement() {
        while (hasPreviousValue())
            if (readPreviousValue() instanceof JsonElement element) return element;
        return INSTANCE;
    }

    default JsonLong readNextLong() {
        while (hasNextValue()) if (readNextNumber() instanceof JsonLong val) return val;
        return INSTANCE;
    }

    default JsonFloat readNextFloat() {
        while (hasNextValue()) if (readNextNumber() instanceof JsonFloat val) return val;
        return INSTANCE;
    }

    default JsonDouble readNextDouble() {
        while (hasNextValue()) if (readNextNumber() instanceof JsonDouble val) return val;
        return INSTANCE;
    }

    default JsonString readNextString() {
        while (hasNextValue()) if (readNextNative() instanceof JsonString val) return val;
        return INSTANCE;
    }

    default JsonNative readNextNative() {
        while (hasNextValue()) if (readNextValue() instanceof JsonNative val) return val;
        return INSTANCE;
    }

    default JsonNumber readNextNumber() {
        while (hasNextValue()) if (readNextNative() instanceof JsonNumber val) return val;
        return INSTANCE;
    }

    default JsonInteger readNextInteger() {
        while (hasNextValue()) if (readNextNumber() instanceof JsonInteger val) return val;
        return INSTANCE;
    }

    default JsonBoolean readNextBoolean() {
        while (hasNextValue()) if (readNextNative() instanceof JsonBoolean val) return val;
        return INSTANCE;
    }

    default JsonArray readNextArray() {
        while (hasNextValue()) if (readNextElement() instanceof JsonArray arr) return arr;
        return INSTANCE;
    }

    default JsonObject readNextObject() {
        while (hasNextValue()) if (readNextElement() instanceof JsonObject obj) return obj;
        return INSTANCE;
    }

    default JsonElement readNextElement() {
        while (hasNextValue()) if (readNextValue() instanceof JsonElement element) return element;
        return INSTANCE;
    }

    Printer getPrinter(@NotNull Printer.Type printerType);

    interface Values extends Iterable<JsonValue> {
        boolean hasId(Object id);

        boolean hasValue(Object value);

        JsonElement getParent();

        default boolean isNotEmpty() {
            return !isEmpty();
        }

        JsonValue getLast();

        JsonValue getFirst();

        JsonValue getNext();

        JsonValue getPrevious();

        boolean isEmpty();

        int size();
    }

    interface Names extends Iterable<String> {
        String get(int index);

        boolean isEmpty();

        int indexOf(CharSequence item);

        int size();

        default String getLast() {
            return isNotEmpty() ? get(size() - 1) : null;
        }

        default String getFirst() {
            return isNotEmpty() ? get(0) : null;
        }

        String getNext();

        String getPrevious();

        default boolean isNotEmpty() {
            return !isEmpty();
        }

        boolean contains(CharSequence item);
    }
}
