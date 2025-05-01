package com.hid_web.be.controller.professor.request;

import lombok.Data;

@Data
public class UpdateBiographyRequest {
    private String year;
    private String description;
}
