package com.highschool.highschoolsystem.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileUploadUtils {
    public static void saveFile(String path, String fileName, MultipartFile file) throws Exception {
        Path uploadPath = Paths.get(path);
//        path = /static/images/user-photos/
//        fileName = 1.jpg
//        save file to /static/images/user-photos/1.jpg
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, REPLACE_EXISTING);
        } catch (Exception e) {
            throw new Exception("Could not save file: " + fileName, e);
        }

    }
}
