package com.duoc.peliculas.repository;

import com.duoc.peliculas.model.Pelicula;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PeliculaRepositoryTest {

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findById_PeliculaExistente_DebeRetornarPelicula() {
        // Arrange
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo("El Padrino");
        pelicula.setAnio(1972);
        pelicula.setDirector("Francis Ford Coppola");
        
        entityManager.persist(pelicula);
        entityManager.flush();

        // Act
        Optional<Pelicula> peliculaEncontrada = peliculaRepository.findById(pelicula.getId());

        // Assert
        assertTrue(peliculaEncontrada.isPresent());
        assertEquals(pelicula.getTitulo(), peliculaEncontrada.get().getTitulo());
    }

    @Test
    void save_NuevaPelicula_DebeGuardarConId() {
        // Arrange
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo("Pulp Fiction");
        pelicula.setAnio(1994);
        pelicula.setDirector("Quentin Tarantino");

        // Act
        Pelicula peliculaGuardada = peliculaRepository.save(pelicula);

        // Assert
        assertNotNull(peliculaGuardada.getId());
        assertEquals("Pulp Fiction", peliculaGuardada.getTitulo());
    }
} 