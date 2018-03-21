package perkpack.repositories;

import org.springframework.data.repository.query.Param;
import perkpack.models.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findByEmail(@Param("email") String email);
}
