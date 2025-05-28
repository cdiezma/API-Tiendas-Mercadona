package es.mercadona.api_tiendas.dto.external;

import lombok.Data;

@Data
public class StoreDto {

    private String id;
    private String description;
    private String address;
    private String city;

    // Additional fields can be added as needed
}
