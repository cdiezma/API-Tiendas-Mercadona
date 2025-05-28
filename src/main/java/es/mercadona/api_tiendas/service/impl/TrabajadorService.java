package es.mercadona.api_tiendas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.mercadona.api_tiendas.dto.TrabajadorDto;
import es.mercadona.api_tiendas.dto.TrabajadorGridDto;
import es.mercadona.api_tiendas.entity.Tienda;
import es.mercadona.api_tiendas.entity.Trabajador;
import es.mercadona.api_tiendas.exception.ApiTiendasException;
import es.mercadona.api_tiendas.helper.TrabajadorHelper;
import es.mercadona.api_tiendas.persistence.TiendaPersistence;
import es.mercadona.api_tiendas.persistence.TrabajadorPersistence;
import es.mercadona.api_tiendas.service.TrabajadorApiPort;
import jakarta.transaction.Transactional;

@Service
public class TrabajadorService implements TrabajadorApiPort {

    @Autowired
    private TrabajadorHelper trabajadorHelper;

    @Autowired
    private TrabajadorPersistence trabajadorPersistence;

    @Autowired
    private TiendaPersistence tiendaPersistence;

    @Override
    public List<TrabajadorGridDto> getTrabajadoresByTiendaId(String codigoTienda) {
        if (codigoTienda == null) {
            throw new ApiTiendasException("Código tienda no válido: " + codigoTienda);
        }
        Long idTienda = tiendaPersistence.findIdTiendaByCodigo(codigoTienda);
        if (idTienda == null) {
            throw new ApiTiendasException("No se encontró la tienda con código: " + codigoTienda);
        }

        return trabajadorHelper.convertTrabajadoresToTrabajadoresDto(
                trabajadorPersistence.getTrabajadoresByTiendaId(idTienda));
    }

    @Override
    @Transactional
    public TrabajadorGridDto createTrabajador(TrabajadorDto trabajadorDto) {
        if (trabajadorDto == null) {
            throw new ApiTiendasException("Datos del trabajador no válidos");
        }

        Tienda tienda = tiendaPersistence.findByCodigo(trabajadorDto.getCodigoTienda());
        if (tienda == null) {
            throw new ApiTiendasException("No se encontró la tienda con código: " + trabajadorDto.getCodigoTienda());
        }

        Trabajador trabajadorToSave = trabajadorHelper.convertTrabajadorDtoToTrabajador(trabajadorDto);
        trabajadorToSave.setTienda(tienda);
        return trabajadorHelper.convertTrabajadorToTrabajadorDto(
                trabajadorPersistence.save(trabajadorToSave));
    }

    @Override
    @Transactional
    public TrabajadorGridDto updateTrabajador(Long idTrabajador, TrabajadorDto trabajadorDto) {
        if (trabajadorDto == null || trabajadorDto.getIdentificacion() == null) {
            throw new ApiTiendasException("Datos del trabajador no válidos");
        }

        Trabajador trabajador = trabajadorPersistence.findById(idTrabajador)
                .orElseThrow(() -> new ApiTiendasException("No se encontró el trabajador con ID: " + idTrabajador));

        Tienda tienda = tiendaPersistence.findByCodigo(trabajadorDto.getCodigoTienda());
        if (tienda == null) {
            throw new ApiTiendasException("No se encontró la tienda con código: " + trabajadorDto.getCodigoTienda());
        }

        Trabajador trabajadorToSave = trabajadorHelper.convertTrabajadorDtoToTrabajador(trabajadorDto);
        trabajadorToSave.setId(trabajador.getId());
        trabajadorToSave.setTienda(tienda);
        return trabajadorHelper.convertTrabajadorToTrabajadorDto(
                trabajadorPersistence.save(trabajadorToSave));
    }

    @Override
    @Transactional
    public Long deleteTrabajador(Long idTrabajador) {
        if (idTrabajador == null) {
            throw new ApiTiendasException("ID del trabajador no válido: " + idTrabajador);
        }

        Trabajador trabajador = trabajadorPersistence.findById(idTrabajador)
                .orElseThrow(() -> new ApiTiendasException("No se encontró el trabajador con ID: " + idTrabajador));

        trabajadorPersistence.delete(trabajador);
        return idTrabajador;
    }

    @Override
    public Trabajador getTrabajadorById(Long idTrabajador) {
        if (idTrabajador == null) {
            throw new ApiTiendasException("ID del trabajador no válido: " + idTrabajador);
        }

        return trabajadorPersistence.findById(idTrabajador)
                .orElseThrow(() -> new ApiTiendasException("No se encontró el trabajador con ID: " + idTrabajador));
    }
}
