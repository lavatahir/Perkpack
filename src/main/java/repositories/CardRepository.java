package repositories;

import models.Card;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "cards", path = "cards")
public interface CardRepository extends PagingAndSortingRepository<Card, Long> {
    Card findByName(String name);
}
