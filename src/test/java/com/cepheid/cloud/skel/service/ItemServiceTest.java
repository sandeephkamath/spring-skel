package com.cepheid.cloud.skel.service;

import com.cepheid.cloud.skel.exception.ItemNotFoundException;
import com.cepheid.cloud.skel.model.*;
import com.cepheid.cloud.skel.repository.ItemRepository;
import com.cepheid.cloud.skel.repository.ItemSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Test
    void getAll_shouldReturnPagedItemsFromRepository() {
        Item item = getItem();
        ItemFilter itemFilter = ItemFilter.builder().name("item1").status(Status.VALID).build();
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Item> pagedItems = new PageImpl<>(singletonList(item));
        when(itemRepository.findAll(new ItemSpecification(itemFilter), pageRequest)).thenReturn(pagedItems);

        assertEquals(pagedItems, itemService.getAll(itemFilter, pageRequest));
    }

    @Test
    void getItem_shouldReturnItem_withGivenID() {
        Item item = getItem();
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertEquals(item, itemService.getItem(item.getId()));
    }

    @Test
    void getItem_shouldThrowItemNotFound_whenItemNotFound() {
        long id = 123L;
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.getItem(id));
    }

    @Test
    void add_shouldReturnPersistedItem() {
        Item item = getItem();
        item.setId(null);
        when(itemRepository.save(item)).thenReturn(item);

        assertEquals(item, itemService.add(item));
    }

    @Test
    void addDescription_shouldReturnPersistedItemWithNewDescription() {
        Description description = Description.builder().language("EN").build();
        Item item = getItem();
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(item.addDescription(description))).thenReturn(item);
        itemService.addDescription(item.getId(), description);
        assertEquals(3, item.getDescriptions().size());
    }

    @Test
    void update_shouldReturnUpdatedItem_whenItemWithIdPresent() {
        Item item = getItem();
        long itemId = item.getId();
        ItemDTO itemDTO = ItemDTO.builder().name(item.getName()).build();
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        assertEquals(item, itemService.update(itemId, itemDTO));
    }

    @Test
    void update_shouldThrowItemNotFoundException_whenItemNotPresent() {
        assertThrows(ItemNotFoundException.class, () -> itemService.update(123L, ItemDTO.builder().build()));
    }

    private Item getItem() {
        Item item = Item.builder().name("item1").build();
        item.setId(123L);
        Description description = Description.builder().language("EN").shortDescription("s1").build();
        item.setDescriptions(new ArrayList<>(singletonList(description)));
        return item;
    }
}