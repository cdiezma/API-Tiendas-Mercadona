package es.mercadona.api_tiendas.exception;

public class ApiTiendasException extends RuntimeException {
    public ApiTiendasException(String mensaje) {
        super(mensaje);
    }

    public ApiTiendasException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
