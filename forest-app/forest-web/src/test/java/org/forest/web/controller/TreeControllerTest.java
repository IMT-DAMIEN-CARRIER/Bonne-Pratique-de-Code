package org.forest.web.controller;

import org.forest.model.Exposure;
import org.forest.model.Species;
import org.forest.model.Tree;
import org.forest.service.TreeService;
import org.forest.web.config.WebTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(TreeController.class)
@Import(WebTestConfig.class)
class TreeControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private TreeService treeService;

    @Autowired
    public TreeControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void shouldGetATreeById() throws Exception {
        // Given
        UUID uuid = UUID.randomUUID();

        // When
        when(treeService.get(uuid)).thenReturn(Optional.of(
                new Tree(uuid, LocalDate.now(), Species.OAK, Exposure.SHADOW, 40)
        ));

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/tree/%s".formatted(uuid.toString()))
                .accept(MediaType.ALL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.species").value("OAK"));

    }


}