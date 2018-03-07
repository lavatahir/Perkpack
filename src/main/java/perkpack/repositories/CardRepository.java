package perkpack.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import perkpack.models.Card;

public interface CardRepository extends PagingAndSortingRepository<Card, Long> {
    Card findByName(String name);
}
