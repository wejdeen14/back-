package com.example.restaurant_universitaire.DTO;

import java.util.List;

import lombok.Data;
@Data
public class categorieDTO {
    private Long id_for;
    private String nom_for;
    private List<String> categories;
}