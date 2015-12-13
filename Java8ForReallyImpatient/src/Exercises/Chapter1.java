/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exercises;

import java.io.File;
import java.util.Arrays;
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int v = chooser.showOpenDialog(null);

        if (v == JFileChooser.APPROVE_OPTION) {
            File select = chooser.getSelectedFile();
            String[] result = new Chapter1().listFileWithExtension(select, ".exe");
            for (String file : result) {
                System.out.println(file);
            }
        }
    }

}
