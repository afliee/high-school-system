package com.highschool.highschoolsystem.util;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileUploadUtils {
    private static final String IMAGE_PHOTO_UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/uploads/images/user-photos/";
    public static final String ASSIGNMENT_UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/uploads/assignments/";
//    accept file type pdf, doc, docx, ppt, pptx, xls, xlsx, jpg, png, jpeg, txt
    private static final String[] ASSIGNMENT_TYPE_ACCEPT = {
            "text/plain",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "image/jpg",
            "image/png",
            "image/jpeg"
    };
    public static void saveFile(String path, String fileName, MultipartFile file) throws Exception {
        Path uploadPath = Paths.get(IMAGE_PHOTO_UPLOAD_DIR);
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

    public static void saveAssignmentFile(String path, String filename, MultipartFile file) throws Exception {
        if (!isAssignmentTypeAccept(file.getContentType())) {
            throw new Exception("File type not accept");
        }

        String uploadPathStr = ASSIGNMENT_UPLOAD_DIR + path;
        Path uploadPath = Paths.get(uploadPathStr.replaceAll("/", "\\\\").trim());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(filename);
            Files.copy(inputStream, filePath, REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not save file: " + filename, e);
        }
    }

    private static boolean isAssignmentTypeAccept(String type) {
        for (String s : ASSIGNMENT_TYPE_ACCEPT) {
            if (s.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public static void removeFile(String path) throws Exception {
//        remove file
        String uploadPathStr = ASSIGNMENT_UPLOAD_DIR + path;
        Path uploadPath = Paths.get(uploadPathStr.replaceAll("/", "\\\\").trim());
//uploads\assignments\831c1ea3-c06c-4f9d-b2ca-11a3f53813d0\e88e8137-58d2-441c-8db4-ec7f4676a383\MidTerm.pdf
        try {
            System.out.println("uploadPath: " + uploadPath);
            FileUtils.deleteDirectory(uploadPath.getParent().toFile());
//            remove dir if emty with folder name 831c1ea3-c06c-4f9d-b2ca-11a3f53813d0
//            Files.delete(uploadPath.getParent());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not remove file: " + path, e);
        }
    }

    public static void removeDir(String path) throws Exception {
//        remove file
        String uploadPathStr = ASSIGNMENT_UPLOAD_DIR + path.substring(0, path.lastIndexOf("/"));
        Path uploadPath = Paths.get(uploadPathStr.replaceAll("/", "\\\\").trim());

        try {
            System.out.println("uploadPath: " + uploadPath);
            FileUtils.deleteDirectory(uploadPath.getParent().toFile());

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not remove file: " + path, e);
        }
    }

    public static void createDir(String path) throws Exception {
        String uploadPathStr = ASSIGNMENT_UPLOAD_DIR + path;
        Path uploadPath = Paths.get(uploadPathStr.replaceAll("/", "\\\\").trim());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }
}
