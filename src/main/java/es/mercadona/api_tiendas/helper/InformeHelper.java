package es.mercadona.api_tiendas.helper;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.mercadona.api_tiendas.dto.AsignacionGridDto;
import es.mercadona.api_tiendas.dto.InformeTiendaDto;
import es.mercadona.api_tiendas.dto.InformeTiendaSeccionesDto;
import es.mercadona.api_tiendas.dto.SeccionGridDto;
import es.mercadona.api_tiendas.dto.SeccionHorasLibresDto;
import es.mercadona.api_tiendas.entity.Asignacion;
import es.mercadona.api_tiendas.entity.Seccion;
import es.mercadona.api_tiendas.entity.Tienda;
import es.mercadona.api_tiendas.service.ExternalStoresApiPort;

@Component
public class InformeHelper {

    @Autowired
    private ExternalStoresApiPort externalStoresApiPort;

    public InformeTiendaDto convertTiendaIntoInformeTiendaDto(Tienda tienda) {
        if (tienda == null) {
            return null;
        }
        InformeTiendaDto informeTiendaDto = new InformeTiendaDto();
        informeTiendaDto.setNombre(tienda.getNombre());
        System.out.println(tienda.getId().toString());
        informeTiendaDto.setDireccion(externalStoresApiPort.getDireccionTiendaById(tienda.getId().toString()));
        informeTiendaDto.setSecciones(new ArrayList<>());
        for (Seccion seccion : tienda.getSecciones()) {
            SeccionGridDto seccionGridDto = new SeccionGridDto();
            seccionGridDto.setTrabajadores(new ArrayList<>());
            seccionGridDto.setNombre(seccion.getNombre());
            for (Asignacion asignacion : seccion.getAsignaciones()) {
                AsignacionGridDto asignacionGridDto = new AsignacionGridDto();
                asignacionGridDto.setNombre(asignacion.getTrabajador().getNombre());
                asignacionGridDto.setApellidos(asignacion.getTrabajador().getApellidos());
                asignacionGridDto.setIdentificacion(asignacion.getTrabajador().getIdentificacion());
                asignacionGridDto.setHorasAsignadas(asignacion.getHorasAsignadas());
                seccionGridDto.getTrabajadores().add(asignacionGridDto);
            }
            informeTiendaDto.getSecciones().add(seccionGridDto);
        }

        return informeTiendaDto;
    }

    public InformeTiendaSeccionesDto convertTiendaIntoInformeTiendaSeccionesDto(Tienda tienda) {
        if (tienda == null) {
            return null;
        }
        InformeTiendaSeccionesDto informeTiendaSeccionesDto = new InformeTiendaSeccionesDto();
        informeTiendaSeccionesDto.setNombre(tienda.getNombre());
        informeTiendaSeccionesDto.setDireccion(externalStoresApiPort.getDireccionTiendaById(tienda.getId().toString()));
        informeTiendaSeccionesDto.setSecciones(new ArrayList<>());
        for (Seccion seccion : tienda.getSecciones().stream()
                .filter(Seccion::hasHorasDisponibles)
                .toList()) {
            SeccionHorasLibresDto seccionGridDto = new SeccionHorasLibresDto();
            seccionGridDto.setNombre(seccion.getNombre());
            seccionGridDto.setHorasDisponibles(seccion.getHorasDisponibles());
            seccionGridDto.setHorasNecesarias(seccion.getHorasNecesarias());
            seccionGridDto.setHorasAsignadas(seccion.getHorasNecesarias() - seccion.getHorasDisponibles());
            informeTiendaSeccionesDto.getSecciones().add(seccionGridDto);
        }
        return informeTiendaSeccionesDto;
    }
}
