package com.hid_web.be.storage.exhibit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitSubImgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subImgId;

    private String subImgUrl;
    private int position;
}

