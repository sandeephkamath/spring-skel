package com.cepheid.cloud.skel.model;

import com.cepheid.cloud.skel.exception.DescriptionNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemTest {

    @Test
    void update_shouldReturnItem_withUpdatedNameAndStatus() {
        ItemDTO itemDTO = new ItemDTO("n1", Status.INVALID);
        Item item = Item.builder().name("n0").status(Status.VALID).build();
        Item expectedItem = Item.builder().name(itemDTO.getName()).status(Status.INVALID).build();
        assertEquals(expectedItem, item.update(itemDTO));
    }

    @Test
    void addDescription_shouldReturn_withAddedDescription() {
        Item item = new Item();
        item.setName("n1");
        item.setId(123L);
        Description description = Description.builder().longDescription("ld").build();

        item.addDescription(description);

        assertEquals(1, item.getDescriptions().size());
    }

    @Test
    void updateDescription_shouldReturnItem_withUpdatedDescription() {
        Description description = Description.builder().longDescription("ld").build();
        description.setId(1L);
        List<Description> descriptions = new ArrayList<>();
        descriptions.add(description);
        Item item = Item.builder().name("n0").status(Status.VALID).build();
        item.setDescriptions(descriptions);
        Description newDescription = Description.builder().longDescription("ld1").build();

        item.updateDescription(description.getId(), newDescription);

        assertEquals(newDescription, item.getDescriptions().get(0));
    }

    @Test
    void updateDescription_shouldThrowDescriptionNotFound_whenDescriptionWithIdNotFound() {
        Description description = Description.builder().longDescription("ld").build();
        description.setId(1L);
        List<Description> descriptions = new ArrayList<>();
        descriptions.add(description);
        Item item = Item.builder().name("n0").status(Status.VALID).build();
        item.setDescriptions(descriptions);
        Description newDescription = Description.builder().longDescription("ld1").build();

        item.updateDescription(description.getId(), newDescription);

        assertThrows(DescriptionNotFoundException.class, () -> item.updateDescription(123L, newDescription));
    }

    @Test
    void deleteDescription_shouldReturnItem_withDeletedDescription() {
        Description description = Description.builder().longDescription("ld").build();
        description.setId(1L);
        List<Description> descriptions = new ArrayList<>();
        descriptions.add(description);
        Item item = Item.builder().name("n0").status(Status.VALID).build();
        item.setDescriptions(descriptions);

        item.deleteDescription(description.getId());

        assertEquals(0, item.getDescriptions().size());
    }

    @Test
    void deleteDescription_shouldThrowDescriptionNotFoundException_whenDescriptionWithIdNotFound() {
        Description description = Description.builder().longDescription("ld").build();
        description.setId(1L);
        List<Description> descriptions = new ArrayList<>();
        descriptions.add(description);
        Item item = Item.builder().name("n0").status(Status.VALID).build();
        item.setDescriptions(descriptions);


        assertThrows(DescriptionNotFoundException.class, () -> item.deleteDescription(123L));
    }
}