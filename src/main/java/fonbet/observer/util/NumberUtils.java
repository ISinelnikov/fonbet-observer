package fonbet.observer.util;

public final class NumberUtils {
    private NumberUtils() {
    }

    public static double convert(String value) {
        if (value.isEmpty()) {
            return 0;
        }
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
