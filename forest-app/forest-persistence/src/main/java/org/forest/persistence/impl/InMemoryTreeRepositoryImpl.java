package org.forest.persistence.impl;

import org.forest.persistence.TreeRepository;

import java.util.*;

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

    @Override
    public void delete(UUID id) throws NoSuchElementException {
        final Optional<Tree> persisted = findOneById(id);

        if (persisted.isPresent()) {
            mutableRepository.remove(persisted);
        } else {
            throw new NoSuchElementException("Tree with id [" + id + "] does not exists");
        }
    }

    @Override
    public Tree update(Tree tree) throws NoSuchElementException {
        final Optional<Tree> persistedTree = findOneById(tree.id());

        if (persistedTree.isPresent()) {
            int index = mutableRepository.indexOf(tree);
            mutableRepository.set(index, tree);
        } else {
            throw new NoSuchElementException("Tree with id [" + tree.id() + "] does not exists");
        }

        return tree;
    }
}
