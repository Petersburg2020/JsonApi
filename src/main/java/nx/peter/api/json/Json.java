package nx.peter.api.json;

import nx.peter.api.json.io.JsonReader;
import nx.peter.java.io.File;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
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
public abstract class Json {
    protected static JsonReader reader;


    public static <K, V> void loadMapInBackground(@NotNull Map<K, V> jsonMap, OnProgressListener listener) {
        reader = JsonReader.fromText("");
        var base = new Thread(() -> reader = fromMap(jsonMap));
        loadInBackground(base, listener);
    }

    @SafeVarargs
    public static <T> void loadArrayInBackground(OnProgressListener listener, @NotNull T... jsonArray) {
        reader = JsonReader.fromText("");
        var base = new Thread(() -> reader = fromArray(jsonArray));
        loadInBackground(base, listener);
    }

    public static <T> void loadListInBackground(@NotNull List<T> jsonList, OnProgressListener listener) {
        reader = JsonReader.fromText("");
        var base = new Thread(() -> reader = fromList(jsonList));
        loadInBackground(base, listener);
    }

    public static void loadStreamInBackground(@NotNull InputStream jsonStream, OnProgressListener listener) {
        reader = JsonReader.fromText("");
        var base = new Thread(() -> reader = fromStream(jsonStream));
        loadInBackground(base, listener);
    }

    public static void loadPathInBackground(@NotNull CharSequence jsonPath, OnProgressListener listener) {
        reader = JsonReader.fromText("");
        var base = new Thread(() -> reader = new File(jsonPath).exists() ? fromFile(jsonPath) : fromRoot(INSTANCE));
        loadInBackground(base, listener);
    }

    public static void loadTextInBackground(@NotNull CharSequence jsonText, OnProgressListener listener) {
        reader = JsonReader.fromText("");
        var base = new Thread(() -> reader = isJson(jsonText.toString()) ? fromText(jsonText) : fromRoot(INSTANCE));
        loadInBackground(base, listener);
    }

    protected static boolean isJson(@NotNull String text) {
        return (text.startsWith("{") && text.endsWith("}")) || (text.startsWith("[") && text.endsWith("]"));
    }

    protected static void loadInBackground(Thread base, OnProgressListener listener) {
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

}