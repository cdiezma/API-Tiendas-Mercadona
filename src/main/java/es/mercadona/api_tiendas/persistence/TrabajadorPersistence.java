package es.mercadona.api_tiendas.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.mercadona.api_tiendas.entity.Trabajador;

@Repository
public interface TrabajadorPersistence extends JpaRepository<Trabajador, Long> {

    List<Trabajador> getTrabajadoresByTiendaId(Long tiendaId);
}
