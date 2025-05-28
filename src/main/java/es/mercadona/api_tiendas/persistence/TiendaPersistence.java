package es.mercadona.api_tiendas.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.mercadona.api_tiendas.entity.Tienda;

@Repository
public interface TiendaPersistence extends JpaRepository<Tienda, Long> {

    boolean existsByCodigo(String codigo);

    @Query("SELECT t.id FROM Tienda t WHERE t.codigo = ?1")
    Long findIdTiendaByCodigo(String codigo);

    Tienda findByCodigo(String codigo);

    @EntityGraph(attributePaths = { "secciones" })
    @Query("SELECT t FROM Tienda t LEFT JOIN t.trabajadores tr WHERE tr.id = ?1")
    Tienda findByTrabajadorId(Long idTrabajador);

    @Query("SELECT t FROM Tienda t WHERE t.codigo = ?1")
    Tienda findByCodigoForInforme(String codigo);

}
