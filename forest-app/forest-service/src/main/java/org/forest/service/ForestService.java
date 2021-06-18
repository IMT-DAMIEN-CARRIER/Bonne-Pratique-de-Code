package org.forest.service;

import org.forest.model.Forest;
import org.forest.model.Species;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ForestService {

    Optional<Forest> get(UUID uuid);

    List<Forest> list();

    UUID add(Forest forest);

    void deleteForest(UUID id);

    Forest updateForest(Forest forest);

    List<Species> getForestSpecies(UUID id);

    double getSumCapacityStorageForest(UUID id);

    double getForestAbsorbtionCapacity(UUID id);
}
