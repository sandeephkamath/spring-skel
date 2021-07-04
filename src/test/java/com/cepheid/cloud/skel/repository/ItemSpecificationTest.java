package com.cepheid.cloud.skel.repository;

import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.model.ItemFilter;
import com.cepheid.cloud.skel.model.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class ItemSpecificationTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void shouldFetchItemWithFilters() {
        Item item = getItem();
        itemRepository.save(item);
        ItemFilter itemFilter = ItemFilter.builder().name("n1").status(Status.VALID).build();
        ItemSpecification itemSpecification = new ItemSpecification(itemFilter);
        Page<Item> actual = itemRepository.findAll(itemSpecification, PageRequest.of(0, 5));
        List<Item> items = Collections.singletonList(item);
        assertEquals(actual.getContent(), items);
    }

    private Item getItem() {
        return Item.builder().name("n1").status(Status.VALID).build();
    }


}