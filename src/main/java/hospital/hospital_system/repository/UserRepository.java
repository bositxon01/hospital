package hospital.hospital_system.repository;

import hospital.hospital_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    //select * from users where username:=username
    Optional<User> findByUsername(String username);


}
