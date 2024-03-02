package co.develhope.deneb128.fileuploader.controllers;

import co.develhope.deneb128.fileuploader.services.FileStorageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final FileStorageService fileStorageService;

    @GetMapping("/download")
    public byte[] download(@RequestParam String fileName, HttpServletResponse response) throws IOException {
        String ext = FilenameUtils.getExtension(fileName);
        switch (ext) {
            case "gif":
                response.setContentType(MediaType.IMAGE_GIF_VALUE);
                break;
            case "jpg":
            case "jpeg":
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                break;
            case "png":
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                break;
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        return fileStorageService.download(fileName);
    }

    @PostMapping("/upload")
    public List<String> upload(@RequestParam MultipartFile[] files) throws IOException {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile f : files) {
            String singleUploadedFileName = fileStorageService.upload(f);
            fileNames.add(singleUploadedFileName);
        }

        return fileNames;
    }

    @GetMapping("/latestprofilepicture")
    public byte[] downloadLatestProfilePicture(HttpServletResponse response) throws IOException {
        return this.download(fileStorageService.getLastProfilePictureSaved(), response);
    }

}
