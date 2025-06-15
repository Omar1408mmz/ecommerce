package com.artere.ecommerce.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

/**
 * Data Transfer Object for Category entity
 */
public record CategoryDTO(
    Long id,
    String name,
    String description,
    Long parentId,
    Set<Long> subcategoryIds,
    Set<Long> productIds
) {
    /**
     * Canonical constructor that ensures sets are never null
     */
    public CategoryDTO(Long id, String name, String description, Long parentId, 
                      Set<Long> subcategoryIds, Set<Long> productIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentId = parentId;
        this.subcategoryIds = subcategoryIds == null ? new HashSet<>() : new HashSet<>(subcategoryIds);
        this.productIds = productIds == null ? new HashSet<>() : new HashSet<>(productIds);
    }

    /**
     * Constructor with only basic fields
     */
    public CategoryDTO(Long id, String name, String description) {
        this(id, name, description, null, new HashSet<>(), new HashSet<>());
    }

    /**
     * Default constructor
     */
    public CategoryDTO() {
        this(null, null, null, null, new HashSet<>(), new HashSet<>());
    }

    /**
     * Override the accessors to ensure the returned sets are unmodifiable
     */
    @Override
    public Set<Long> subcategoryIds() {
        return Collections.unmodifiableSet(subcategoryIds);
    }

    @Override
    public Set<Long> productIds() {
        return Collections.unmodifiableSet(productIds);
    }
}
