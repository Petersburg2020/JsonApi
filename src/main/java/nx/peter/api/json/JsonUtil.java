package nx.peter.api.json;

import nx.peter.api.json.core.JsonNull;
import nx.peter.api.json.data.Duo;
import nx.peter.api.json.reader.*;
import nx.peter.java.util.data.DataManager;
import nx.peter.java.util.data.Word;
import nx.peter.java.util.param.IntString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static nx.peter.java.util.data.DataManager.*;

public interface JsonUtil {
    String ESCAPED_SEQUENCE = "Escaped sequence";
    String INVALID_ESCAPED_SEQUENCE = "Invalid escape sequence";

    
    static <T> List<T> createList() {
        return new ArrayList<>();
    }

    @Contract(" -> new")
    static @NotNull JsonNull newNull() {
        return new IJsonNull();
    }

    static JsonNative newNative(Object value) {
        return value instanceof CharSequence s ? newString(s) : value instanceof Boolean b ? newBoolean(b) : value instanceof Number n ? newNumber(n) : JsonNull.INSTANCE;
    }

    static JsonNumber newNumber(Number value) {
        return value instanceof Integer i ? newInteger(i) : value instanceof Long l ? newLong(l) : value instanceof Float f ? newFloat(f) : value instanceof Double d ? newDouble(d) : JsonNull.INSTANCE;
    }

    static JsonInteger newInteger(Integer value) {
        return value == null ? JsonNull.INSTANCE : new IJsonElement.IJsonInteger(value);
    }

    static JsonBoolean newBoolean(Boolean value) {
        return value == null ? JsonNull.INSTANCE : new IJsonElement.IJsonBoolean(value);
    }

    static JsonString newString(CharSequence value) {
        return value == null ? JsonNull.INSTANCE : new IJsonElement.IJsonString(value);
    }

    static JsonDouble newDouble(Double value) {
        return value == null ? JsonNull.INSTANCE : new IJsonElement.IJsonDouble(value);
    }

    static JsonFloat newFloat(Float value) {
        return value == null ? JsonNull.INSTANCE : new IJsonElement.IJsonFloat(value);
    }

    static JsonLong newLong(Long value) {
        return value == null ? JsonNull.INSTANCE : new IJsonElement.IJsonLong(value);
    }

    static JsonObject newObject(Map value) {
        return value == null ? JsonNull.INSTANCE : new IJsonObject(resolve(value));
    }

    static JsonArray newArray(List value) {
        return value == null ? JsonNull.INSTANCE : new IJsonArray(resolve(value));
    }

    static JsonValue newValue(Object value) {
        return isNative(value) ? newNative(value) : value instanceof List l ? newArray(l) : value instanceof Map m ? newObject(m) : JsonNull.INSTANCE;
    }

    static @NotNull Map<String, Object> format(@NotNull Map<String, Object> object) {
        var result = new LinkedHashMap<String, Object>();
        for (var name : object.keySet()) {
            var item = object.get(name);
            if (isNative(item) || item == null) result.put(name, item);
            else if (item instanceof List list) result.put(name, format(resolve(list)));
            else if (item instanceof Map map) result.put(name, format(resolve(map)));
        }
        return result;
    }

    static @NotNull Map<String, Object> resolve(@NotNull Map map) {
        var temp = new LinkedHashMap<String, Object>();
        for (var name : map.keySet()) {
            var item = map.get(name);
            if (name instanceof CharSequence && isJsonItem(item)) temp.put(name.toString(), item);
        }
        return temp;
    }

    static @NotNull List<Object> format(@NotNull List<Object> array) {
        var result = new ArrayList<>();
        for (var item : array) {
            if (isNative(item) || item == null) result.add(item);
            else if (item instanceof List list) result.add(format(resolve(list)));
            else if (item instanceof Map map) result.add(format(resolve(map)));
        }
        return result;
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull List<Object> resolve(@NotNull List list) {
        return new ArrayList<>(list);
    }

    static boolean isJsonItem(Object obj) {
        return isNative(obj) || isList(obj) || isMap(obj) || obj == null;
    }

    static boolean isNative(Object obj) {
        return isBoolean(obj) || isNumber(obj) || isString(obj);
    }

    static boolean isNumber(Object obj) {
        return obj instanceof Number;
    }

    static boolean isLong(Object obj) {
        return obj instanceof Long;
    }

    static boolean isDouble(Object obj) {
        return obj instanceof Double;
    }

    static boolean isFloat(Object obj) {
        return obj instanceof Float;
    }

    static boolean isInteger(Object obj) {
        return obj instanceof Integer;
    }

    static boolean isString(Object obj) {
        return obj instanceof CharSequence;
    }

    static boolean isBoolean(Object obj) {
        return obj instanceof Boolean;
    }

    static boolean isList(Object obj) {
        return obj instanceof List;
    }

    static boolean isMap(Object obj) {
        return obj instanceof Map;
    }

    static boolean isElement(Object obj) {
        return isList(obj) || isMap(obj);
    }


    @Contract("_, _, _ -> new")
    static @NotNull Duo<Character, IntString> readEscapeCharacter(@NotNull CharSequence json, int pos, int lineNumber) throws IOException {
        var buffer = json.toString().toCharArray();
        var lineStart = 0;
        var escaped = buffer[pos];
        switch (escaped) {
            case 'u':
                //if (pos + 4 > limit && !fillBuffer(4)) throw syntaxError("Unterminated escape sequence");
                // Equivalent to Integer.parseInt(stringPool.get(buffer, pos, 4), 16);
                var result = getResult(pos, buffer);
                pos += 4;
                return new IJsonElement.IDuo<>(result, new IntString(ESCAPED_SEQUENCE, lineNumber));

            case 't':
                return new IJsonElement.IDuo<>('\t', new IntString(ESCAPED_SEQUENCE, lineNumber));

            case 'b':
                return new IJsonElement.IDuo<>('\b', new IntString(ESCAPED_SEQUENCE, lineNumber));

            case 'n':
                return new IJsonElement.IDuo<>('\n', new IntString(ESCAPED_SEQUENCE, lineNumber));

            case 'r':
                return new IJsonElement.IDuo<>('\r', new IntString(ESCAPED_SEQUENCE, lineNumber));

            case 'f':
                return new IJsonElement.IDuo<>('\f', new IntString(ESCAPED_SEQUENCE, lineNumber));
            case '\n':
                lineNumber++;
                lineStart = pos;
                // fall-through
            case '\'':
            case '"':
            case '\\':
            case '/':
                return new IJsonElement.IDuo<>(escaped, new IntString(ESCAPED_SEQUENCE, lineNumber));
            default:
                // throw error when none of the above cases are matched
                return new IJsonElement.IDuo<>(null, new IntString(INVALID_ESCAPED_SEQUENCE, lineNumber));
        }
    }

    static char getResult(int pos, char... buffer) {
        char result = 0;
        for (int i = pos, end = i + 4; i < end; i++) {
            char c = buffer[i];
            result <<= 4;
            if (c >= '0' && c <= '9') result += (char) (c - '0');
            else if (c >= 'a' && c <= 'f') result += (char) (c - 'a' + 10);
            else if (c >= 'A' && c <= 'F') result += (char) (c - 'A' + 10);
            else throw new NumberFormatException("\\u" + new String(buffer, pos, 4));
        }
        return result;
    }

    static boolean isLiteral(char c) {
        return switch (c) {
            // checkLenient(); // fall-through
            case '/', '\\', ';', '#', '=', '{', '}', '[', ']', ':', ',', ' ', '\t', '\f', '\r', '\n' -> false;
            default -> true;
        };
    }

    @Contract(value = " -> new", pure = true)
    static <T> @NotNull List<T> newList() {
        return new ArrayList<>();
    }

    static @NotNull List<Object> extractArray(@NotNull CharSequence letters) {
        List<Object> array = newList();
        var data = new Word(letters.toString().substring(letters.toString().indexOf("[") + 1, letters.toString().lastIndexOf("]")).trim());
        int start = 0, end = 0;

        while (start > -1 && start < data.length() - 1) {
            start = data.subLetters(end).getFirstLetterIndex();
            char firstLetter = data.subLetters(end).getFirstLetter();
            if (firstLetter == ',' || firstLetter == '}') {
                end += data.subLetters(end).indexOf(firstLetter) + 1;
                firstLetter = data.subLetters(end).getFirstLetter();
            }

            if (start <= -1)
                break;
            start += end;

            end = getEnd(data, firstLetter, start);

            if (end < 0 || start >= data.length())
                break;

            String value = data.substring(start, end).trim();
            if (end >= data.length() - 1)
                value = data.substring(start).trim();

            if (value.startsWith("[") && !value.endsWith("]")) {
                end = value.lastIndexOf("]");
                value = value.substring(0, end + 1);
            } else if (value.startsWith("{") && !value.endsWith("}")) {
                end = value.lastIndexOf("}");
                value = value.substring(0, end + 1);
            }

            var type = getType(value);

            var val = switch (type) {
                case OBJECT -> extractObject(value);
                case ARRAY -> extractArray(value);
                default -> toObject(value);
            };

            array.add(val);

            if (type.contentEquals(OBJECT) || type.contentEquals(ARRAY) || type.contentEquals(STRING))
                end++;

            if (end >= data.length() - 1 || start >= data.length() - 1)
                break;
        }
        return array;
    }

    static @NotNull Map<String, Object> extractObject(@NotNull CharSequence letters) {
        var object = new LinkedHashMap<String, Object>();
        var data = new Word(letters.toString().substring(letters.toString().indexOf("{") + 1, letters.toString().lastIndexOf("}")).replaceAll("\t", "").replaceAll("\n", "").trim());
        int start = 0, end = 0, lastIndex;

        while (start < data.length() - 1 && end < data.length() - 1) {
            start = data.indexOf("\"", end) + 1;
            end = data.indexOf("\":", start);
            if (end < 0 || end > data.length() - 1 || start > data.length() - 1)
                break;

            String key = data.substring(start, end);
            start = data.indexOf(":", end);
            if (start > -1) start++;
            else break;

            lastIndex = start;
            start = data.subLetters(lastIndex).getFirstLetterIndex();
            char firstLetter = data.subLetters(lastIndex + 1).getFirstLetter();
            if (firstLetter == ',' || firstLetter == '}') {
                end += data.subLetters(end).indexOf(firstLetter) + 1;
                firstLetter = data.subLetters(end).getFirstLetter();
            }

            if (start <= -1) break;
            else start += lastIndex;

            end = getEnd(data, firstLetter, start);

            if (end < 0 || start > data.length() - 1)
                break;

            var value = data.substring(start, end).trim();
            if (end >= data.length() - 1 && start < data.length() - 1)
                value = data.substring(start).trim();

            if (value.startsWith("[") && !value.endsWith("]"))
                value = value.substring(0, value.lastIndexOf("]") + 1);
            else if (value.startsWith("{") && !value.endsWith("}"))
                value = value.substring(0, value.lastIndexOf("}") + 1);

            var type = getType(value);

            var val = switch (type) {
                case OBJECT -> extractObject(value);
                case ARRAY -> extractArray(value);
                default -> toObject(value);
            };
            object.put(key, val);

            if (type.contentEquals(OBJECT) || type.contentEquals(ARRAY) || type.contentEquals(STRING))
                end++;

            if (end > data.length() - 1 || start > data.length() - 1 || end <= -1 || start <= -1)
                break;
        }
        return object;
    }

    private static int getEnd(@NotNull Word data, char firstLetter, int start) {
        int end;
        if (start <= -1 || start > data.length() - 1) return -1;
        if (firstLetter == '\"') {
            end = data.subLetters(start).contains("\",") ? data.indexOf("\",", start) : data.indexOf("\" ", start);
            if (end > -1)
                end++;
            else
                end = data.length();
        } else if (firstLetter == '[') {
            end = getCoverIndex(data, '[', ']', start);
            if (end > -1)
                end++;
        } else if (firstLetter == '{') {
            end = getCoverIndex(data, '{', '}', start);
            if (end > -1)
                end++;
        } else
            end = data.subLetters(start).contains(",") ? data.indexOf(",", start) : data.length() - 1;
        return end;
    }

    static String getType(String value) {
        return value == null || value.equals("null") || value.startsWith("\"") && value.endsWith("\"") ? STRING : value.startsWith("[") && value.endsWith("]") ? ARRAY :
                value.startsWith("{") && value.endsWith("}") ? OBJECT :
                        isBool(value) ? BOOLEAN : DataManager.isInteger(value) ? INTEGER : DataManager.isLong(value) ? LONG :
                                DataManager.isFloat(value) ? FLOAT : isNumberOnly(value) ? DOUBLE : NULL;
    }

    String ARRAY = "array";
    String OBJECT = "object";
    String STRING = "string";
    String INTEGER = "int";
    String FLOAT = "float";
    String DOUBLE = "double";
    String LONG = "long";
    String BOOLEAN = "boolean";
    String NULL = "null";

    static int getCoverIndex(Word source, char open, char close, int index) {
        if (source == null || index > source.length() || index < 0 && !source.subLetters(index).contains(open))
            return -1;
        int start, end = index, openCount = 0, closeCount = 0, lastIndex = -1;
        start = source.indexOf(open, end);
        end = start;
        lastIndex = end + 1;
        while (start > -1 && start < source.length() - 1) {
            start = source.indexOf(close, lastIndex);
            if (start <= -1 || start < end) return openCount == closeCount ? lastIndex : -1;
            start++;
            if (start >= source.length() - 1) {
                openCount = source.subLetters(end).getLetterCount(open);
                closeCount = source.subLetters(end).getLetterCount(close);
            } else {
                openCount = source.subLetters(end, start).getLetterCount(open);
                closeCount = source.subLetters(end, start).getLetterCount(close);
            }
            lastIndex = start;
            if (openCount == closeCount) return lastIndex;
        }
        return -1;
    }

    static boolean isValid(Word source, char open, char close, int start) {
        return getCoverIndex(source, open, close, start) > -1;
    }

    static Object toObject(String value) {
        return switch (getType(value)) {
            case BOOLEAN -> value.contentEquals("true");
            case INTEGER -> extractIntegers(value).get(0);
            case LONG -> !extractLongs(value).isEmpty() ? extractLongs(value).get(0) : Long.MIN_VALUE;
            case FLOAT -> !extractFloats(value).isEmpty() ? extractFloats(value).get(0) : Float.MIN_VALUE;
            case DOUBLE -> !extractDecimals(value).isEmpty() ? extractDecimals(value).get(0) : Double.MIN_VALUE;
            case STRING -> value.equals(NULL) ? null :
                    value.length() > 1 ? new Word(value).remove("\"", value.length() - 2).remove("\"").replaceAll("@10", "\n").replaceAll("@09", "\t").get() : value;
            default -> null;
        };
    }

    static @NotNull String toJson(List<Object> json, int indent) {
        var pretty = toJson(json, indent, false);
        return pretty.substring(0, pretty.trim().length() - 1) + "]";
    }

    static @NotNull String toPrettyJson(List<Object> json, int indent) {
        var pretty = toJson(json, indent, true);
        return pretty.substring(0, pretty.trim().length() - 1) + "\n" + tab(indent) + "]";
    }

    static @NotNull String toJson(Map<String, Object> json, int indent) {
        var notPretty = toJson(json, indent, false);
        return "{" + notPretty.substring(1, notPretty.trim().length() - 1) + '}';
    }

    static @NotNull String toPrettyJson(Map<String, Object> json, int indent) {
        var pretty = toJson(json, indent, true);
        return "{" + pretty.substring(1, pretty.trim().length() - 1) + "\n" + tab(indent) + '}';
    }

    static String toJson(@NotNull List<Object> json, int indent, boolean format) {
        return json.stream().map(e -> {
            Object value = e;
            if (value == null || isString(value))
                value = value == null ? "null" : "\"" + value + "\"";
            else if (value instanceof Map<?,?> object)
                value = format ? toPrettyJson(resolve(object), indent + 1) : toJson(resolve(object), indent + 1);
            else if (value instanceof List<?> array)
                value = format ? toPrettyJson(resolve(array), indent + 1) : toJson(resolve(array), indent + 1);
            return (format ? "\n" + tab(indent + 1) : "") + value;
        }).toList().toString();
    }

    static String toJson(@NotNull Map<String, Object> json, int indent, boolean format) {
        return json.entrySet().stream().map(e -> {
            Object value = e.getValue();
            if (value == null || isString(value))
                value = value == null ? null : "\"" + value + "\"";
            else if (value instanceof Map<?,?> object)
                value = format ? toPrettyJson(resolve(object), indent + 1) : toJson(resolve(object), indent + 1);
            else if (value instanceof List<?> array)
                value = format ? toPrettyJson(resolve(array), indent + 1) : toJson(resolve(array), indent + 1);
            return (format ? "\n" + tab(indent + 1) : "") + "\"" + e.getKey() + "\": " + value;
        }).toList().toString();
    }
}
