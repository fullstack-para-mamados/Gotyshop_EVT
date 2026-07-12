package com.example.GotyStore.repository;

import com.example.GotyStore.Model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findByClienteId(int clienteId);
    List<Venta> findByJuegoId(int juegoId);
}
