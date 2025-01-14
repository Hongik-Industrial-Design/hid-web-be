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
public class ExhibitDetailImgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailImgId;

    private String detailImgUrl;
    private int position;
}


