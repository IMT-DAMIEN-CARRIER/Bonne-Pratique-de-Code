package org.forest.service.impl;

import org.forest.model.Forest;
import org.forest.model.Species;
import org.forest.model.Tree;
import org.forest.persistence.ForestRepository;
import org.forest.service.ForestService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class ForestServiceImpl implements ForestService {
    private ForestRepository forestRepository;

    @Autowired
    public ForestServiceImpl(ForestRepository forestRepository) {
        this.forestRepository = forestRepository;
    }

    @Override
    public Optional<Forest> get(UUID uuid) {
        return this.forestRepository.findOneById(uuid);
    }

    @Override
    public List<Forest> list() {
        return this.forestRepository.findAll();
    }

    @Override
    public UUID add(Forest forest) {
        return this.forestRepository.insert(forest);
    }

    @Override
    public void deleteForest(UUID id) {
        this.forestRepository.delete(id);
    }

    @Override
    public Forest updateForest(Forest forest) {
        return this.forestRepository.update(forest);
    }

    @Override
    public List<Species> getForestSpecies(UUID id) {
        return this.forestRepository.getAllSpeciesByForest(id);
    }

    @Override
    public double getSumCapacityStorageForest(UUID id) {
        Optional<Forest> forest = this.get(id);

        if (forest.isPresent()) {
            return forest.get().trees().stream().map(Tree::carbonStorageCapacity)
                    .mapToDouble(a -> a)
                    .sum();
        }

        throw new NoSuchElementException("Forest with id [" + id + "] does not exists");
    }

    @Override
    public double getForestAbsorbtionCapacity(UUID id) {
        int numberOfSpecies = this.getForestSpecies(id).size();

        return 0.4 * numberOfSpecies * this.getSumCapacityStorageForest(id);
    }
}
