package nx.peter.api.json;

import nx.peter.api.json.io.JsonWriter;

public interface OnWriteJsonProgressListener {
    void onStart(long startTimeInMillis);

    void onCompleted(JsonWriter json, long durationInMillis);

    void onProgress(int percent, long durationInMillis);
}
