package com.duoc.peliculas.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "PELICULAS")
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pelicula_seq")
    @SequenceGenerator(name = "pelicula_seq", sequenceName = "PELICULA_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITULO", nullable = false)
    private String titulo;

    @Column(name = "ANIO")
    private Integer anio;

    @Column(name = "DIRECTOR")
    private String director;

    @Column(name = "GENERO")
    private String genero;

    @Column(name = "SINOPSIS", length = 1000)
    private String sinopsis;
} 