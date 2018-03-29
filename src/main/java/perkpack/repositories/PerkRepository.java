package perkpack.repositories;

import perkpack.models.Perk;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "perks", path = "perks")
public interface PerkRepository extends PagingAndSortingRepository<Perk, Long> {
    Perk findByName(@Param("name") String name);
    List<Perk> findAllByOrderByScoreDesc();
}
