package es.mercadona.api_tiendas.service;

import java.util.List;

import es.mercadona.api_tiendas.dto.TrabajadorDto;
import es.mercadona.api_tiendas.dto.TrabajadorGridDto;
import es.mercadona.api_tiendas.entity.Trabajador;

public interface TrabajadorApiPort {

    List<TrabajadorGridDto> getTrabajadoresByTiendaId(String tiendaId);

    TrabajadorGridDto createTrabajador(TrabajadorDto trabajadorDto);

    TrabajadorGridDto updateTrabajador(Long idTrabajador, TrabajadorDto trabajadorDto);

    Long deleteTrabajador(Long idTrabajador);

    Trabajador getTrabajadorById(Long idTrabajador);
}
