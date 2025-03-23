package com.hid_web.be.domain.exhibit;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ExhibitObjectKeyState {
    private final Set<String> currentObjectKeys;
    private final Set<String> updatedObjectKeys = new HashSet<>();

    public ExhibitObjectKeyState(Set<String> currentUrls) {
        this.currentObjectKeys = currentUrls;
    }

    public Set<String> extractDeletedObjectKeys() {
        Set<String> deletedUrls = new HashSet<>(currentObjectKeys);
        deletedUrls.removeAll(updatedObjectKeys);
        return deletedUrls;
    }
}
