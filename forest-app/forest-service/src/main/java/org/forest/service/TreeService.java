package org.forest.service;

import org.forest.model.Tree;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * TODO : Here, you will have to create the service methods and implement them in the impl package.
 */
public interface TreeService {

    Optional<Tree> get(UUID uuid);

    List<Tree> list();

    UUID add(Tree tree);

    void deleteTree(UUID id);

    Tree updateTree(Tree tree);
}
