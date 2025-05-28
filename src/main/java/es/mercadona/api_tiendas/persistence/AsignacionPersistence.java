package es.mercadona.api_tiendas.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.mercadona.api_tiendas.entity.Asignacion;

@Repository
public interface AsignacionPersistence extends JpaRepository<Asignacion, Long> {

    Asignacion findByTrabajadorIdAndSeccionId(Long idTrabajador, Long idSeccion);

    List<Asignacion> findByTrabajadorId(Long idTrabajador);
}
