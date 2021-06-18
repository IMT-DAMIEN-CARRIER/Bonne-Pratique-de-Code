package org.forest.service.impl;

import org.forest.model.Tree;
import org.forest.persistence.TreeRepository;
import org.forest.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TreeServiceImpl implements TreeService {

    private TreeRepository treeRepository;

    @Autowired
    public TreeServiceImpl(TreeRepository treeRepository) {
        this.treeRepository = treeRepository;
    }

    @Override
    public Optional<Tree> get(UUID uuid) {
        return this.treeRepository.findOneById(uuid);
    }

    @Override
    public List<Tree> list() {
        return this.treeRepository.findAll();
    }

    @Override
    public UUID add(Tree tree) {
        return this.treeRepository.insert(tree);
    }

    @Override
    public void deleteTree(UUID id) {
        this.treeRepository.delete(id);
    }

    @Override
    public Tree updateTree(Tree tree) {
        return this.treeRepository.update(tree);
    }
}
