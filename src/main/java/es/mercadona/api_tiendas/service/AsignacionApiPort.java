package es.mercadona.api_tiendas.service;

import es.mercadona.api_tiendas.dto.AsignacionDto;

public interface AsignacionApiPort {

    boolean asignarTrabajador(AsignacionDto asignacionDto);

    boolean desasignarTrabajador(AsignacionDto asignacionDto);
}
