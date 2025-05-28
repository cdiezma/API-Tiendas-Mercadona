package es.mercadona.api_tiendas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.mercadona.api_tiendas.dto.InformeTiendaDto;
import es.mercadona.api_tiendas.dto.InformeTiendaSeccionesDto;
import es.mercadona.api_tiendas.entity.Tienda;
import es.mercadona.api_tiendas.exception.ApiTiendasException;
import es.mercadona.api_tiendas.helper.InformeHelper;
import es.mercadona.api_tiendas.persistence.TiendaPersistence;
import es.mercadona.api_tiendas.service.TiendaApiPort;

@Service
public class TiendaService implements TiendaApiPort {

    @Autowired
    private TiendaPersistence tiendaPersistence;

    @Autowired
    private InformeHelper informeHelper;

    @Override
    public Tienda getTiendaByTrabajadorId(Long idTrabajador) {
        if (idTrabajador == null) {
            throw new ApiTiendasException("El ID del trabajador no puede ser nulo");
        }
        Tienda tienda = tiendaPersistence.findByTrabajadorId(idTrabajador);
        if (tienda == null) {
            throw new ApiTiendasException("No se encontró una tienda asociada al trabajador con ID: " + idTrabajador);
        }
        return tienda;
    }

    @Override
    public InformeTiendaDto getInformeEstadoTienda(String codigoTienda) {
        if (codigoTienda == null || codigoTienda.isEmpty()) {
            throw new ApiTiendasException("El código de la tienda no puede ser nulo o vacío");
        }
        Tienda tienda = tiendaPersistence.findByCodigoForInforme(codigoTienda);
        if (tienda == null) {
            throw new ApiTiendasException("No se encontró una tienda con el código: " + codigoTienda);
        }
        return informeHelper.convertTiendaIntoInformeTiendaDto(tienda);
    }

    @Override
    public InformeTiendaSeccionesDto getInformeSeccionesTienda(String codigoTienda) {
        if (codigoTienda == null || codigoTienda.isEmpty()) {
            throw new ApiTiendasException("El código de la tienda no puede ser nulo o vacío");
        }
        Tienda tienda = tiendaPersistence.findByCodigoForInforme(codigoTienda);
        if (tienda == null) {
            throw new ApiTiendasException("No se encontró una tienda con el código: " + codigoTienda);
        }
        return informeHelper.convertTiendaIntoInformeTiendaSeccionesDto(tienda);
    }
}
