package co.develhope.deneb128.fileuploader.dto;

import co.develhope.deneb128.fileuploader.entities.User;
import lombok.Data;

@Data
public class DownloadedProfilePictureDTO {
    private User user;
    private byte[] profilePicture;
}
