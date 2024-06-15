package nx.peter.api.json;

import nx.peter.api.json.data.Duo;
import nx.peter.api.json.reader.JsonArray;
import nx.peter.api.json.reader.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static nx.peter.api.json.JsonUtil.isJsonItem;
import static nx.peter.api.json.JsonUtil.newValue;
import static nx.peter.api.json.core.JsonNull.INSTANCE;

class IJsonArray extends IJsonElement implements JsonArray, nx.peter.api.json.writer.JsonArray {

    public IJsonArray(@NotNull List<Object> array) {
        super(array);
    }

    @Override
    public JsonValue readValue(int index) {
        return ((IJsonValue) newValue(array.get(index))).setId(index).setParent(this);
    }

    @Override
    public boolean removeValue(@NotNull Object value) {
        return value instanceof JsonValue val && val.isNotNull() ? array.remove(val.get()) : isJsonItem(value) && array.remove(value);
    }

    @Override
    public Duo<Boolean, nx.peter.api.json.writer.JsonValue> put(int index, Object value) {
        var result = readValue(index);
        var added = isJsonItem(value) && index >= 0 && index < size() && array.remove(index) != null;
        if (added) array.add(index, value);
        return new IDuo<>(added, (nx.peter.api.json.writer.JsonValue) result);
    }

    @Override
    public Duo<Boolean, nx.peter.api.json.writer.JsonValue> removeIndex(int index) {
        var added = index >= 0 && index < size();
        var result = added ? array.remove(index) : null;
        return new IDuo<>(added, (nx.peter.api.json.writer.JsonValue) newValue(result));
    }

    @Override
    public boolean put(Object value) {
        var val = value instanceof JsonValue v && v.isNotNull() ? v.get() : isJsonItem(value) ? value : null;
        if (val == null) return false;
        array.add(val);
        return true;
    }

    @Override
    public nx.peter.api.json.writer.JsonValue writeValue(int index) {
        return ((IJsonValue) newValue(array.get(index))).setId(index).setParent(this);
    }

    @Override
    public JsonValue readNextValue() {
        return hasNextValue() ? readValue(++currentIndex) : INSTANCE;
    }

    @Override
    public JsonValue readPreviousValue() {
        return hasPreviousValue() ? readValue(--currentIndex) : INSTANCE;
    }

}
