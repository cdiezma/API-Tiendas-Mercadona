package es.mercadona.api_tiendas.helper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import es.mercadona.api_tiendas.dto.TiendaGridDto;
import es.mercadona.api_tiendas.entity.Tienda;

@Component
public class TiendaHelper {

    public List<TiendaGridDto> convertTiendaToTiendaDto(List<Tienda> tiendas) {
        if (tiendas == null || tiendas.isEmpty()) {
            return Collections.emptyList();
        }

        return tiendas.stream()
                .map(this::convertTiendaToTiendaDto)
                .collect(Collectors.toList());
    }

    public TiendaGridDto convertTiendaToTiendaDto(Tienda tienda) {
        if (tienda == null) {
            return null;
        }

        TiendaGridDto tiendaDto = new TiendaGridDto();
        tiendaDto.setCodigo(tienda.getCodigo());
        tiendaDto.setNombre(tienda.getNombre());

        return tiendaDto;
    }
}
