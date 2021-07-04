package com.cepheid.cloud.skel.service;

import com.cepheid.cloud.skel.exception.ItemNotFoundException;
import com.cepheid.cloud.skel.model.Description;
import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.model.ItemDTO;
import com.cepheid.cloud.skel.model.ItemFilter;
import com.cepheid.cloud.skel.repository.ItemRepository;
import com.cepheid.cloud.skel.repository.ItemSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Page<Item> getAll(ItemFilter itemFilter, Pageable pageable) {
        return itemRepository.findAll(new ItemSpecification(itemFilter), pageable);
    }

    public Item getItem(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    }

    public Item add(Item item) {
        return itemRepository.save(item);
    }

    public Item update(long itemId, ItemDTO itemDTO) {
        return itemRepository.findById(itemId)
                .map(savedItem -> itemRepository.save(savedItem.update(itemDTO)))
                .orElseThrow(ItemNotFoundException::new);
    }

    public Item addDescription(long itemId, Description description) {
        return itemRepository.findById(itemId)
                .map(item -> itemRepository.save(item.addDescription(description)))
                .orElseThrow(ItemNotFoundException::new);
    }

    public Item updateDescription(long itemId, Description description, long descriptionId) {
        return itemRepository.findById(itemId)
                .map(item -> item.updateDescription(descriptionId, description))
                .orElseThrow(ItemNotFoundException::new);
    }

    public void delete(long itemId) {
        itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        itemRepository.deleteById(itemId);
    }

    public Item deleteDescription(long itemId, long descriptionId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        return itemRepository.save(item.deleteDescription(descriptionId));
    }
}
