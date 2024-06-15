package nx.peter.api.json.core;

import nx.peter.api.json.JsonUtil;
import nx.peter.api.json.reader.JsonArray;
import nx.peter.api.json.reader.JsonBoolean;
import nx.peter.api.json.reader.JsonDouble;
import nx.peter.api.json.reader.JsonFloat;
import nx.peter.api.json.reader.JsonInteger;
import nx.peter.api.json.reader.JsonLong;
import nx.peter.api.json.reader.JsonObject;
import nx.peter.api.json.reader.JsonString;

public interface JsonNull extends JsonArray, JsonObject, JsonBoolean, JsonString, JsonLong, JsonFloat, JsonDouble, JsonInteger,
        nx.peter.api.json.writer.JsonArray, nx.peter.api.json.writer.JsonObject, nx.peter.api.json.writer.JsonBoolean,
        nx.peter.api.json.writer.JsonString, nx.peter.api.json.writer.JsonLong, nx.peter.api.json.writer.JsonFloat,
        nx.peter.api.json.writer.JsonDouble, nx.peter.api.json.writer.JsonInteger {
    JsonNull INSTANCE = JsonUtil.newNull();
}
