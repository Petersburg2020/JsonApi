package nx.peter.api.json.data;

import nx.peter.api.json.JsonUtil;
import nx.peter.api.json.core.JsonNull;
import nx.peter.api.json.reader.JsonArray;
import nx.peter.api.json.reader.JsonObject;
import nx.peter.java.util.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import org.jetbrains.annotations.NotNull;

public class Advanced {

    public static <T> ObjectDetail<T> getObjectDetail(T model) {
        if (model == null) return new ObjectDetail<>();
        Class<T> clazz = (Class<T>) model.getClass();
        var fields = clazz.getFields();
        List<String> names = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        List<ObjectDetail.Type> types = new ArrayList<>();
        for (Field field : fields) {
            names.add(field.getName());
            Class type = field.getType();

            try {
                if (field.isEnumConstant()) {
                    values.add(field.get(model));
                    types.add(ObjectDetail.Type.Enum);
                } else if (type.equals(int.class)) {
                    values.add(field.getInt(model));
                    types.add(ObjectDetail.Type.Integer);
                } else if (type.equals(boolean.class)) {
                    values.add(field.getBoolean(model));
                    types.add(ObjectDetail.Type.Boolean);
                } else if (type.equals(double.class)) {
                    values.add(field.getDouble(model));
                    types.add(ObjectDetail.Type.Double);
                } else if (type.equals(float.class)) {
                    values.add(field.getFloat(model));
                    types.add(ObjectDetail.Type.Float);
                } else if (type.equals(long.class)) {
                    values.add(field.getLong(model));
                    types.add(ObjectDetail.Type.Long);
                } else if (type.equals(String.class)) {
                    values.add(field.get(model).toString());
                    types.add(ObjectDetail.Type.String);
                } else if (type.equals(Map.class)) {
                    values.add(field.get(model));
                    types.add(ObjectDetail.Type.Map);
                } else if (type.equals(List.class)) {
                    values.add(field.get(model));
                    types.add(ObjectDetail.Type.List);
                } else {
                    values.add(field.get(model));
                    types.add(ObjectDetail.Type.Object);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                System.out.println(e.getMessage());
                System.out.println("An error occurred!");
            }
        }
        return new ObjectDetail<>(names, values, types, clazz);
    }

    public static <T> T getObject(@NotNull Map<String, Object> detail, Class<T> clazz) {
        if (detail == null | clazz == null) return null;
        Class[] parameters = null;
        List<String> names = null;
        for (Constructor<?> c : clazz.getConstructors())
            if (equals(clazz, c)) {
                parameters = new Class[c.getParameterCount()];
                names = getSortedParameterNames(c, clazz);
                int index;
                for (Parameter p : c.getParameters()) {
                    index = Integer.parseInt(p.getName().substring(3));
                    parameters[index] = p.getType();
                }
                break;
            }
        boolean compat = parameters != null;
        if (compat)
            try {
                return clazz.getConstructor(parameters)
                        .newInstance(sortByName(detail, names).toArray());
            } catch (InstantiationException
                    | NoSuchMethodException
                    | InvocationTargetException
                    | ClassCastException
                    | IllegalArgumentException
                    | IllegalAccessException e) {
                System.out.println(e.getMessage());
                return null;
            }
        return null;
    }

    public static <T> JsonObject toJson(Map<String, T> map) {
        if (map != null) {
            Map<String, Object> object = new LinkedHashMap<>();
            for (String name : map.keySet()) {
                var value = map.get(name);
                if (value instanceof Integer
                        || value instanceof Long
                        || value instanceof Double
                        || value instanceof Float
                        || value instanceof Boolean
                        || value instanceof String
                        || value instanceof Byte) object.put(name, value);
                else if (value instanceof Object[]) object.put(name, toJson((Object[]) value));
                else if (value instanceof List)
                    object.put(name, toJson(Util.toObjectList((List<?>) value)));
                else if (value instanceof Map)
                    object.put(name, toJson(Util.toObjectMap((Map<?, ?>) value)));
                else object.put(name, toJson(value));
            }
            return JsonUtil.newObject(object);
        }
        return null;
    }

    @SafeVarargs
    public static <T> JsonArray toJson(T... value) {
        return toJson(Arrays.asList(value));
    }

    public static <T> JsonArray toJson(List<T> list) {
        var array = new ArrayList<Object>();
        if (list != null)
            // System.out.println("We are array!");
            for (var value : list)
                if (value instanceof Integer
                        || value instanceof Long
                        || value instanceof Double
                        || value instanceof Float
                        || value instanceof Boolean
                        || value instanceof String
                        || value instanceof Byte) array.add(value);
                else if (value instanceof Object[]) array.add(toJson((Object[]) value));
                else if (value instanceof List)
                    array.add(toJson(Util.toObjectList((List<?>) value)));
                else if (value instanceof Map)
                    array.add(toJson(Util.toObjectMap((Map<?, ?>) value)));
                else array.add(toJson(value));
        return JsonUtil.newArray(array);
    }

    public static <T> JsonObject toJson(T model) {
        Map<String, Object> object = new LinkedHashMap<>();
        if (model != null) {
            var detail = getObjectDetail(model);

            System.out.println(detail);
            for (var name : detail.names) {
                var value = detail.get(name);
                if (isRawData(value)) object.put(name, value);
                else if (isArray(value)) object.put(name, toJson(toObjectList(value)));
                else if (value instanceof List) object.put(name, toJson(toObjectList(value)));
                else if (value instanceof Map)
                    object.put(name, toJson(Util.toObjectMap((Map<?, ?>) value)));
                else object.put(name, toJson(value));
            }
            object.put("className", model.getClass().getName());
        }
        return JsonUtil.newObject(object);
    }

    protected static <O> List<Object> toObjectList(Object array) {
        return isList(array) ? toObjectList((List<O>) array) : toObjectList((O[]) array);
    }

    protected static <T> String getClassName(Class<T> clazz) {
        return clazz.getName();
    }

    protected static boolean isArray(Object value) {
        return value != null && getClassName(value.getClass()).startsWith("[");
    }

    protected static boolean isList(Object value) {
        return value instanceof List;
    }

    protected static List<Object> toObjectList(List list) {
        List<Object> objs = new ArrayList<>();
        for(var it : list) 
        	objs.add(it);
        return objs;
    }

    @SafeVarargs
    protected static <O> List<Object> toObjectList(O... array) {
        return toObjectList(Arrays.asList(array));
    }

    private static boolean isRawData(Object value) {
        return value instanceof Integer
                || value instanceof Long
                || value instanceof Double
                || value instanceof Float
                || value instanceof Boolean
                || value instanceof Character
                || value instanceof Byte
                || value instanceof CharSequence;
    }

    public static <T> T fromJson(nx.peter.api.json.core.JsonObject j, Class<T> clazz) {
        var json = (JsonObject) j;
        if (json.hasName("className")) {
            Map<String, Object> detail = new LinkedHashMap<>();
            try {
                var clazzStr = json.readString("className");
                if (clazzStr instanceof JsonNull
                        || !clazz.equals(Class.forName(clazzStr.getString()))) return null;
                for (String key : json.getNames())
                    if (!key.contentEquals("className")) {
                        var value = json.readValue(key);
                        if (isRawData(value)) detail.put(key, value);
                        else if (value instanceof JsonObject obj) {
                            Map<String, Object> map = new LinkedHashMap<>();
                            for (var k : obj.getNames())
                                map.put(k, obj.readValue(k).get());
                            detail.put(key, map);
                        } else if (value instanceof JsonArray arr) {
                            List<Object> list = new ArrayList<>();
                            for (var v : arr) list.add(v.get());
                            detail.put(key, list);
                        }
                    }
                return getObject(detail, clazz);
            } catch (ClassNotFoundException | ClassCastException e) {
                return null;
            }
        }
        return null;
    }

    public static List<Object> sortByName(Map<String, Object> detail, List<String> names) {
        if (detail == null | names == null) return new ArrayList<Object>();
        List<Object> vals = new ArrayList<>();
        // System.out.println("Sort");
        for (var name : names) vals.add(detail.get(name));
        // System.out.println(detail.get(name) + ": " + name);
        return vals;
    }

    public static boolean isCompatibile(Set<String> params, Class clazz) {
        List<String> fields = getFieldNames(clazz);
        boolean good = fields.size() == params.size();
        if (good) {
            for (var field : fields) if (!params.contains(field)) return false;
            return true;
        }
        return false;
    }

    public static List<String> getFieldNames(Class clazz) {
        var fields = clazz.getFields();
        List<String> names = new ArrayList<>();
        for (var field : fields) names.add(field.getName());
        return names;
    }

    public static List<Field> getFields(Class clazz) {
        return new ArrayList<>(Arrays.asList(clazz.getFields()));
    }

    public static boolean equals(List<String> fields, List<String> parameters) {
        if (fields.size() == parameters.size()) {
            for (int index = 0; index < fields.size(); index++)
                if (!fields.get(index).equals(parameters.get(index))) return false;
            return true;
        }
        return false;
    }

    public static boolean equals(Class clazz, Constructor constructor) {
        if (clazz.getFields().length == constructor.getParameters().length) {
            for (Field field : clazz.getFields()) if (!contains(constructor, field)) return false;
            return true;
        }
        return false;
    }

    public static List<String> getSortedParameterNames(Constructor constructor, Class clazz) {
        if (clazz == null | constructor == null) return new ArrayList<>();
        List<String> names = new ArrayList<>();
        if (equals(clazz, constructor))
            for (Parameter param : constructor.getParameters())
                for (Field field : clazz.getFields())
                    if (param.getType().equals(field.getType()) && !names.contains(field.getName()))
                        names.add(field.getName());
        return names;
    }

    public static boolean contains(Constructor constructor, Field field) {
        if (field == null | constructor == null) return false;
        return getParameterTypes(constructor).contains(field.getType());
    }

    public static List<Class> getParameterTypes(Constructor constructor) {
        if (constructor == null) return new ArrayList<>();
        Parameter[] parameters = constructor.getParameters();
        List<Class> types = new ArrayList<>();
        for (Parameter field : parameters) types.add(field.getType());
        return types;
    }

    public static List<String> getParameterNames(Constructor constructor) {
        if (constructor == null) return new ArrayList<>();
        Parameter[] parameters = constructor.getParameters();
        List<String> names = new ArrayList<>();
        for (Parameter param : parameters) names.add(param.getName());
        return names;
    }

    public static String getName(Class clazz) {
        if (clazz == null) return "";
        String name = clazz.getName();
        if (name.contains(".")) name = name.substring(name.lastIndexOf(".") + 1);
        return name;
    }

    public static ObjectDetail.Type getType(Object obj) {
        if (obj == null) return ObjectDetail.Type.None;
        // Class type = obj.getClass();
        if (Util.isInt(obj)) return ObjectDetail.Type.Integer;
        else if (Util.isBoolean(obj)) return ObjectDetail.Type.Boolean;
        else if (Util.isFloat(obj)) return ObjectDetail.Type.Float;
        else if (Util.isDouble(obj)) return ObjectDetail.Type.Double;
        else if (Util.isLong(obj)) return ObjectDetail.Type.Long;
        else if (obj instanceof Character) return ObjectDetail.Type.Character;
        else if (obj instanceof CharSequence) return ObjectDetail.Type.String;
        else if (obj instanceof Map) return ObjectDetail.Type.Map;
        else if (obj instanceof List) return ObjectDetail.Type.List;
        else return ObjectDetail.Type.Object;
    }

    public static String getSqlType(ObjectDetail.Type type) {
        return switch (type) {
            case Boolean -> "BOOLEAN";
            case Double -> "DECIMAL(11, 4)";
            case Enum -> "ENUM";
            case Float -> "FLOAT(8)";
            case Integer -> "INTEGER";
            case Long -> "BIGINT";
            case Object -> "OBJECT";
            case String -> "VARCHAR(200)";
            default -> "";
        };
    }

    public static int toInt(Object obj, int def) {
        if (getType(obj).equals(ObjectDetail.Type.Integer)) return Integer.parseInt(obj.toString());
        return def;
    }

    public static double toDouble(Object obj, double def) {
        if (getType(obj).equals(ObjectDetail.Type.Double))
            return Double.parseDouble(obj.toString());
        return def;
    }

    public static float toFloat(Object obj, float def) {
        if (getType(obj).equals(ObjectDetail.Type.Double)) return Float.parseFloat(obj.toString());
        return def;
    }

    public static long toLong(Object obj, long def) {
        if (getType(obj).equals(ObjectDetail.Type.Double)) return Long.parseLong(obj.toString());
        return def;
    }

    public static boolean toBoolean(Object obj, boolean def) {
        if (getType(obj).equals(ObjectDetail.Type.Double))
            return Boolean.parseBoolean(obj.toString());
        return def;
    }

    public static class ObjectDetail<T> {
        public final List<String> names;
        public final List<Object> values;
        public final List<Type> types;

        public final Class<T> clazz;

        public enum Type {
            Boolean,
            Character,
            Double,
            Enum,
            Float,
            Integer,
            List,
            Long,
            Map,
            None,
            Object,
            String;

            public boolean isObject() {
                return this.equals(Object);
            }

            public boolean isInteger() {
                return this.equals(Integer);
            }

            public boolean isString() {
                return this.equals(String);
            }

            public boolean isNone() {
                return this.equals(None);
            }
        }

        public ObjectDetail() {
            this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null);
        }

        public ObjectDetail(
                List<String> names, List<Object> values, List<Type> type, Class<T> clazz) {
            this.names = names;
            this.values = values;
            this.types = type;
            this.clazz = clazz;
        }

        public Object get(String name) {
            return containsName(name) ? values.get(indexOf(name)) : null;
        }

        public <V> List<V> getList(String name) {
            return getType(name).equals(Type.List) ? (List<V>) values.get(indexOf(name)) : null;
        }

        public <K, V> Map<K, V> getMap(String name) {
            return getType(name).equals(Type.Map) ? (Map<K, V>) values.get(indexOf(name)) : null;
        }

        public String getString(String name) {
            return getType(name).equals(Type.String) ? (String) get(name) : null;
        }

        public double getDouble(String name, double defaultValue) {
            return getType(name).equals(Type.Double) ? (double) get(name) : defaultValue;
        }

        public float getFloat(String name, float defaultValue) {
            return getType(name).equals(Type.Float) ? (float) get(name) : defaultValue;
        }

        public <E extends Enum<E>> E getEnum(String name, Class<E> type) {
            Object value = get(name);
            return getType(name).equals(Type.Enum) && value.getClass().equals(type)
                    ? (E) value
                    : null;
        }

        public long getLong(String name, long defaultValue) {
            return getType(name).equals(Type.Long) ? (long) get(name) : defaultValue;
        }

        public int getInt(String name, int defaultValue) {
            return getType(name).equals(Type.Integer) ? (int) get(name) : defaultValue;
        }

        public List<Type> getTypes() {
            return types;
        }

        public List<DataType> getDataTypes() {
            List<DataType> dataTypes = new ArrayList<>();

            for (var name : names)
                dataTypes.add(new IDataType(name, Advanced.getSqlType(getType(name))));

            return dataTypes;
        }

        public DataType getDataType(String name) {
            int index = indexOf(name);
            if (index == -1)
                return null;
            return getDataTypes().get(index);
        }

        public boolean getBoolean(String name, boolean defaultValue) {
            return getType(name).equals(Type.Boolean) ? (boolean) get(name) : defaultValue;
        }

        public Type getType(String name) {
            return containsName(name) ? types.get(indexOf(name)) : Type.None;
        }

        public boolean containsName(String name) {
            return names.contains(name);
        }

        public int indexOf(String name) {
            return names.indexOf(name);
        }

        public boolean isEmpty() {
            return names.isEmpty() || values.isEmpty();
        }

        public boolean containsValue(Object value) {
            return values.contains(value);
        }

        public Iterator<String> names() {
            return names.iterator();
        }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new LinkedHashMap<>();
            for (String name : names) map.put(name, get(name));
            return map;
        }

        public String getName() {
            return Util.getClassName(clazz);
        }

        @Override
        public String toString() {
            return "Names: " + names.toString() + "\nValues: " + values.toString();
        }
    }

    // Database value type
    static class IDataType implements DataType {
        protected String name, type;

        public IDataType(String name, String type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String type() {
            return type;
        }
    }
}
