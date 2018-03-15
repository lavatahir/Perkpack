package perkpack.repositories;

import org.springframework.data.repository.query.Param;
import perkpack.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByEmail(@Param("email") String email);
}
