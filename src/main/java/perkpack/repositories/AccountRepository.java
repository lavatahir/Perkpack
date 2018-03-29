package perkpack.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import perkpack.models.Account;

public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
    Account findByEmail(@Param("email") String email);
}
