package nx.peter.api.json;

import nx.peter.api.json.core.JsonElement;
import nx.peter.api.json.core.JsonNull;
import nx.peter.api.json.writer.JsonValue;
import org.jetbrains.annotations.NotNull;

import static nx.peter.api.json.JsonUtil.isJsonItem;

class IJsonValue implements JsonValue, nx.peter.api.json.reader.JsonValue {
    protected JsonElement parent;
    protected Object value, id;
    protected boolean readable, writable;

    public IJsonValue(Object value) {
        this.value = value;
        parent = JsonNull.INSTANCE;
        readable = false;
        writable = false;
    }

    @Override
    public Object getId() {
        return id;
    }

    public IJsonValue setValue(Object value) {
        this.value = value;
        return this;
    }

    public IJsonValue setId(Object id) {
        this.id = id;
        return this;
    }

    public IJsonValue setParent(JsonElement parent) {
        this.parent = parent;
        if (this.parent != null) {
            readable = this.parent.isReadable();
            writable = this.parent.isWritable();
        }
        return this;
    }

    @Override
    public JsonElement getParent() {
        return parent;
    }

    @Override
    public boolean isReadable() {
        return readable;
    }

    @Override
    public boolean isWritable() {
        return writable;
    }

    @Override
    public Object get() {
        return value;
    }

    @Override
    public boolean set(Object value) {
        this.value = value;
        return true;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return value == null ? -1 : o instanceof IJsonValue val ? String.valueOf(value).compareTo(String.valueOf(val)) : isJsonItem(o) ? String.valueOf(value).compareTo(String.valueOf(o)) : 1;
    }

    @Override
    public String toString() {
        return value != null ? String.valueOf(value) : "";
    }
}
