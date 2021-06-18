package org.forest.service.impl;

import org.forest.model.Exposure;
import org.forest.model.Species;
import org.forest.model.Tree;
import org.forest.persistence.TreeRepository;
import org.forest.service.TreeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreeServiceImplTest {

    @Mock
    private TreeRepository treeRepository;

    @InjectMocks
    private TreeService treeService = new TreeServiceImpl(treeRepository);

    @Test
    void shouldGetATree(){
        // GIVEN
        final UUID uuid = UUID.randomUUID();
        final Tree repositoryTree = new Tree(uuid, LocalDate.now(), Species.OAK, Exposure.SHADOW, 40);
        when(treeRepository.findOneById(uuid)).thenReturn(Optional.of(repositoryTree));

        // WHEN
        Optional<Tree> tree = treeService.get(uuid);

        // THEN
        assertTrue(tree.isPresent());
        assertEquals(uuid, tree.map(Tree::id).get());
    }

    @Test
    void shouldGetAll() {
        // GIVEN
        when(treeRepository.findAll()).thenReturn(List.of(
                new Tree(UUID.randomUUID(), LocalDate.now(), Species.OAK, Exposure.SHADOW, 40.0),
                new Tree(UUID.randomUUID(), LocalDate.now(), Species.OAK, Exposure.SHADOW, 40.0)
        ));

        // WHEN
        List<Tree> all = treeService.list();

        // THEN
        assertEquals(2, all.size());
    }

    @Test
    void shouldSaveATree() {
        UUID generatedId = UUID.randomUUID();
        Tree newTree = new Tree(generatedId, LocalDate.now(), Species.OAK, Exposure.SHADOW, 40.0);

        when(treeRepository.insert(any(Tree.class))).thenReturn(newTree);

        assertEquals(generatedId, treeService.add(newTree).id());
    }

    @Test
    void shouldUpdateATree() {
        UUID generatedId = UUID.randomUUID();
        Tree updatedTree = new Tree(generatedId, LocalDate.now(), Species.FIR, Exposure.SHADOW, 40.0);

        // WHEN
        treeService.updateTree(updatedTree);

        // THEN
        verify(treeRepository, times(1)).update(updatedTree);
    }

    @Test
    void shouldDeleteATree(){
        // GIVEN
        UUID uuid = UUID.randomUUID();

        // WHEN
        treeService.deleteTree(uuid);

        // THEN
        verify(treeRepository, times(1)).delete(uuid);
    }
    // GIVEN
    // WHEN
    // THEN
}