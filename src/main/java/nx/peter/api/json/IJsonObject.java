package nx.peter.api.json;

import nx.peter.api.json.data.Duo;
import nx.peter.api.json.reader.JsonObject;
import nx.peter.api.json.reader.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import static nx.peter.api.json.JsonUtil.isJsonItem;
import static nx.peter.api.json.JsonUtil.newValue;
import static nx.peter.api.json.core.JsonNull.INSTANCE;

class IJsonObject extends IJsonElement implements JsonObject, nx.peter.api.json.writer.JsonObject {
    public IJsonObject(@NotNull Map<String, Object> object) {
        super(object);
    }

    @Override
    public JsonValue readValue(@NotNull CharSequence name) {
        return ((IJsonValue) newValue(object.get(name.toString())))
                .setId(name.toString())
                .setParent(this);
    }

    @Override
    public Duo<Boolean, nx.peter.api.json.writer.JsonValue> put(
            @NotNull CharSequence name, Object value) {
        Object result = null;
        if (isJsonItem(value)) {
            value = value instanceof CharSequence v ? v.toString() : value;
            result = object.put(name.toString(), value);
        }
        return new IDuo<>(isJsonItem(value), (nx.peter.api.json.writer.JsonValue) newValue(result));
    }

    @Override
    public Duo<Boolean, nx.peter.api.json.writer.JsonValue> removeName(@NotNull CharSequence name) {
        var result = object.remove(name.toString());
        return new IDuo<>(result != null, (nx.peter.api.json.writer.JsonValue) newValue(result));
    }

    @Override
    public nx.peter.api.json.writer.JsonValue writeValue(@NotNull CharSequence name) {
        return ((IJsonValue) newValue(object.get(name.toString())))
                .setId(name.toString())
                .setParent(this);
    }

    @Override
    public boolean removeValue(@NotNull Object value) {
        return value instanceof JsonValue val && val.isNotNull()
                ? object.values().remove(val.get())
                : isJsonItem(value) && object.values().remove(value);
    }

    @Override
    public JsonValue readNextValue() {
        return hasNextValue()
                ? readValue(String.valueOf(object.keySet().toArray()[++currentIndex]))
                : INSTANCE;
    }

    @Override
    public JsonValue readPreviousValue() {
        return hasPreviousValue()
                ? readValue(String.valueOf(object.keySet().toArray()[--currentIndex]))
                : INSTANCE;
    }

    @Override
    public boolean hasName(CharSequence name) {
        return object.containsKey(name.toString());
    }
    @Override
    public Names getNames() {
        return new INames(new ArrayList<>(object.keySet()));
    }
}
