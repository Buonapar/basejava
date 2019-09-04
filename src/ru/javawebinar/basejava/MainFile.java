package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class MainFile {
    private static void fileList(String directory) {
        File file = new File(directory);
        String[] dirList = file.list();
        for (String dirName : Objects.requireNonNull(dirList)) {
            File fileTemp = new File(directory + File.separator + dirName);
            if (fileTemp.isFile()) {
                System.out.println(dirName);
            } else {
                fileList(directory + File.separator + dirName);
            }
        }
    }

    public static void main(String[] args) {
        String filePath = ".\\.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/ru/javawebinar/basejava");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("\nСписок файлов в проекте:");
        fileList(dir.getPath());
    }
}
