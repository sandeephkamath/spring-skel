package com.cepheid.cloud.skel;

import com.cepheid.cloud.skel.exception.ItemNotFoundException;
import com.cepheid.cloud.skel.model.*;
import com.cepheid.cloud.skel.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest extends TestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getItems_shouldFetchPagedItems_withPageParamsAndFilters() throws Exception {
        Item item = getItem();
        ItemFilter itemFilter = new ItemFilter(item.getName(), Status.INVALID);
        Page<Item> pagedResponse = new PageImpl<>(Collections.singletonList(item));

        when(itemService.getAll(itemFilter, PageRequest.of(0, 5))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/1.0/items")
                .param("name", itemFilter.getName())
                .param("status", itemFilter.getStatus().name())
                .param("page", "0")
                .param("size", "5"))
                .andExpect(content().string(objectMapper.writeValueAsString(pagedResponse)))
                .andExpect(status().isOk());
    }

    @Test
    public void getItem_shouldFetchItemWithId_whenItemPresent() throws Exception {
        Item item = getItem();
        when(itemService.getItem(item.getId())).thenReturn(item);
        mockMvc.perform(get("/api/1.0/items/{id}", item.getId()))
                .andExpect(content().string(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk());
    }

    @Test
    public void getItem_shouldThrow404_whenItemNotPresent() throws Exception {
        Item item = getItem();
        when(itemService.getItem(item.getId())).thenThrow(ItemNotFoundException.class);
        mockMvc.perform(get("/api/1.0/items/{id}", item.getId()))
                .andExpect(status().reason(ItemNotFoundException.REASON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void add_shouldReturnAddedItem_whenPersisted() throws Exception {
        Item item = getItem();
        when(itemService.add(item)).thenReturn(item);

        mockMvc.perform(post("/api/1.0/items/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)))
                .andExpect(content().string(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk());
    }

    @Test
    public void addDescription_shouldReturnItem_whenPersisted() throws Exception {
        Item item = getItem();
        Description description = getDescription();
        when(itemService.addDescription(item.getId(), description)).thenReturn(item);

        mockMvc.perform(post("/api/1.0/items/{itemId}/description", item.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(description)))
                .andExpect(content().string(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk());
    }

    @Test
    public void update_shouldReturnUpdatedItem() throws Exception {
        Item item = getItem();
        ItemDTO itemDTO = getItemDTO();
        when(itemService.update(item.getId(), itemDTO)).thenReturn(item);
        mockMvc.perform(patch("/api/1.0/items/{id}", item.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(content().string(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateDescription_shouldReturnUpdatedItem() throws Exception {
        Item item = getItem();
        Description description = getDescription();
        when(itemService.updateDescription(item.getId(), description, description.getId())).thenReturn(item);
        mockMvc.perform(put("/api/1.0/items/{id}/description/{descriptionId}", item.getId(), description.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(description)))
                .andExpect(content().string(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk());
    }

    @Test
    public void update_shouldThrow404_whenItemNotPresent() throws Exception {
        Item item = getItem();
        ItemDTO itemDTO = getItemDTO();
        when(itemService.update(item.getId(), itemDTO)).thenThrow(ItemNotFoundException.class);
        mockMvc.perform(patch("/api/1.0/items/{id}", item.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().reason(ItemNotFoundException.REASON))
                .andExpect(status().isNotFound());
    }

    private Item getItem() {
        Item item = Item.builder().name("item1").build();
        item.setId(123L);
        return item;
    }

    private Description getDescription() {
        Description description = Description.builder().longDescription("ld").build();
        description.setId(1234L);
        return description;
    }

    private ItemDTO getItemDTO() {
        return ItemDTO.builder().name("item1").build();
    }


}
