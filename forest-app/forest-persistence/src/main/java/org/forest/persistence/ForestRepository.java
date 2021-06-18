package org.forest.persistence;

import org.forest.model.Forest;
import org.forest.model.Species;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ForestRepository {
    Optional<Forest> findOneById(UUID id);

    List<Forest> findAll();

    UUID insert(Forest forest);

    void delete(UUID id);

    Forest update(Forest forest);

    List<Species> getAllSpeciesByForest(UUID id);
}
