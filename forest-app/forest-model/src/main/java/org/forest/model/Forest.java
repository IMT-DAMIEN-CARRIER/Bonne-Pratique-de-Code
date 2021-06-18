package org.forest.model;

import java.util.List;
import java.util.UUID;

public record Forest(UUID id,
              ForestType type,
              List<Tree> trees,
              double surface) {
}
