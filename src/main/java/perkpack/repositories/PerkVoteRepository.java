package perkpack.repositories;

import org.springframework.data.repository.CrudRepository;
import perkpack.models.PerkVote;

public interface PerkVoteRepository extends CrudRepository<PerkVote,Long> {
}
