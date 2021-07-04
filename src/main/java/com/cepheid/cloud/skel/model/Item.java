package com.cepheid.cloud.skel.model;

import com.cepheid.cloud.skel.exception.DescriptionNotFoundException;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class Item extends AbstractEntity {

    private String name;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Singular
    private List<Description> descriptions;
    private Status status;

    public Item update(ItemDTO itemDTO) {
        setName(itemDTO.getName());
        setStatus(itemDTO.getStatus());
        return this;
    }

    public Item addDescription(Description description) {
        if (descriptions == null) {
            descriptions = new ArrayList<>();
        }
        descriptions.add(description);
        return this;
    }

    public Item updateDescription(long descriptionId, Description description) {
        deleteDescription(descriptionId);
        description.setId(descriptionId);
        descriptions.add(description);
        return this;
    }

    public Item deleteDescription(long descriptionId) {
        checkDescriptionPresent(descriptionId);
        descriptions.removeIf(desc -> desc.getId() == descriptionId);
        return this;
    }

    private void checkDescriptionPresent(long descriptionId) {
        boolean isDescriptionPresent = descriptions.stream().anyMatch(desc -> desc.getId() == descriptionId);
        if (!isDescriptionPresent) {
            throw new DescriptionNotFoundException();
        }
    }
}
