package com.craftsmanship.tfm.restapi.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.craftsmanship.tfm.restapi.kafka.model.Item;

public class ItemsPersistenceStub implements ItemsPersistence {
    private static Map<Long, Item> items = new HashMap<Long, Item>();
    private static Long currentIndex = 1L;

    @Override
    public Item create(Item item) {
        item.setId(currentIndex);
        items.put(currentIndex, item);
        currentIndex++;
        return item;
    }

    @Override
    public List<Item> list() {
        return new ArrayList<Item>(items.values());
    }

    @Override
    public Item get(Long id) {
        return items.get(id);
    }

    @Override
    public Item update(Long id, Item item) {
        items.put(id, item);
        return item;
    }

    @Override
    public Item delete(Long id) {
        return items.remove(id);
    }
}