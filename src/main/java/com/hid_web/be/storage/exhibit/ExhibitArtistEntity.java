package com.hid_web.be.storage.exhibit;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitArtistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;

    private String artistUUID;
    private String profileImgUrl;
    private String nameKo;
    private String nameEn;
    private String role;
    private String email;
    private String instagramUrl;
    private String behanceUrl;
    private String linkedinUrl;

    public ExhibitArtistEntity(Long id, String artistUUID, String uploadedUrl) {
        this.artistId = id;
        this.artistUUID = artistUUID;;
        this.profileImgUrl = uploadedUrl;
    }
}

