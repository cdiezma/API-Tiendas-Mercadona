package es.mercadona.api_tiendas.helper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.mercadona.api_tiendas.dto.TrabajadorDto;
import es.mercadona.api_tiendas.dto.TrabajadorGridDto;
import es.mercadona.api_tiendas.entity.Trabajador;
import es.mercadona.api_tiendas.exception.ApiTiendasException;

@Component
public class TrabajadorHelper {

    @Autowired
    private TiendaHelper tiendaHelper;

    public List<TrabajadorGridDto> convertTrabajadoresToTrabajadoresDto(List<Trabajador> trabajadores) {
        if (trabajadores == null || trabajadores.isEmpty()) {
            return Collections.emptyList();
        }

        return trabajadores.stream()
                .map(this::convertTrabajadorToTrabajadorDto)
                .collect(Collectors.toList());
    }

    public TrabajadorGridDto convertTrabajadorToTrabajadorDto(Trabajador trabajador) {
        if (trabajador == null) {
            return null;
        }

        TrabajadorGridDto trabajadorDto = new TrabajadorGridDto();
        trabajadorDto.setId(trabajador.getId());
        trabajadorDto.setNombre(trabajador.getNombre());
        trabajadorDto.setApellidos(trabajador.getApellidos());
        trabajadorDto.setIdentificacion(trabajador.getIdentificacion());
        trabajadorDto.setTienda(tiendaHelper.convertTiendaToTiendaDto(trabajador.getTienda()));
        return trabajadorDto;
    }

    public Trabajador convertTrabajadorDtoToTrabajador(TrabajadorDto trabajadorDto) {
        if (trabajadorDto == null) {
            return null;
        }
        validateTrabajadorDto(trabajadorDto);

        Trabajador trabajador = new Trabajador();
        trabajador.setNombre(trabajadorDto.getNombre());
        trabajador.setApellidos(trabajadorDto.getApellidos());
        trabajador.setIdentificacion(trabajadorDto.getIdentificacion());
        trabajador.setHorasTotales(trabajadorDto.getHorasTotales());
        return trabajador;
    }

    private void validateTrabajadorDto(TrabajadorDto trabajadorDto) {
        if (trabajadorDto.getNombre() == null || trabajadorDto.getNombre().isEmpty()) {
            throw new ApiTiendasException("El nombre del trabajador es obligatorio");
        }
        if (trabajadorDto.getApellidos() == null || trabajadorDto.getApellidos().isEmpty()) {
            throw new ApiTiendasException("Los apellidos del trabajador son obligatorios");
        }
        if (trabajadorDto.getIdentificacion() == null || trabajadorDto.getIdentificacion().isEmpty()) {
            throw new ApiTiendasException("La identificación del trabajador es obligatoria");
        }
        if (trabajadorDto.getHorasTotales() == null || trabajadorDto.getHorasTotales() <= 0) {
            throw new ApiTiendasException("Las horas totales del trabajador deben ser un número positivo");
        }
    }

}
