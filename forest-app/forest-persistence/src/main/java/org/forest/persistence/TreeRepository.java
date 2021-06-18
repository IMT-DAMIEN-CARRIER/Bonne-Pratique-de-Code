package org.forest.persistence;

import org.forest.model.Tree;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TreeRepository {

    Optional<Tree> findOneById(UUID id);

    List<Tree> findAll();

    UUID insert(Tree tree);

    void delete(UUID id);

    Tree update(Tree tree);
}
