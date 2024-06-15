package nx.peter.api.json;

import nx.peter.api.json.core.JsonNull;
import nx.peter.api.json.data.Duo;
import nx.peter.api.json.io.PrettyPrinter;
import nx.peter.api.json.io.Printer;
import nx.peter.api.json.io.TextPrinter;
import nx.peter.api.json.reader.JsonElement.Names;
import nx.peter.api.json.reader.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;

class IJsonNull extends IJsonValue implements JsonNull {
    public IJsonNull() {
        super(null);
    }

    @Override
    public boolean set(Object value) {
        return false;
    }

    @Override
    public Object get() {
        return null;
    }

    @Override
    public JsonValue readValue(int index) {
        return this;
    }

    @Override
    public Values getValues() {
        return new IJsonElement.IValues(this, Collections.emptyList());
    }

    @Override
    public void refresh() {}

    @Override
    public boolean hasNextValue() {
        return false;
    }

    @Override
    public boolean hasPreviousValue() {
        return false;
    }

    @Override
    public JsonValue readNextValue() {
        return this;
    }

    @Override
    public JsonValue readPreviousValue() {
        return this;
    }

    @Override
    public Printer getPrinter(Printer.@NotNull Type printerType) {
        return printerType == Printer.Type.PrettyPrinter
                ? (PrettyPrinter) () -> ""
                : (TextPrinter) () -> "";
    }

    @NotNull
    @Override
    public Iterator<JsonValue> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean hasValue(Object value) {
        return false;
    }

    @Override
    public boolean removeValue(@NotNull Object value) {
        return false;
    }

    @Override
    public boolean setValue(nx.peter.api.json.core.JsonValue value) {
        return false;
    }

    @Override
    public boolean clear() {
        return false;
    }

    @Override
    public JsonValue readValue(@NotNull CharSequence name) {
        return this;
    }

    @Override
    public Duo<Boolean, nx.peter.api.json.writer.JsonValue> put(
            @NotNull CharSequence name, Object value) {
        return new IJsonElement.IDuo<>(false, this);
    }

    @Override
    public Duo<Boolean, nx.peter.api.json.writer.JsonValue> removeName(@NotNull CharSequence name) {
        return new IJsonElement.IDuo<>(false, this);
    }

    @Override
    public nx.peter.api.json.writer.JsonValue writeValue(@NotNull CharSequence name) {
        return this;
    }

    @Override
    public Duo<Boolean, nx.peter.api.json.writer.JsonValue> put(int index, Object value) {
        return new IJsonElement.IDuo<>(false, this);
    }

    @Override
    public Duo<Boolean, nx.peter.api.json.writer.JsonValue> removeIndex(int index) {
        return new IJsonElement.IDuo<>(false, this);
    }

    @Override
    public boolean put(Object value) {
        return false;
    }

    @Override
    public nx.peter.api.json.writer.JsonValue writeValue(int index) {
        return this;
    }

    @Override
    public Names getNames() {
        return new IJsonElement.INames(Collections.emptyList());
    }

    @Override
    public boolean hasName(CharSequence name) {
        return false;
    }
}
