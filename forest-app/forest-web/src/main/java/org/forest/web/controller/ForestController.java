package org.forest.web.controller;

import org.forest.api.controller.ForestApi;
import org.forest.api.model.Forest;
import org.forest.service.ForestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping
public class ForestController implements ForestApi {
    private final Logger logger = LoggerFactory.getLogger(ForestController.class);

    private ForestService forestService;

    @Autowired
    public ForestController(ForestService forestService) {
        this.forestService = forestService;
    }

//    private org.forest.model.Forest map(Forest apiForest) {
//        return new org.forest.model.Forest(apiForest.getId(),
//                org.forest.model.ForestType.valueOf(apiForest.getType().getValue()),
//                apiForest.getTrees().stream().map(tree ->
//                        new org.forest.model.Tree(
//                                tree.id(),
//                                tree.getBirth(),
//                                org.forest.model.Species.valueOf(tree.getSpecies().getValue()),
//                                org.forest.model.Exposure.valueOf(tree.getExposure().getValue()),
//                                tree.getCarbonStorageCapacity()
//                        )
//                ).collect(Collectors.toList()),
//                apiForest.getSurface()
//        );
//    }
//
//    private Forest map(org.forest.model.Forest forest) {
//        return new Forest()
//                .id(forest.id())
//                .trees(forest.trees())
//                .type(forest.type())
//                .surface(forest.surface());
//    }
}
