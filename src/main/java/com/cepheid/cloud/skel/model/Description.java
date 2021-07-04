package com.cepheid.cloud.skel.model;

import lombok.*;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Description extends AbstractEntity {

    private String language;
    private String shortDescription;
    private String longDescription;

}
