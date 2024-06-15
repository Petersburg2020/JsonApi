package nx.peter.api.json.io;

import nx.peter.api.json.Json;
import nx.peter.api.json.writer.JsonArray;
import nx.peter.api.json.writer.JsonElement;
import nx.peter.api.json.writer.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static nx.peter.api.json.JsonUtil.newArray;
import static nx.peter.api.json.JsonUtil.newObject;
import static nx.peter.api.json.core.JsonNull.INSTANCE;

public class JsonWriter implements JsonStream<JsonElement, JsonArray, JsonObject> {
    JsonElement root;
    OutputStream stream;

    JsonWriter(@NotNull JsonElement root, @NotNull OutputStream stream) {
        this.stream = stream;
        this.root = root;
    }

    public static @NotNull JsonWriter createArray(@NotNull OutputStream stream) {
        return fromText("[\n\t\n]", stream);
    }

    public static @NotNull JsonWriter createObject(@NotNull OutputStream stream) {
        return fromText("{\n\t\n}", stream);
    }

    public static <T> @NotNull JsonWriter fromModel(T model, @NotNull OutputStream stream) {
        return fromRoot(Json.toJson(model), stream);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull JsonWriter fromRoot(@NotNull nx.peter.api.json.core.JsonElement root, @NotNull OutputStream stream) {
        return new JsonWriter((JsonElement) root, stream);
    }

    @Contract("_, _ -> new")
    public static @NotNull JsonWriter fromFile(@NotNull CharSequence path, @NotNull OutputStream stream) {
        return new JsonWriter((JsonElement) JsonReader.fromFile(path).openRootElement(), stream);
    }

    @Contract("_, _ -> new")
    public static <K, T> @NotNull JsonWriter fromMap(@NotNull Map<K, T> map, @NotNull OutputStream stream) {
        return new JsonWriter((JsonElement) JsonReader.fromMap(map).openRootElement(), stream);
    }

    @SafeVarargs
    public static <T> @NotNull JsonWriter fromArray(@NotNull OutputStream stream, @NotNull T... array) {
        return fromList(Arrays.asList(array), stream);
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull JsonWriter fromList(@NotNull List<T> list, @NotNull OutputStream stream) {
        return new JsonWriter((JsonElement) JsonReader.fromList(list).openRootElement(), stream);
    }

    @Contract("_, _ -> new")
    public static @NotNull JsonWriter fromText(@NotNull CharSequence json, @NotNull OutputStream stream) {
        return new JsonWriter((JsonElement) JsonReader.fromText(json).openRootElement(), stream);
    }

    @Contract("_, _ -> new")
    public static @NotNull JsonWriter fromStream(@NotNull InputStream json, @NotNull OutputStream stream) {
        return new JsonWriter((JsonElement) JsonReader.fromStream(json).openRootElement(), stream);
    }

    @Override
    public void close() {
        if (stream != null) {
            var reader = new BufferedWriter(new OutputStreamWriter(stream));
            try {
                reader.write(((nx.peter.api.json.reader.JsonElement) root).getPrinter(Printer.Type.PrettyPrinter).print());
                reader.flush();
                reader.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            stream = null;
            root = INSTANCE;
        }
    }

    @Override
    public JsonElement openRootElement() {
        return root;
    }

    @Override
    public JsonObject openRootObject() {
        return isRootObject() ? (JsonObject) root : isRootArray() ? INSTANCE : (JsonObject) (root = (JsonElement) newObject(Collections.emptyMap()));
    }

    @Override
    public JsonArray openRootArray() {
        return isRootArray() ? (JsonArray) root : isRootObject() ? INSTANCE : (JsonArray) (root = (JsonElement) newArray(Collections.emptyList()));
    }

    @Override
    public boolean isRootElement() {
        return root.isNotNull();
    }

    @Override
    public boolean isRootObject() {
        return root instanceof JsonObject && root.isNotNull();
    }

    @Override
    public boolean isRootArray() {
        return root instanceof JsonArray && root.isNotNull();
    }
}
