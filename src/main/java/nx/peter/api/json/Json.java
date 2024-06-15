package nx.peter.api.json;

import nx.peter.api.json.data.Advanced;
import nx.peter.api.json.io.JsonReader;
import nx.peter.api.json.io.JsonWriter;
import nx.peter.api.json.reader.JsonObject;
import nx.peter.java.io.File;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static nx.peter.api.json.core.JsonNull.INSTANCE;
import static nx.peter.api.json.io.JsonReader.*;

/**
 * This is a utility class that allows loading of Json from various sources:
 * <ul>
 *     <li>Array of Objects</li>
 *     <li>{@link InputStream InputStream} of Json</li>
 *     <li>{@link List List} of Objects</li>
 *     <li>{@link Map Map} of Key-Value Pairs</li>
 *     <li>{@link String Path} to Json {@link java.io.File File}</li>
 *     <li>{@link String Text} of Json</li>
 * </ul>
 */
public final class Json {
    private static JsonReader reader;
    private static JsonWriter writer;

    private Json() {}

    /**
     * Read map of key-value pairs as json in the background
     * @param jsonMap json map
     * @param listener {@link OnReadJsonProgressListener} listener
     * @param <K> Map Key-Type
     * @param <V> Map Value-Type
     */
    public static <K, V> void readMapInBackground(@NotNull Map<K, V> jsonMap, OnReadJsonProgressListener listener) {
        reader = JsonReader.fromText("");
        var base = new Thread(() -> reader = fromMap(jsonMap));
        readInBackground(base, listener);
    }

    /**
     * Read array of items as json in the background
     * @param listener {@link OnReadJsonProgressListener} listener
     * @param jsonArray json array
     * @param <T> Array Item-Type
     */
    @SafeVarargs
    public static <T> void readArrayInBackground(OnReadJsonProgressListener listener, @NotNull T... jsonArray) {
        reader = JsonReader.fromText("");
        var base = new Thread(() -> reader = fromArray(jsonArray));
        readInBackground(base, listener);
    }

    /**
     * Read list of items as json in the background
     * @param jsonList json list
     * @param listener {@link OnReadJsonProgressListener} listener
     * @param <T> List Item-Type
     */
    public static <T> void readListInBackground(@NotNull List<T> jsonList, OnReadJsonProgressListener listener) {
        reader = JsonReader.fromText("");
        var base = new Thread(() -> reader = fromList(jsonList));
        readInBackground(base, listener);
    }

    /**
     * Read input stream as json in the background
     * @param jsonStream json input stream
     * @param listener {@link OnReadJsonProgressListener} listener
     */
    public static void readStreamInBackground(@NotNull InputStream jsonStream, OnReadJsonProgressListener listener) {
        reader = JsonReader.fromText("");
        var base = new Thread(() -> reader = fromStream(jsonStream));
        readInBackground(base, listener);
    }

    /**
     * Read file path as json in the background
     * @param jsonPath json file path
     * @param listener {@link OnReadJsonProgressListener} listener
     */
    public static void readPathInBackground(@NotNull CharSequence jsonPath, OnReadJsonProgressListener listener) {
        reader = JsonReader.fromText("");
        var base = new Thread(() -> reader = new File(jsonPath).exists() ? fromFile(jsonPath) : fromRoot(INSTANCE));
        readInBackground(base, listener);
    }

    /**
     * Read json text as json in the background
     * @param jsonText json text
     * @param listener {@link OnReadJsonProgressListener} listener
     */
    public static void readTextInBackground(@NotNull CharSequence jsonText, OnReadJsonProgressListener listener) {
        reader = JsonReader.fromText("");
        var base = new Thread(() -> reader = isJson(jsonText.toString()) ? fromText(jsonText) : fromRoot(INSTANCE));
        readInBackground(base, listener);
    }

    /**
     * Read json text as json in the background
     * @param model object model instance
     * @param listener {@link OnWriteJsonProgressListener} listener
     * @param <T> object type
     */
    public static <T> void readModelInBackground(@NotNull T model, OnReadJsonProgressListener listener) {
        reader = JsonReader.fromText("");
        var base = new Thread(() -> reader = fromModel(model));
        readInBackground(base, listener);
    }

    private static void readInBackground(Thread base, OnReadJsonProgressListener listener) {
        var startTime = System.currentTimeMillis();
        var processor = new Thread(() -> {
            if (listener != null) listener.onStart(startTime);
            base.start();
            while (!base.isInterrupted() && base.isAlive()) {
                var duration = System.currentTimeMillis() - startTime;
                if ((duration % 1000) <= 1 && listener != null) listener.onProgress(0, duration);
                if (base.getState() == Thread.State.TERMINATED || base.isInterrupted() && !base.isAlive()) {
                    if (listener != null) listener.onCompleted(reader, duration);
                    reader.close();
                    reader = null;
                    break;
                }
            }
        });

        processor.start();
    }


    private static boolean isJson(@NotNull String text) {
        return (text.startsWith("{") && text.endsWith("}")) || (text.startsWith("[") && text.endsWith("]"));
    }




    /**
     * Write a new {@link nx.peter.api.json.writer.JsonArray JsonArray} in the background
     * @param stream json {@link OutputStream output}
     * @param listener {@link OnWriteJsonProgressListener} listener
     */
    public static void writeNewArrayInBackground(@NotNull OutputStream stream, OnWriteJsonProgressListener listener) {
        writer = JsonWriter.fromText("", stream);
        var base = new Thread(() -> writer = JsonWriter.createArray(stream));
        writeInBackground(base, listener);
    }

    /**
     * Write a new {@link nx.peter.api.json.writer.JsonObject JsonObject} in the background
     * @param stream json {@link OutputStream output}
     * @param listener {@link OnWriteJsonProgressListener} listener
     */
    public static void writeNewObjectInBackground(@NotNull OutputStream stream, OnWriteJsonProgressListener listener) {
        writer = JsonWriter.fromText("", stream);
        var base = new Thread(() -> writer = JsonWriter.createObject(stream));
        writeInBackground(base, listener);
    }

    /**
     * Write map of key-value pairs as json in the background
     * @param jsonMap json map
     * @param stream json {@link OutputStream output}
     * @param listener {@link OnWriteJsonProgressListener} listener
     * @param <K> Map Key-Type
     * @param <V> Map Value-Type
     */
    public static <K, V> void writeMapInBackground(@NotNull Map<K, V> jsonMap, @NotNull OutputStream stream, OnWriteJsonProgressListener listener) {
        writer = JsonWriter.fromText("", stream);
        var base = new Thread(() -> writer = JsonWriter.fromMap(jsonMap, stream));
        writeInBackground(base, listener);
    }

    /**
     * Write array of items as json in the background
     * @param stream json {@link OutputStream output}
     * @param listener {@link OnWriteJsonProgressListener} listener
     * @param jsonArray json array
     * @param <T> Array Item-Type
     */
    @SafeVarargs
    public static <T> void writeArrayInBackground(@NotNull OutputStream stream, OnWriteJsonProgressListener listener, @NotNull T... jsonArray) {
        writer = JsonWriter.fromText("", stream);
        var base = new Thread(() -> writer = JsonWriter.fromArray(stream, jsonArray));
        writeInBackground(base, listener);
    }

    /**
     * Write list of items as json in the background
     * @param jsonList json list
     * @param stream json {@link OutputStream output}
     * @param listener {@link OnWriteJsonProgressListener} listener
     * @param <T> List Item-Type
     */
    public static <T> void writeListInBackground(@NotNull List<T> jsonList, @NotNull OutputStream stream, OnWriteJsonProgressListener listener) {
        writer = JsonWriter.fromText("", stream);
        var base = new Thread(() -> writer = JsonWriter.fromList(jsonList, stream));
        writeInBackground(base, listener);
    }

    /**
     * Write input stream as json in the background
     * @param jsonStream json input stream
     * @param stream json {@link OutputStream output}
     * @param listener {@link OnWriteJsonProgressListener} listener
     */
    public static void writeStreamInBackground(@NotNull InputStream jsonStream, @NotNull OutputStream stream, OnWriteJsonProgressListener listener) {
        writer = JsonWriter.fromText("", stream);
        var base = new Thread(() -> writer = JsonWriter.fromStream(jsonStream, stream));
        writeInBackground(base, listener);
    }

    /**
     * Write file path as json in the background
     * @param jsonPath json file path
     * @param stream json {@link OutputStream output}
     * @param listener {@link OnWriteJsonProgressListener} listener
     */
    public static void writePathInBackground(@NotNull CharSequence jsonPath, @NotNull OutputStream stream, OnWriteJsonProgressListener listener) {
        writer = JsonWriter.fromText("", stream);
        var base = new Thread(() -> writer = new File(jsonPath).exists() ? JsonWriter.fromFile(jsonPath, stream) : JsonWriter.fromRoot(INSTANCE, stream));
        writeInBackground(base, listener);
    }

    /**
     * Write json text as json in the background
     * @param jsonText json text
     * @param stream json {@link OutputStream output}
     * @param listener {@link OnWriteJsonProgressListener} listener
     */
    public static void writeTextInBackground(@NotNull CharSequence jsonText, @NotNull OutputStream stream, OnWriteJsonProgressListener listener) {
        writer = JsonWriter.fromText("", stream);
        var base = new Thread(() -> writer = isJson(jsonText.toString()) ? JsonWriter.fromText(jsonText, stream) : JsonWriter.fromRoot(INSTANCE, stream));
        writeInBackground(base, listener);
    }

    /**
     * Write json text as json in the background
     * @param model object model instance
     * @param stream json {@link OutputStream output}
     * @param listener {@link OnWriteJsonProgressListener} listener
     * @param <T> object type
     */
    public static <T> void writeModelInBackground(@NotNull T model, @NotNull OutputStream stream, OnWriteJsonProgressListener listener) {
        writer = JsonWriter.fromText("", stream);
        var base = new Thread(() -> writer = JsonWriter.fromModel(model, stream));
        writeInBackground(base, listener);
    }

    private static void writeInBackground(@NotNull Thread base, OnWriteJsonProgressListener listener) {
        var startTime = System.currentTimeMillis();
        var processor = new Thread(() -> {
            if (listener != null) listener.onStart(startTime);
            base.start();
            while (!base.isInterrupted() && base.isAlive()) {
                var duration = System.currentTimeMillis() - startTime;
                if ((duration % 1000) <= 1 && listener != null) listener.onProgress(0, duration);
                if (base.getState() == Thread.State.TERMINATED || base.isInterrupted() && !base.isAlive()) {
                    if (listener != null) listener.onCompleted(writer, duration);
                    break;
                }
            }
        });

        processor.start();
    }




    /**
     * Convert an {@link Object} instance to a readable {@link JsonObject}
     * @param instance object instance
     * @return readable JsonObject
     * @param <T> the instance type
     */
    public static <T> JsonObject toJson(T instance) {
        return Advanced.toJson(instance);
    }

    /**
     * Convert {@link nx.peter.api.json.core.JsonObject JsonObject} to an {@link Object} with the given {@link Class<T>}
     * @param json json object
     * @param clazz class of object to be created
     * @return the object instance for the given class
     * @param <T> object type
     */
    public static <T> @Nullable T fromJson(nx.peter.api.json.core.JsonObject json, Class<T> clazz) {
        return Advanced.fromJson(json, clazz);
    }

}