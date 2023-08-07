package ru.rudikov.resourceservice.application.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceObject {

    private int id;
    private String value;
    private String path;
}
