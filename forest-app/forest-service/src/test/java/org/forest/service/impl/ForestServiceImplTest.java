package org.forest.service.impl;

import org.forest.model.*;
import org.forest.persistence.ForestRepository;
import org.forest.service.ForestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ForestServiceImplTest {
    @Mock
    private ForestRepository forestRepository;

    @InjectMocks
    private ForestService forestService = new ForestServiceImpl(forestRepository);

    @Test
    void shouldGetATree(){
        // GIVEN
        final UUID uuid = UUID.randomUUID();
        final List<Tree> trees = new ArrayList<>();
        final Forest repositoryForest = new Forest(uuid, ForestType.TROPICAL, trees, 100000.00);
        when(forestRepository.findOneById(uuid)).thenReturn(Optional.of(repositoryForest));

        // WHEN
        Optional<Forest> forest = forestService.get(uuid);

        // THEN
        assertTrue(forest.isPresent());
        assertEquals(uuid, forest.map(Forest::id).get());
    }

    @Test
    void shouldGetAll() {
        // GIVEN
        final List<Tree> trees = new ArrayList<>();

        when(forestRepository.findAll()).thenReturn(List.of(
                new Forest(UUID.randomUUID(), ForestType.TROPICAL, trees, 100000.00),
                new Forest(UUID.randomUUID(), ForestType.BOREAL, trees, 100000.00)
        ));

        // WHEN
        List<Forest> all = forestService.list();

        // THEN
        assertEquals(2, all.size());
    }

    @Test
    void shouldSaveATree() {
        UUID generatedId = UUID.randomUUID();
        final List<Tree> trees = new ArrayList<>();
        Forest newForest = new Forest(generatedId, ForestType.TROPICAL, trees, 100000.00);

        when(forestRepository.insert(any(Forest.class))).thenReturn(newForest.id());

        assertEquals(generatedId, forestService.add(newForest));
    }

    @Test
    void shouldUpdateATree() {
        UUID generatedId = UUID.randomUUID();
        final List<Tree> trees = new ArrayList<>();
        Forest forest = new Forest(generatedId, ForestType.TROPICAL, trees, 100000.00);

        // WHEN
        forestService.updateForest(forest);

        // THEN
        verify(forestRepository, times(1)).update(forest);
    }

    @Test
    void shouldDeleteATree() {
        // GIVEN
        UUID uuid = UUID.randomUUID();

        // WHEN
        forestService.deleteForest(uuid);

        // THEN
        verify(forestRepository, times(1)).delete(uuid);
    }

    @Test
    void shouldGetAllSpecies() {
        // GIVEN
        UUID generatedId = UUID.randomUUID();

        //WHEN
        forestService.getForestSpecies(generatedId);

        //THEN
        verify(forestRepository, times(1)).getAllSpeciesByForest(generatedId);
    }
}
