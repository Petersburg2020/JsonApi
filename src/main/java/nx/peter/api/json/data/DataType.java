package nx.peter.api.json.data;

public interface DataType {
    String name();
    String type();
    
    default boolean isObject() {
        return name().equals("OBJECT");
    }
    
    default boolean isString() {
        return name().startsWith("VARCHAR");
    }
    
    default boolean isEmpty() {
        return name().isEmpty();
    }
}
