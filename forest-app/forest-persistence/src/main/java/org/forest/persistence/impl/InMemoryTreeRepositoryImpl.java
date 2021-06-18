package org.forest.persistence.impl;

import org.forest.persistence.TreeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.forest.model.Tree;
import org.springframework.stereotype.Service;

@Service
public class InMemoryTreeRepositoryImpl implements TreeRepository {

    private List<Tree> mutableRepository = new ArrayList<>();

    @Override
    public Optional<Tree> findOneById(UUID id) {
        return mutableRepository.stream()
                .filter(tree -> id.equals(tree.id()))
                .findFirst();
    }

    @Override
    public List<Tree> findAll() {
        return mutableRepository;
    }

    @Override
    public UUID insert(Tree tree) {
        final Tree persisted = new Tree(UUID.randomUUID(), tree.birth(), tree.species(), tree.exposure(), tree.carbonStorageCapacity());
        mutableRepository.add(persisted);

        return persisted.id();
    }
}
