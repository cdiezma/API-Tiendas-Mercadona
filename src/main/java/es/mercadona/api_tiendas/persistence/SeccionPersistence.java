package es.mercadona.api_tiendas.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.mercadona.api_tiendas.entity.Seccion;

@Repository
public interface SeccionPersistence extends JpaRepository<Seccion, Long> {

}
