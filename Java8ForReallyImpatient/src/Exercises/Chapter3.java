/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exercises;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 *
 * @author Trung Pham
 */
public class Chapter3 {

    static void logIf(Logger log, Level level, Supplier<Boolean> condition, Supplier<String> msg) {
        if (log.isLoggable(level)) {
            if (condition.get()) {
                log.log(level, msg.get());
            }
        }
    }

    static <T> Image transform(Image in, BiFunction<Color, T, Color> f, T arg) {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                out.getPixelWriter().setColor(x, y, f.apply(in.getPixelReader().getColor(x, y), arg));
            }
        }
        return out;
    }

    static void withLock(ReentrantLock myLock, Runnable action) {
        myLock.lock();
        try {
            action.run();
        } finally {
            myLock.unlock();
        }

    }

    public static Image transform(Image in, ColorTransformer f) {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x < 10 || y < 10 || (width - x) <= 10 || (height - y) <= 10) {
                    out.getPixelWriter().setColor(x, y,
                            f.apply(in.getPixelReader().getColor(x, y)));
                }
            }
        }
        return out;
    }

    public static Image transform(Image in, int thickness, ColorTransformer f) {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x < thickness || y < thickness || (width - x) <= thickness || (height - y) <= thickness) {
                    out.getPixelWriter().setColor(x, y,
                            f.apply(in.getPixelReader().getColor(x, y)));
                }
            }
        }
        return out;
    }

    public static Comparator<String> getComparator(boolean normal, boolean caseSensitive, boolean spaceSensitive) {
        return (a, b) -> {
            if (!normal) {
                StringBuilder builder = new StringBuilder(a);
                a = builder.reverse().toString();
            }
            if (!caseSensitive) {
                a = a.toLowerCase();
                b = b.toLowerCase();
            }
            if (!spaceSensitive) {
                a = a.replaceAll("\\s+", "");
                b = b.replaceAll("\\s+", "");
            }
            return a.compareTo(b);

        };
    }

    public <T> Comparator<T> lexicographicComparator(String... fields) {
        return (a, b) -> {

            for (String field : fields) {
                try {
                    Field tmp = a.getClass().getDeclaredField(field);
                    tmp.setAccessible(true);
                    String x = tmp.get(a).toString();
                    String y = tmp.get(b).toString();
                    if (x.compareTo(y) != 0) {
                        return x.compareTo(y);
                    }
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Chapter3.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return 0;

        };
    }

    @FunctionalInterface
    public interface ColorTransformer {

        Color apply(Color colorAtXY);
    }

    public static void main(String[] args) {
        Logger.getGlobal().setLevel(Level.SEVERE);

        logIf(Logger.getGlobal(), Level.WARNING, () -> true, () -> "HE HE");
        withLock(new ReentrantLock(), () -> System.out.println("LOCK!"));
        transform(null, (c) -> Color.GRAY);
        System.out.println(getComparator(false, false, false).compare("eH     Eh    ", "he  he"));
    }

}
