package com.hid_web.be.domain.exhibit;

import lombok.Data;
import lombok.ToString;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString
public class ExhibitUUIDState {
    private final Set<String> currentUUIDs;
    private final Set<String> updatedUUIDs = new HashSet<>();

    public ExhibitUUIDState(Set<String> currentUUIDs) {
        this.currentUUIDs = currentUUIDs;
    }

    public Set<String> extractDeletedUUIDs() {
        Set<String> deletedUUIDs = new HashSet<>(currentUUIDs);
        deletedUUIDs.removeAll(updatedUUIDs);
        return deletedUUIDs;
    }
}