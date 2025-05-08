package com.example.colorbackend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorScheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String primary;
    private String secondary;
    private String alert;
    private String danger;
    private String success;
    private String info;
    private int upvotes;
    private int downvotes;
}
