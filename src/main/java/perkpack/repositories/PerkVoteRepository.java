package perkpack.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import perkpack.models.PerkVote;

import java.util.List;

public interface PerkVoteRepository extends PagingAndSortingRepository<PerkVote,Long> {
    List<PerkVote> findByName(@Param("name") String name);
}
