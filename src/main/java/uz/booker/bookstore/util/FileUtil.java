package uz.booker.bookstore.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class FileUtil {

    @Value("${upload.path}")
    private String uploadPath;


    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, fileName);
        Files.write(filePath, file.getBytes());
        return fileName;
    }


    public void downloadFile(String fileName, HttpServletResponse response) throws IOException {
        // Определяем заголовок для скачивания файла
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentLength(0); // Добавляем эту строку


        // Открываем поток ввода-вывода для копирования файла
        try (FileInputStream inputStream = new FileInputStream(uploadPath + File.separator + fileName);
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        // Загрузка в папку "Downloads" в Windows
        saveToWindowsDownloadFolder(fileName);
    }

    private void saveToWindowsDownloadFolder(String fileName) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            String windowsDownloadPath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + fileName;
            Files.copy(Paths.get(uploadPath, fileName), Paths.get(windowsDownloadPath));
        }
    }
}
