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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
}