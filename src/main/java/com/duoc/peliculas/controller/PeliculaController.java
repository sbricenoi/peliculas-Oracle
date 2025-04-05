package com.duoc.peliculas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.duoc.peliculas.model.Pelicula;
import com.duoc.peliculas.repository.PeliculaRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaRepository peliculaRepository;

    // Endpoint para obtener información detallada de una película por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Pelicula> obtenerPeliculaPorId(@PathVariable Long id) {
        Optional<Pelicula> pelicula = peliculaRepository.findById(id);
        return pelicula.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para obtener todas las películas registradas
    @GetMapping
    public ResponseEntity<List<Pelicula>> obtenerTodasLasPeliculas() {
        List<Pelicula> peliculas = peliculaRepository.findAll();
        return ResponseEntity.ok(peliculas);
    }

    // Endpoint para crear una nueva película
    @PostMapping
    public ResponseEntity<Pelicula> crearPelicula(@RequestBody Pelicula pelicula) {
        Pelicula nuevaPelicula = peliculaRepository.save(pelicula);
        return ResponseEntity.ok(nuevaPelicula);
    }

    // Endpoint para actualizar una película existente
    @PutMapping("/{id}")
    public ResponseEntity<Pelicula> actualizarPelicula(@PathVariable Long id, @RequestBody Pelicula detallesPelicula) {
        return peliculaRepository.findById(id)
            .map(peliculaExistente -> {
                peliculaExistente.setTitulo(detallesPelicula.getTitulo());
                peliculaExistente.setAnio(detallesPelicula.getAnio());
                peliculaExistente.setDirector(detallesPelicula.getDirector());
                peliculaExistente.setGenero(detallesPelicula.getGenero());
                peliculaExistente.setSinopsis(detallesPelicula.getSinopsis());
                
                Pelicula peliculaActualizada = peliculaRepository.save(peliculaExistente);
                return ResponseEntity.ok(peliculaActualizada);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para eliminar una película
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPelicula(@PathVariable Long id) {
        return peliculaRepository.findById(id)
            .map(pelicula -> {
                peliculaRepository.delete(pelicula);
                return ResponseEntity.ok().<Void>build();
            })
            .orElse(ResponseEntity.notFound().build());
    }

} 