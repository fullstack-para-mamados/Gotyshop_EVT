package com.example.GotyStore.repository;

import com.example.GotyStore.Model.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Integer> {
    List<Juego> findByPlataforma(String plataforma);
    List<Juego> findByGenero(String genero);
}
