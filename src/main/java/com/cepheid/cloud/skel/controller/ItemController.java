package com.cepheid.cloud.skel.controller;

import com.cepheid.cloud.skel.model.Description;
import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.model.ItemDTO;
import com.cepheid.cloud.skel.model.ItemFilter;
import com.cepheid.cloud.skel.service.ItemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@RestController()
@RequestMapping("/api/1.0/items")
@Api()
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping()
    public Page<Item> getItems(ItemFilter request, Pageable pageable) {
        return itemService.getAll(request, pageable);
    }

    @GetMapping("/{itemId}")
    public Item getItems(@PathVariable long itemId) {
        return itemService.getItem(itemId);
    }

    @PostMapping()
    public Item add(@RequestBody Item item) {
        return itemService.add(item);
    }

    @PostMapping("/{itemId}/description")
    public Item addDescription(@PathVariable long itemId, @RequestBody Description description) {
        return itemService.addDescription(itemId, description);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestBody ItemDTO itemDTO, @PathVariable long itemId) {
        return itemService.update(itemId, itemDTO);
    }

    @PutMapping("/{itemId}/description/{descriptionId}")
    public Item update(@RequestBody Description description, @PathVariable long itemId,
                       @PathVariable long descriptionId) {
        return itemService.updateDescription(itemId, description, descriptionId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable long itemId) {
        itemService.delete(itemId);
    }

    @DeleteMapping("/{itemId}/description/{descriptionId}")
    public Item deleteDescription(@PathVariable long itemId, @PathVariable long descriptionId) {
        return itemService.deleteDescription(itemId, descriptionId);
    }


}
