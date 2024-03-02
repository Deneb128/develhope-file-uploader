package co.develhope.deneb128.fileuploader.repositories;

import co.develhope.deneb128.fileuploader.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Long> {
}
