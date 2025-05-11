package com.duoc.peliculas.controller;

import com.duoc.peliculas.model.Pelicula;
import com.duoc.peliculas.repository.PeliculaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PeliculaControllerTest {

    @Mock
    private PeliculaRepository peliculaRepository;

    @InjectMocks
    private PeliculaController peliculaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerPeliculaPorId_PeliculaExistente_DebeRetornarPelicula() {
        // Arrange
        Long id = 1L;
        Pelicula pelicula = new Pelicula();
        pelicula.setId(id);
        pelicula.setTitulo("Inception");
        pelicula.setAnio(2010);

        when(peliculaRepository.findById(id)).thenReturn(Optional.of(pelicula));

        // Act
        ResponseEntity<EntityModel<Pelicula>> respuesta = peliculaController.obtenerPeliculaPorId(id);

        // Assert
        assertTrue(respuesta.getStatusCode().is2xxSuccessful());
        assertNotNull(respuesta.getBody());
        assertEquals(pelicula, respuesta.getBody().getContent());
        verify(peliculaRepository).findById(id);
    }

    @Test
    void crearPelicula_PeliculaValida_DebeGuardarYRetornarPelicula() {
        // Arrange
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo("Matrix");
        pelicula.setAnio(1999);
        pelicula.setDirector("Hnos. Wachowski");

        when(peliculaRepository.save(pelicula)).thenReturn(pelicula);

        // Act
        ResponseEntity<EntityModel<Pelicula>> respuesta = peliculaController.crearPelicula(pelicula);

        // Assert
        assertTrue(respuesta.getStatusCode().is2xxSuccessful());
        assertNotNull(respuesta.getBody());
        assertEquals(pelicula, respuesta.getBody().getContent());
        verify(peliculaRepository).save(pelicula);
    }

    @Test
    void eliminarPelicula_PeliculaExistente_DebeEliminarCorrectamente() {
        // Arrange
        Long id = 1L;
        Pelicula pelicula = new Pelicula();
        pelicula.setId(id);
        pelicula.setTitulo("Star Wars");
        pelicula.setAnio(1977);

        doNothing().when(peliculaRepository).deleteById(id);
        when(peliculaRepository.findById(id)).thenReturn(Optional.of(pelicula));

        // Act
        ResponseEntity<?> respuesta = peliculaController.eliminarPelicula(id);

        // Assert
        assertTrue(respuesta.getStatusCode().is2xxSuccessful());
        verify(peliculaRepository).deleteById(id);
    }

    @Test
    void eliminarPelicula_PeliculaNoExistente_DebeRetornarNotFound() {
        // Arrange
        Long id = 999L;

        when(peliculaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> respuesta = peliculaController.eliminarPelicula(id);

        // Assert
        assertTrue(respuesta.getStatusCode().is4xxClientError());
        verify(peliculaRepository, never()).deleteById(id);
    }
} 