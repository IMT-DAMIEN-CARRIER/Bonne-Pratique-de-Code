package org.forest.service.impl;

import org.forest.model.Forest;
import org.forest.persistence.ForestRepository;
import org.forest.service.CO2AbsorptionService;
import org.springframework.stereotype.Service;

@Service
public class CO2AbsorptionServiceImpl implements CO2AbsorptionService {
    private ForestRepository forestRepository;

    public CO2AbsorptionServiceImpl(ForestRepository forestRepository) {
        this.forestRepository = forestRepository;
    }

    @Override
    public double getAbsorption(Forest forest) {
        throw new UnsupportedOperationException("TODO : implement this method");
    }
}
