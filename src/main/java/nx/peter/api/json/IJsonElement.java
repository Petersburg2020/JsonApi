package nx.peter.api.json;

import nx.peter.api.json.data.Duo;
import nx.peter.api.json.io.PrettyPrinter;
import nx.peter.api.json.io.Printer;
import nx.peter.api.json.io.TextPrinter;
import nx.peter.api.json.reader.*;
import nx.peter.api.json.writer.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static nx.peter.api.json.JsonUtil.*;

abstract class IJsonElement extends IJsonValue
        implements JsonElement, nx.peter.api.json.reader.JsonElement {
    protected final Map<String, Object> object;
    protected final List<Object> array;
    protected int indent = 0, currentIndex = 0;

    public IJsonElement(@NotNull List<Object> array) {
        super(null);
        this.array = JsonUtil.resolve(array);
        set(this.array);
        object = null;
    }

    public IJsonElement(@NotNull Map<String, Object> object) {
        super(null);
        this.object = JsonUtil.resolve(object);
        set(this.object);
        array = null;
    }

    @Override
    public Printer getPrinter(@NotNull Printer.Type printerType) {
        return printerType == Printer.Type.PrettyPrinter
                ? (PrettyPrinter)
                        () ->
                                isArray()
                                        ? toPrettyJson(array, indent)
                                        : isObject() ? toPrettyJson(object, indent) : ""
                : (TextPrinter)
                        () ->
                                isArray()
                                        ? toJson(array, indent)
                                        : isObject() ? toJson(object, indent) : "";
    }

    @Override
    public boolean isArray() {
        return super.isArray() && array != null;
    }

    @Override
    public boolean isObject() {
        return super.isObject() && object != null;
    }

    @Override
    public IJsonValue setParent(nx.peter.api.json.core.JsonElement parent) {
        indent = ((IJsonElement) parent).indent + 1;
        return super.setParent(parent);
    }

    @NotNull
    @Override
    public Iterator<JsonValue> iterator() {
        return getValues().iterator();
    }

    @Override
    public int size() {
        return isArray() && array != null
                ? array.size()
                : isObject() && object != null ? object.size() : 0;
    }

    @Override
    public boolean hasValue(Object value) {
        var val = value instanceof JsonValue v ? v.get() : value;
        return isArray() && array != null
                ? array.contains(val)
                : (isObject() && object != null && object.containsValue(val));
    }

    @Override
    public boolean setValue(nx.peter.api.json.core.JsonValue value) {
        if (value instanceof IJsonElement val) {
            clear();
            if (value.isArray() && val.array != null && array != null)
                return array.addAll(val.array);
            else if (value.isObject() && val.object != null && object != null) {
                object.putAll(val.object);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean clear() {
        if (isObject() && object != null) {
            object.clear();
            return true;
        } else if (isArray() && array != null) {
            array.clear();
            return true;
        }
        return false;
    }

    @Override
    public Values getValues() {
        return isObject() && object != null
                ? new IValues(this, object)
                : new IValues(this, isArray() && array != null ? array : new ArrayList<>());
    }

    @Override
    public void refresh() {
        currentIndex = 0;
    }

    @Override
    public boolean hasNextValue() {
        return currentIndex < size();
    }

    @Override
    public boolean hasPreviousValue() {
        return currentIndex > 1;
    }

    @Override
    public String toString() {
        return getPrinter(Printer.Type.TextPrinter).print();
    }

    public static class IJsonNative extends IJsonValue
            implements JsonNative, nx.peter.api.json.writer.JsonNative {
        public IJsonNative(Object value) {
            super(value);
        }
    }

    public static class IJsonBoolean extends IJsonNative
            implements JsonBoolean, nx.peter.api.json.writer.JsonBoolean {
        public IJsonBoolean(Boolean value) {
            super(value);
        }
    }

    public static class IJsonString extends IJsonNative
            implements JsonString, nx.peter.api.json.writer.JsonString {
        public IJsonString(CharSequence value) {
            super(value);
        }
    }

    public static class IJsonNumber extends IJsonNative
            implements JsonNumber, nx.peter.api.json.writer.JsonNumber {
        public IJsonNumber(Number value) {
            super(value);
        }
    }

    public static class IJsonInteger extends IJsonNumber
            implements JsonInteger, nx.peter.api.json.writer.JsonInteger {
        public IJsonInteger(Integer value) {
            super(value);
        }
    }

    public static class IJsonLong extends IJsonNumber
            implements JsonLong, nx.peter.api.json.writer.JsonLong {
        public IJsonLong(Long value) {
            super(value);
        }
    }

    public static class IJsonFloat extends IJsonNumber
            implements JsonFloat, nx.peter.api.json.writer.JsonFloat {
        public IJsonFloat(Float value) {
            super(value);
        }
    }

    public static class IJsonDouble extends IJsonNumber
            implements JsonDouble, nx.peter.api.json.writer.JsonDouble {
        public IJsonDouble(Double value) {
            super(value);
        }
    }

    public static class INames implements Names {
        List<String> names;
        int currentIndex;

        public INames(List<String> names) {
            this.names = names;
            currentIndex = -1;
        }

        @Override
        public Iterator<String> iterator() {
            return names.iterator();
        }

        @Override
        public String get(int index) {
            currentIndex = index < size() && index >= 0 ? index : currentIndex;
            return names.get(index);
        }

        @Override
        public boolean isEmpty() {
            return names.isEmpty();
        }

        @Override
        public int indexOf(CharSequence item) {
            return names.indexOf(item.toString());
        }

        @Override
        public int size() {
            return names.size();
        }

        @Override
        public String getNext() {
            return currentIndex < size() - 1 ? get(++currentIndex) : null; 
        }

        @Override
        public String getPrevious() {
            return currentIndex < size() - 1 ? get(--currentIndex) : null;
        }

        @Override
        public boolean contains(CharSequence item) {
            return names.contains(item.toString());
        }
    }

    public static class IValues implements Values {
        final nx.peter.api.json.core.JsonElement parent;
        final Map<Object, JsonValue> valuesWithId;
        final List<Object> values;
        int currentIndex;

        public IValues(nx.peter.api.json.core.JsonElement parent, @NotNull List<Object> values) {
            this.parent = parent;
            currentIndex = -1;
            this.values = new ArrayList<>();
            this.valuesWithId = new HashMap<>();

            for (int id = 0; id < values.size(); id++) {
                var item = values.get(id);
                if (isJsonItem(item))
                    valuesWithId.put(id, ((IJsonValue) newValue(item)).setParent(parent).setId(id));
            }
        }

        public IValues(
                nx.peter.api.json.core.JsonElement parent, @NotNull Map<String, Object> values) {
            this.parent = parent;
            this.values = new ArrayList<>();
            this.valuesWithId = new HashMap<>();

            for (var name : values.keySet()) {
                var item = values.get(name);
                if (isJsonItem(item))
                    valuesWithId.put(
                            name, ((IJsonValue) newValue(item)).setParent(parent).setId(name));
            }
        }

        @NotNull
        @Override
        public Iterator<JsonValue> iterator() {
            return valuesWithId.values().iterator();
        }

        @Override
        public boolean hasId(Object id) {
            return valuesWithId.containsKey(id);
        }

        @Override
        public boolean hasValue(Object value) {
            return JsonUtil.isNative(value)
                    ? values.contains(value)
                    : value instanceof IJsonValue val && valuesWithId.containsValue(val);
        }

        @Override
        public nx.peter.api.json.reader.JsonElement getParent() {
            return (nx.peter.api.json.reader.JsonElement) parent;
        }

        @Override
        public boolean isEmpty() {
            return values.isEmpty();
        }

        @Override
        public int size() {
            return values.size();
        }

        @Override
        public String toString() {
            return super.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof List<?> && values.equals(obj))
                    || (obj instanceof Values && values.equals(((IValues) obj).values));
        }

        @Override
        public JsonValue getLast() {
            return get(size() - 1);
        }

        JsonValue get(int index) {
            currentIndex = index < size() && index >= 0 ? index : currentIndex;
            return (JsonValue) valuesWithId.values().toArray()[index];
        }

        @Override
        public JsonValue getFirst() {
            return get(0);
        }

        @Override
        public JsonValue getNext() {
            return currentIndex < size() - 1 ? get(++currentIndex) : null;
        }

        @Override
        public JsonValue getPrevious() {
            return currentIndex > 0 ? get(--currentIndex) : null;
        }
    }

    public static class IDuo<First, Second> implements Duo<First, Second> {
        final First first;
        final Second second;

        public IDuo(First first, Second second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public First getFirst() {
            return first;
        }

        @Override
        public Second getSecond() {
            return second;
        }
    }
}
