package org.forest.web.controller;

import org.forest.api.controller.TreeApi;
import org.forest.api.model.Tree;
import org.forest.service.TreeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * TODO : Implement here the methods defined in TreeApi.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping
public class TreeController implements TreeApi {
    private final Logger logger = LoggerFactory.getLogger(TreeController.class);

    private TreeService treeService;

    @Autowired
    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }


    @Override
    public ResponseEntity<List<Tree>> listTrees() {
        return ResponseEntity.ok(treeService.list().stream().map(this::map).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Tree> getTree(String id) {
        return getOptionalUUID(id)
                .flatMap(treeService::get)
                .map(this::map)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Tree> createTree(Tree tree) {
        org.forest.model.Tree saved = treeService.add(this.map(tree));

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(uri).body(map(saved));
    }

    @Override
    public ResponseEntity<String> deleteTree(String id) {
        treeService.deleteTree(UUID.fromString(id));

        return ResponseEntity.ok(id);
    }

    public ResponseEntity<Tree> updateTree(Tree tree) {
        org.forest.model.Tree modelTree = this.map(tree);

        if (treeService.get(modelTree.id()).isPresent()) {
            return ResponseEntity.ok(this.map(treeService.updateTree(modelTree)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Optional<UUID> getOptionalUUID(String uuid) {
        try {
            return Optional.of(UUID.fromString(uuid));
        } catch (IllegalArgumentException e) {
            logger.error("Error while parsing UUID", e);
            return Optional.empty();
        }
    }

    private org.forest.model.Tree map(org.forest.api.model.Tree apiTree) {
        return new org.forest.model.Tree(apiTree.getId(),
                apiTree.getBirth(),
                org.forest.model.Species.valueOf(apiTree.getSpecies().getValue()),
                org.forest.model.Exposure.valueOf(apiTree.getExposure().getValue()),
                apiTree.getCarbonStorageCapacity()
        );
    }

    private org.forest.api.model.Tree map(org.forest.model.Tree modelTree) {
        return new org.forest.api.model.Tree()
                .id(modelTree.id())
                .birth(modelTree.birth())
                .species(org.forest.api.model.Species.valueOf(modelTree.species().name()))
                .exposure(org.forest.api.model.Exposure.valueOf(modelTree.exposure().name()))
                .carbonStorageCapacity(modelTree.carbonStorageCapacity());
    }
}
