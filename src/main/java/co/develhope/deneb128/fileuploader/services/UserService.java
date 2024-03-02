package co.develhope.deneb128.fileuploader.services;

import co.develhope.deneb128.fileuploader.dto.DownloadedProfilePictureDTO;
import co.develhope.deneb128.fileuploader.entities.User;
import co.develhope.deneb128.fileuploader.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public User create(User user) {
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getOne(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            return null;
        }
        return foundUser.get();
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public void delete(Long id) {
        User foundUser = getUser(id);
        if(foundUser.getProfilePicture() != null){
            fileStorageService.remove(foundUser.getProfilePicture());
        }
        userRepository.deleteById(id);
    }

    @SneakyThrows
    private User getUser(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (!foundUser.isPresent()) throw new Exception("User is not present");
        return foundUser.get();
    }

    @SneakyThrows
    public User uploadProfilePicture(Long id, MultipartFile profilePicture) {
        User foundUser = getUser(id);
        if(foundUser.getProfilePicture() != null){
            fileStorageService.remove(foundUser.getProfilePicture());
        }
        String fileName = fileStorageService.upload(profilePicture);
        foundUser.setProfilePicture(fileName);
        fileStorageService.setLastProfilePictureSaved(fileName);
        return userRepository.save(foundUser);
    }

    @SneakyThrows
    public DownloadedProfilePictureDTO downloadProfilePicture(Long id) {
        User foundUser = getUser(id);

        DownloadedProfilePictureDTO dto = new DownloadedProfilePictureDTO();
        dto.setUser(foundUser);

        if (foundUser.getProfilePicture() == null) return dto;
        byte[] profilePictureBytes = fileStorageService.download(foundUser.getProfilePicture());
        dto.setProfilePicture(profilePictureBytes);
        return dto;
    }
}
