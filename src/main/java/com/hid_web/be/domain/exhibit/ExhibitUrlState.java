package com.hid_web.be.domain.exhibit;

import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
public class ExhibitUrlState {
    private final Set<String> currentUrls;
    private final Set<String> updatedUrls = new HashSet<>();

    public ExhibitUrlState(Set<String> currentUrls) {
        this.currentUrls = currentUrls;
    }

    public Set<String> extractDeletedUrls() {
        Set<String> deletedUrls = new HashSet<>(currentUrls);
        deletedUrls.removeAll(updatedUrls);
        return deletedUrls;
    }
}
