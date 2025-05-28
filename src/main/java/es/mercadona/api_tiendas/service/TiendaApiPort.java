package es.mercadona.api_tiendas.service;

import es.mercadona.api_tiendas.dto.InformeTiendaDto;
import es.mercadona.api_tiendas.dto.InformeTiendaSeccionesDto;
import es.mercadona.api_tiendas.entity.Tienda;

public interface TiendaApiPort {

    Tienda getTiendaByTrabajadorId(Long idTrabajador);

    InformeTiendaDto getInformeEstadoTienda(String codigoTienda);

    InformeTiendaSeccionesDto getInformeSeccionesTienda(String codigoTienda);
}
