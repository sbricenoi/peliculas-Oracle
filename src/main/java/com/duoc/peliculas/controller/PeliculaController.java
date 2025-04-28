package com.duoc.peliculas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.duoc.peliculas.model.Pelicula;
import com.duoc.peliculas.repository.PeliculaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaRepository peliculaRepository;

    // Endpoint para obtener información detallada de una película por su ID con HATEOAS
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pelicula>> obtenerPeliculaPorId(@PathVariable Long id) {
        Optional<Pelicula> pelicula = peliculaRepository.findById(id);
        return pelicula.map(p -> {
            EntityModel<Pelicula> model = EntityModel.of(p);
            model.add(linkTo(methodOn(PeliculaController.class).obtenerPeliculaPorId(id)).withSelfRel());
            model.add(linkTo(methodOn(PeliculaController.class).obtenerTodasLasPeliculas()).withRel("todas-peliculas"));
            return ResponseEntity.ok(model);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para obtener todas las películas con HATEOAS
    @GetMapping
    public ResponseEntity<List<EntityModel<Pelicula>>> obtenerTodasLasPeliculas() {
        List<EntityModel<Pelicula>> peliculas = peliculaRepository.findAll().stream()
            .map(pelicula -> {
                EntityModel<Pelicula> model = EntityModel.of(pelicula);
                model.add(linkTo(methodOn(PeliculaController.class).obtenerPeliculaPorId(pelicula.getId())).withSelfRel());
                return model;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(peliculas);
    }

    // Endpoint para crear una nueva película con HATEOAS
    @PostMapping
    public ResponseEntity<EntityModel<Pelicula>> crearPelicula(@RequestBody Pelicula pelicula) {
        Pelicula nuevaPelicula = peliculaRepository.save(pelicula);
        EntityModel<Pelicula> model = EntityModel.of(nuevaPelicula);
        model.add(linkTo(methodOn(PeliculaController.class).obtenerPeliculaPorId(nuevaPelicula.getId())).withSelfRel());
        return ResponseEntity.ok(model);
    }

    // Endpoint para actualizar una película existente con HATEOAS
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Pelicula>> actualizarPelicula(@PathVariable Long id, @RequestBody Pelicula detallesPelicula) {
        return peliculaRepository.findById(id)
            .map(peliculaExistente -> {
                peliculaExistente.setTitulo(detallesPelicula.getTitulo());
                peliculaExistente.setAnio(detallesPelicula.getAnio());
                peliculaExistente.setDirector(detallesPelicula.getDirector());
                peliculaExistente.setGenero(detallesPelicula.getGenero());
                peliculaExistente.setSinopsis(detallesPelicula.getSinopsis());
                
                Pelicula peliculaActualizada = peliculaRepository.save(peliculaExistente);
                
                EntityModel<Pelicula> model = EntityModel.of(peliculaActualizada);
                model.add(linkTo(methodOn(PeliculaController.class).obtenerPeliculaPorId(id)).withSelfRel());
                model.add(linkTo(methodOn(PeliculaController.class).obtenerTodasLasPeliculas()).withRel("todas-peliculas"));
                
                return ResponseEntity.ok(model);
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