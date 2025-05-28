package es.mercadona.api_tiendas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.mercadona.api_tiendas.dto.external.StoreDto;
import es.mercadona.api_tiendas.exception.ApiTiendasException;
import es.mercadona.api_tiendas.service.ExternalStoresApiPort;

@Service
public class ExternalStoresService implements ExternalStoresApiPort {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${external.stores-api.url}")
    private String EXTERNAL_STORES_API_URL;

    @Override
    public String getDireccionTiendaById(String tiendaId) {
        if (tiendaId == null || tiendaId.isEmpty()) {
            throw new ApiTiendasException("El ID de la tienda no puede ser nulo o vacío");
        }
        try {
            String url = EXTERNAL_STORES_API_URL + "/stores/" + tiendaId;
            StoreDto tienda = restTemplate.getForObject(url, StoreDto.class);
            if (tienda == null || tienda.getAddress() == null) {
                throw new ApiTiendasException("No se encontró la tienda con ID: " + tiendaId);
            }
            return tienda.getAddress();
        } catch (RestClientException e) {
            throw new ApiTiendasException("Error al obtener la tienda de la API externa con ID: " + tiendaId, e);
        }

    }

}
