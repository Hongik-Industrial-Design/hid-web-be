package com.hid_web.be.domain.exhibit;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitArtistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String artistUUID;
    private String profileImageUrl;
    private String artistNameKo;
    private String artistNameEn;
    private String role;
    private String email;
    private String instagramUrl;
    private String behanceUrl;
    private String linkedinUrl;

    public ExhibitArtistEntity(Long id, String artistUUID, String uploadedUrl) {
        this.id = id;
        this.artistUUID = artistUUID;;
        this.profileImageUrl = uploadedUrl;
    }
}

