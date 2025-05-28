package es.mercadona.api_tiendas.helper;

import org.springframework.stereotype.Component;

import es.mercadona.api_tiendas.dto.AsignacionDto;
import es.mercadona.api_tiendas.exception.ApiTiendasException;

@Component
public class AsignacionHelper {

    public void validateAsignacion(AsignacionDto asignacionDto) {
        if (asignacionDto.getIdTrabajador() == null) {
            throw new ApiTiendasException("El ID del trabajador no puede ser nulo");
        }
        if (asignacionDto.getIdSeccion() == null) {
            throw new ApiTiendasException("El ID de la sección no puede ser nulo");
        }
        if (asignacionDto.getHoras() == null || asignacionDto.getHoras() <= 0) {
            throw new ApiTiendasException("Las horas asignadas deben ser un número positivo");
        }
    }
}
