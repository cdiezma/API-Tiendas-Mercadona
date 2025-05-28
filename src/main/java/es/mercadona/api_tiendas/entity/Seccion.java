package es.mercadona.api_tiendas.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Seccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "horas_necesarias", nullable = false)
    private int horasNecesarias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tienda_id")
    private Tienda tienda;

    @OneToMany(mappedBy = "seccion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Asignacion> asignaciones;

    public int getHorasDisponibles() {
        int horasAsignadas = asignaciones == null ? 0
                : asignaciones.stream()
                        .filter(asignacion -> asignacion != null)
                        .mapToInt(Asignacion::getHorasAsignadas)
                        .sum();
        return horasNecesarias - horasAsignadas;
    }

    public boolean hasHorasDisponibles() {
        return getHorasDisponibles() > 0;
    }
}
