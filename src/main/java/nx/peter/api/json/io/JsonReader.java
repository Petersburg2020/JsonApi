package nx.peter.api.json.io;

import nx.peter.api.json.Json;
import nx.peter.api.json.reader.JsonArray;
import nx.peter.api.json.reader.JsonElement;
import nx.peter.api.json.reader.JsonObject;
import nx.peter.java.io.FileManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static nx.peter.api.json.JsonUtil.*;
import static nx.peter.api.json.core.JsonNull.INSTANCE;

public class JsonReader implements JsonStream<JsonElement, JsonArray, JsonObject> {
    JsonElement root;

    JsonReader(JsonElement root) {
        this.root = root;
    }

    JsonReader(Map<String, Object> map) {
        root = newObject(map);
    }

    JsonReader(List<Object> list) {
        root = newArray(list);
    }

    JsonReader(@NotNull String text) {
        if (text.startsWith("{") && text.endsWith("}"))
            root = newObject(extractObject(text));
        else if (text.startsWith("[") && text.endsWith("]"))
            root = newArray(extractArray(text));
        else root = INSTANCE;
    }

    public static @NotNull <T> JsonReader fromModel(@NotNull T model) {
        return fromRoot(Json.toJson(model));
    }

    public static @NotNull JsonReader fromRoot(@NotNull nx.peter.api.json.core.JsonElement root) {
        return new JsonReader((JsonElement) root);
    }

    @Contract("_ -> new")
    public static <K, T> @NotNull JsonReader fromMap(@NotNull Map<K, T> list) {
        return new JsonReader(resolve(list));
    }

    @SafeVarargs
    @Contract("_ -> new")
    public static <T> @NotNull JsonReader fromArray(@NotNull T... array) {
        return fromList(Arrays.asList(array));
    }

    @Contract("_ -> new")
    public static <T> @NotNull JsonReader fromList(@NotNull List<T> list) {
        return new JsonReader(resolve(list));
    }

    @Contract("_ -> new")
    public static @NotNull JsonReader fromFile(@NotNull CharSequence path) {
        try {
            return fromStream(new FileInputStream(path.toString()));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return fromText("");
        }
    }

    @Contract("_ -> new")
    public static @NotNull JsonReader fromText(@NotNull CharSequence text) {
        return new JsonReader(text.toString());
    }

    @Contract("_ -> new")
    public static @NotNull JsonReader fromStream(@NotNull InputStream stream) {
        return fromText(FileManager.readString(stream).trim());
    }

    public String prettyPrint() {
        return root.getPrinter(Printer.Type.PrettyPrinter).print();
    }

    public String textPrint() {
        return root.getPrinter(Printer.Type.TextPrinter).print();
    }

    @Override
    public JsonElement openRootElement() {
        return root;
    }

    @Override
    public JsonObject openRootObject() {
        return isRootObject() ? (JsonObject) root : INSTANCE;
    }

    @Override
    public JsonArray openRootArray() {
        return isRootArray() ? (JsonArray) root : INSTANCE;
    }

    @Override
    public boolean isRootElement() {
        return root.isElement() && root.isRootElement() && root.isNotNull();
    }

    @Override
    public boolean isRootObject() {
        return root.isObject() && root.isRootElement() && root.isNotNull();
    }

    @Override
    public boolean isRootArray() {
        return root.isArray() && root.isRootElement() && root.isNotNull();
    }

    @Override
    public void close() {

    }
}
