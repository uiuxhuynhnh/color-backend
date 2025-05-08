package com.example.colorbackend.repository;

import com.example.colorbackend.model.ColorScheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorSchemeRepository extends JpaRepository<ColorScheme, Long> {
    ColorScheme findTopByOrderByIdDesc();
}
