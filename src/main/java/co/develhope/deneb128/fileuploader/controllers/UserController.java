package co.develhope.deneb128.fileuploader.controllers;

import co.develhope.deneb128.fileuploader.dto.DownloadedProfilePictureDTO;
import co.develhope.deneb128.fileuploader.entities.User;
import co.develhope.deneb128.fileuploader.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(User user) {
        user.setId(null);
        return userService.create(user);
    }

    @SneakyThrows
    @PostMapping("/{id}/profile")
    public User uploadProfileImage(@PathVariable Long id, @RequestParam MultipartFile profilePicture) {
        return userService.uploadProfilePicture(id, profilePicture);
    }

    @GetMapping()
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {
        return userService.getOne(id);
    }

    @SneakyThrows
    @GetMapping("/{id}/profile")
    public @ResponseBody byte[] getProfileImage(@PathVariable Long id, HttpServletResponse response) {
        DownloadedProfilePictureDTO downloadedProfilePictureDTO = userService.downloadProfilePicture(id);
        String fileName = downloadedProfilePictureDTO.getUser().getProfilePicture();
        if(!StringUtils.hasText(fileName)) throw new Exception("User does not have a profile picture");
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
        return downloadedProfilePictureDTO.getProfilePicture();
    }

    @PutMapping("/{id}")
    public void update(@RequestBody User user, @PathVariable Long id) {
        user.setId(id);
        userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
