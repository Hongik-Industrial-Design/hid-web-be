package com.hid_web.be.service;

import com.hid_web.be.domain.exhibit.ExhibitArtistEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExhibitArtistWriter {

    public List<ExhibitArtistEntity> createArtistList(List<String> artistNames) {
        List<ExhibitArtistEntity> exhibitArtistEntities = new ArrayList<>();

        for (String name : artistNames) {
            ExhibitArtistEntity artistEntity = new ExhibitArtistEntity();
            artistEntity.setName(name);
            exhibitArtistEntities.add(artistEntity);
        }

        return exhibitArtistEntities;
    }
}

