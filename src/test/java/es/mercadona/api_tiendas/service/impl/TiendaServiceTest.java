package es.mercadona.api_tiendas.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.mercadona.api_tiendas.dto.InformeTiendaDto;
import es.mercadona.api_tiendas.entity.Tienda;
import es.mercadona.api_tiendas.exception.ApiTiendasException;
import es.mercadona.api_tiendas.helper.InformeHelper;
import es.mercadona.api_tiendas.persistence.TiendaPersistence;

@ExtendWith(MockitoExtension.class)
public class TiendaServiceTest {

    @Mock
    private TiendaPersistence tiendaPersistence;

    @Mock
    private InformeHelper informeHelper;

    @InjectMocks
    private TiendaService tiendaService;

    private Tienda tiendaMock;
    private InformeTiendaDto informeMock;

    @BeforeEach
    void setUp() {

        tiendaMock = new Tienda();
        tiendaMock.setId(1L);
        tiendaMock.setCodigo("T001");
        tiendaMock.setNombre("Tienda Test");

        informeMock = new InformeTiendaDto();

    }

    @Test
    void getInformeEstadoTienda_ConCodigoCorrecto_DevuelveInforme() {

        String codigoTienda = "T001";
        when(tiendaPersistence.findByCodigoForInforme(codigoTienda)).thenReturn(tiendaMock);
        when(informeHelper.convertTiendaIntoInformeTiendaDto(tiendaMock)).thenReturn(informeMock);

        InformeTiendaDto resultado = tiendaService.getInformeEstadoTienda(codigoTienda);

        assertNotNull(resultado);
        assertEquals(informeMock, resultado);
        verify(tiendaPersistence, times(1)).findByCodigoForInforme(codigoTienda);
        verify(informeHelper, times(1)).convertTiendaIntoInformeTiendaDto(tiendaMock);
    }

    @Test
    void getInformeEstadoTienda_ConCodigoNulo_LanzaExcepcion() {

        String codigoTienda = null;

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            tiendaService.getInformeEstadoTienda(codigoTienda);
        });

        assertEquals("El código de la tienda no puede ser nulo o vacío", exception.getMessage());
        verify(tiendaPersistence, never()).findByCodigoForInforme(anyString());
    }

    @Test
    void getInformeEstadoTienda_ConCodigoVacio_LanzaExcepcion() {

        String codigoTienda = "";

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            tiendaService.getInformeEstadoTienda(codigoTienda);
        });

        assertEquals("El código de la tienda no puede ser nulo o vacío", exception.getMessage());
        verify(tiendaPersistence, never()).findByCodigoForInforme(anyString());
    }

    @Test
    void getInformeEstadoTienda_TiendaNoEncontrada_LanzaExcepcion() {

        String codigoTienda = "NOEXISTE";
        when(tiendaPersistence.findByCodigoForInforme(codigoTienda)).thenReturn(null);

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            tiendaService.getInformeEstadoTienda(codigoTienda);
        });

        assertEquals("No se encontró una tienda con el código: " + codigoTienda, exception.getMessage());
        verify(tiendaPersistence, times(1)).findByCodigoForInforme(codigoTienda);
        verify(informeHelper, never()).convertTiendaIntoInformeTiendaDto(any());
    }
}
