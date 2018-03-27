package perkpack.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import perkpack.models.PerkVote;

public interface PerkVoteRepository extends PagingAndSortingRepository<PerkVote,Long> {
}
