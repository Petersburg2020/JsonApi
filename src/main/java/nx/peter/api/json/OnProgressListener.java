package nx.peter.api.json;

import nx.peter.api.json.io.JsonReader;

public interface OnProgressListener {
    void onStart(long startTimeInMillis);

    void onCompleted(JsonReader json, long durationInMillis);

    void onProgress(int percent, long durationInMillis);
}
