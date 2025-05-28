package es.mercadona.api_tiendas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.mercadona.api_tiendas.dto.AsignacionDto;
import es.mercadona.api_tiendas.entity.Asignacion;
import es.mercadona.api_tiendas.entity.Seccion;
import es.mercadona.api_tiendas.entity.Tienda;
import es.mercadona.api_tiendas.entity.Trabajador;
import es.mercadona.api_tiendas.exception.ApiTiendasException;
import es.mercadona.api_tiendas.helper.AsignacionHelper;
import es.mercadona.api_tiendas.persistence.AsignacionPersistence;
import es.mercadona.api_tiendas.service.AsignacionApiPort;
import es.mercadona.api_tiendas.service.TiendaApiPort;
import es.mercadona.api_tiendas.service.TrabajadorApiPort;

@Service
public class AsignacionService implements AsignacionApiPort {

    @Autowired
    private AsignacionPersistence asignacionPersistence;

    @Autowired
    private TiendaApiPort tiendaApiPort;

    @Autowired
    private AsignacionHelper asignacionHelper;

    @Autowired
    private TrabajadorApiPort trabajadorApiPort;

    @Override
    @Transactional
    public boolean asignarTrabajador(AsignacionDto asignacionDto) {
        asignacionHelper.validateAsignacion(asignacionDto);
        Trabajador trabajador = trabajadorApiPort.getTrabajadorById(asignacionDto.getIdTrabajador());

        Tienda tienda = tiendaApiPort.getTiendaByTrabajadorId(asignacionDto.getIdTrabajador());
        Seccion seccion = tienda.getSecciones().stream()
                .filter(s -> s.getId().equals(asignacionDto.getIdSeccion()))
                .findFirst()
                .orElseThrow(() -> new ApiTiendasException("Sección no encontrada en la tienda"));

        Asignacion asignacionExistente = asignacionPersistence
                .findByTrabajadorIdAndSeccionId(asignacionDto.getIdTrabajador(), asignacionDto.getIdSeccion());

        List<Asignacion> asignaciones = asignacionPersistence.findByTrabajadorId(asignacionDto.getIdTrabajador());
        int totalHorasAsignadas = asignaciones.stream()
                .mapToInt(Asignacion::getHorasAsignadas)
                .sum();

        if (totalHorasAsignadas + asignacionDto.getHoras() > trabajador.getHorasTotales()) {
            throw new ApiTiendasException(
                    "La suma de horas asignadas supera las horas máximas permitidas para el trabajador: " +
                            trabajador.getHorasTotales());
        }
        if (seccion.getHorasDisponibles() < asignacionDto.getHoras()) {
            throw new ApiTiendasException("No hay suficientes horas disponibles en la sección para la asignación");
        }

        if (asignacionExistente == null) {
            Asignacion nuevaAsignacion = new Asignacion();
            nuevaAsignacion.setHorasAsignadas(asignacionDto.getHoras());
            nuevaAsignacion.setTrabajador(trabajador);
            nuevaAsignacion.setSeccion(seccion);
            return asignacionPersistence.save(nuevaAsignacion) != null;
        } else {
            asignacionExistente.setHorasAsignadas(asignacionExistente.getHorasAsignadas() + asignacionDto.getHoras());
            return asignacionPersistence.save(asignacionExistente) != null;
        }

    }

    @Override
    @Transactional
    public boolean desasignarTrabajador(AsignacionDto asignacionDto) {

        asignacionHelper.validateAsignacion(asignacionDto);
        trabajadorApiPort.getTrabajadorById(asignacionDto.getIdTrabajador());
        tiendaApiPort.getTiendaByTrabajadorId(asignacionDto.getIdTrabajador());

        Asignacion asignacion = asignacionPersistence
                .findByTrabajadorIdAndSeccionId(asignacionDto.getIdTrabajador(), asignacionDto.getIdSeccion());
        if (asignacion == null) {
            throw new ApiTiendasException("No existe una asignación para el trabajador en esta sección");
        }
        if (asignacion.getHorasAsignadas() < asignacionDto.getHoras()) {
            throw new ApiTiendasException("No se puede desasignar más horas de las asignadas");
        }
        if (asignacion.getHorasAsignadas() == asignacionDto.getHoras()) {
            asignacionPersistence.delete(asignacion);
        } else {
            asignacion.setHorasAsignadas(asignacion.getHorasAsignadas() - asignacionDto.getHoras());
            asignacionPersistence.save(asignacion);
        }
        return true;
    }
}
