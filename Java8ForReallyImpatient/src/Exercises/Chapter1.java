/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exercises;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author Trung Pham
 */
public class Chapter1 {

    public File[] listDirectoriesLambda(File file) {

        return file.listFiles((child) -> child.isDirectory());
    }

    public File[] listDirectoriesMethodReference(File file) {

        return file.listFiles(File::isDirectory);
    }

    public String[] listFileWithExtension(File file, String extension) {
        return file.list((File dir, String name) -> name.endsWith(extension));
    }

    public void sort(File[] files) {
        Arrays.sort(files, (file1, file2) -> {
            if (file1.isDirectory() != file2.isDirectory()) {
                return file1.isDirectory() ? -1 : 1;
            }
            return file1.compareTo(file2);
        });
    }

    public static interface RunnableEx {

        public void run() throws Exception;

        public static Runnable uncheck(RunnableEx run) {

            return () -> {
                try {
                    run.run();
                } catch (Exception ex) {
                    Logger.getLogger(RunnableEx.class.getName()).log(Level.SEVERE, null, ex);
                }
            };

        }

    }

    public static Runnable andThen(Runnable first, Runnable second) {
        return () -> {
            first.run();
            second.run();
        };
    }

    public void runArray() {
        String[] names = {"Tom", "Marray", "Paul"};
        List<Runnable> list = new ArrayList();
        for (String name : names) {//Cannot use for(int i ...) as i is not final
            list.add(() -> System.out.println(name));
        }
        for (Runnable run : list) {
            run.run();
        }
    }

    public void runAndThen() {
        andThen(() -> System.out.println("First"), () -> System.out.println("Second")).run();
    }

    public static interface Collections2<E> extends Collection<E> {

        public default void forEachIf(Consumer<E> action, Predicate<E> filter) {

            for (E e : this) {
                if (filter.test(e)) {
                    action.accept(e);
                }
            }
        }
    }

    static interface I {

        default void f() {
        }
    ;

    }

    static interface J {

        default void f() {
            System.out.println("HE HE");
        }
    ;

    }
    
    static class G implements I, J {

        @Override
        public void f() {

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        JFileChooser chooser = new JFileChooser();
//        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        int v = chooser.showOpenDialog(null);
//
//        if (v == JFileChooser.APPROVE_OPTION) {
//            File select = chooser.getSelectedFile();
//            String[] result = new Chapter1().listFileWithExtension(select, ".exe");
//            for (String file : result) {
//                System.out.println(file);
//            }
//        }
//        new Thread(RunnableEx.uncheck(() -> {
//            System.out.println("ZZZ");
//            Thread.sleep(1000);
//        })).start();
        new Chapter1().runArray();
    }

}
