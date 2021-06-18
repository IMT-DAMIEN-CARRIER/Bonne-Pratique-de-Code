package org.forest.persistence.impl;

import org.forest.model.Forest;
import org.forest.model.Species;
import org.forest.model.Tree;
import org.forest.persistence.ForestRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InMemoryForestRepositoryImpl implements ForestRepository {
    private List<Forest> mutableRepository = new ArrayList<>();

    @Override
    public Optional<Forest> findOneById(UUID id) {
        return mutableRepository.stream()
                .filter(forest -> id.equals(forest.id()))
                .findFirst();
    }

    @Override
    public List<Forest> findAll() {
        return mutableRepository;
    }

    @Override
    public UUID insert(Forest forest) {
        final Forest persisted = new Forest(
                UUID.randomUUID(),
                forest.type(),
                forest.trees(),
                forest.surface()
        );
        mutableRepository.add(persisted);

        return persisted.id();
    }

    @Override
    public void delete(UUID id) throws NoSuchElementException {
        final Optional<Forest> persisted = findOneById(id);

        if (persisted.isPresent()) {
            mutableRepository.remove(persisted);
        } else {
            throw new NoSuchElementException("Forest with id [" + id + "] does not exists");
        }
    }

    @Override
    public Forest update(Forest forest) throws NoSuchElementException {
        final Optional<Forest> persistedForest = findOneById(forest.id());

        if (persistedForest.isPresent()) {
            int index = mutableRepository.indexOf(forest);
            mutableRepository.set(index, forest);
        } else {
            throw new NoSuchElementException("Forest with id [" + forest.id() + "] does not exists");
        }

        return forest;
    }

    @Override
    public List<Species> getAllSpeciesByForest(UUID id) throws NoSuchElementException {
        final Optional<Forest> forest = findOneById(id);

        if (forest.isPresent()) {
            return forest.get().trees().stream().map(Tree::species).distinct().collect(Collectors.toList());
        } else {
            throw new NoSuchElementException("Tree with id [" + id + "] does not exists");
        }
    }
}
