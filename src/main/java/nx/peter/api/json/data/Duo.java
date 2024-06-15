package nx.peter.api.json.data;

/**
 * An object that keeps two (2) values
 * @param <First> first value
 * @param <Second> second value
 * @version 1.0
 */
public interface Duo<First, Second> {
    First getFirst();
    Second getSecond();
}
