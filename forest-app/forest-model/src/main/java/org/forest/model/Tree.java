package org.forest.model;

import java.time.LocalDate;
import java.util.UUID;

public record Tree(UUID id,
            LocalDate birth,
            Species species,
            Exposure exposure,
            double carbonStorageCapacity // Between 10 and 50 kg/year
) {
}
