package perkpack.repositories;

import models.Perk;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "perks", path = "perks")
public interface PerkRepository extends PagingAndSortingRepository<Perk, Long> {
    Perk findByName(@Param("name") String name);
}
