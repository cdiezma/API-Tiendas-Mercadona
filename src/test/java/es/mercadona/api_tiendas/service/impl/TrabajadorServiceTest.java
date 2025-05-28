package es.mercadona.api_tiendas.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.mercadona.api_tiendas.dto.TrabajadorDto;
import es.mercadona.api_tiendas.dto.TrabajadorGridDto;
import es.mercadona.api_tiendas.entity.Tienda;
import es.mercadona.api_tiendas.entity.Trabajador;
import es.mercadona.api_tiendas.exception.ApiTiendasException;
import es.mercadona.api_tiendas.helper.TrabajadorHelper;
import es.mercadona.api_tiendas.persistence.TiendaPersistence;
import es.mercadona.api_tiendas.persistence.TrabajadorPersistence;

@ExtendWith(MockitoExtension.class)
public class TrabajadorServiceTest {

    @Mock
    private TrabajadorPersistence trabajadorPersistence;

    @Mock
    private TiendaPersistence tiendaPersistence;

    @Mock
    private TrabajadorHelper trabajadorHelper;

    @InjectMocks
    private TrabajadorService trabajadorService;

    private Tienda tienda;
    private Trabajador trabajador;
    private TrabajadorDto trabajadorDto;
    private TrabajadorGridDto trabajadorGridDto;
    private List<Trabajador> trabajadores;
    private List<TrabajadorGridDto> trabajadoresGridDto;

    @BeforeEach
    void setUp() {

        tienda = new Tienda();
        tienda.setId(1L);
        tienda.setCodigo("T001");
        tienda.setNombre("Tienda Test");

        trabajador = new Trabajador();
        trabajador.setId(1L);
        trabajador.setNombre("Juan");
        trabajador.setApellidos("Pérez");
        trabajador.setIdentificacion("12345678A");
        trabajador.setHorasTotales(8);
        trabajador.setTienda(tienda);

        trabajadorDto = new TrabajadorDto();
        trabajadorDto.setNombre("Juan");
        trabajadorDto.setApellidos("Pérez");
        trabajadorDto.setIdentificacion("12345678A");
        trabajadorDto.setHorasTotales(8);
        trabajadorDto.setCodigoTienda("T001");

        trabajadorGridDto = new TrabajadorGridDto();
        trabajadorGridDto.setId(1L);
        trabajadorGridDto.setNombre("Juan");
        trabajadorGridDto.setApellidos("Pérez");
        trabajadorGridDto.setIdentificacion("12345678A");
        trabajadorGridDto.setHorasTotales(8);

        trabajadores = Arrays.asList(trabajador);
        trabajadoresGridDto = Arrays.asList(trabajadorGridDto);
    }

    @Test
    void getTrabajadoresByTiendaId_ConCodigoCorrecto_DevuelveTrabajadores() {

        String codigoTienda = "T001";
        Long idTienda = 1L;

        when(tiendaPersistence.findIdTiendaByCodigo(codigoTienda)).thenReturn(idTienda);
        when(trabajadorPersistence.getTrabajadoresByTiendaId(idTienda)).thenReturn(trabajadores);
        when(trabajadorHelper.convertTrabajadoresToTrabajadoresDto(trabajadores)).thenReturn(trabajadoresGridDto);

        List<TrabajadorGridDto> resultado = trabajadorService.getTrabajadoresByTiendaId(codigoTienda);

        assertNotNull(resultado);
        assertEquals(trabajadoresGridDto, resultado);
        verify(tiendaPersistence).findIdTiendaByCodigo(codigoTienda);
        verify(trabajadorPersistence).getTrabajadoresByTiendaId(idTienda);
        verify(trabajadorHelper).convertTrabajadoresToTrabajadoresDto(trabajadores);
    }

    @Test
    void getTrabajadoresByTiendaId_ConCodigoNulo_LanzaExcepcion() {

        String codigoTienda = null;

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            trabajadorService.getTrabajadoresByTiendaId(codigoTienda);
        });

        assertTrue(exception.getMessage().contains("Código tienda no válido"));
        verify(tiendaPersistence, never()).findIdTiendaByCodigo(anyString());
    }

    @Test
    void getTrabajadoresByTiendaId_TiendaNoEncontrada_LanzaExcepcion() {

        String codigoTienda = "NOEXISTE";
        when(tiendaPersistence.findIdTiendaByCodigo(codigoTienda)).thenReturn(null);

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            trabajadorService.getTrabajadoresByTiendaId(codigoTienda);
        });

        assertTrue(exception.getMessage().contains("No se encontró la tienda con código"));
        verify(tiendaPersistence).findIdTiendaByCodigo(codigoTienda);
        verify(trabajadorPersistence, never()).getTrabajadoresByTiendaId(anyLong());
    }

    @Test
    void createTrabajador_DatosCorrectos_CreaTrabajador() {

        when(tiendaPersistence.findByCodigo(trabajadorDto.getCodigoTienda())).thenReturn(tienda);
        when(trabajadorHelper.convertTrabajadorDtoToTrabajador(trabajadorDto)).thenReturn(trabajador);
        when(trabajadorPersistence.save(any(Trabajador.class))).thenReturn(trabajador);
        when(trabajadorHelper.convertTrabajadorToTrabajadorDto(trabajador)).thenReturn(trabajadorGridDto);

        TrabajadorGridDto resultado = trabajadorService.createTrabajador(trabajadorDto);

        assertNotNull(resultado);
        assertEquals(trabajadorGridDto, resultado);
        verify(tiendaPersistence).findByCodigo(trabajadorDto.getCodigoTienda());
        verify(trabajadorHelper).convertTrabajadorDtoToTrabajador(trabajadorDto);
        verify(trabajadorPersistence).save(any(Trabajador.class));
        verify(trabajadorHelper).convertTrabajadorToTrabajadorDto(trabajador);
    }

    @Test
    void createTrabajador_DatosNulos_LanzaExcepcion() {

        TrabajadorDto trabajadorDtoNulo = null;

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            trabajadorService.createTrabajador(trabajadorDtoNulo);
        });

        assertTrue(exception.getMessage().contains("Datos del trabajador no válidos"));
        verify(tiendaPersistence, never()).findByCodigo(anyString());
    }

    @Test
    void createTrabajador_TiendaNoEncontrada_LanzaExcepcion() {

        when(tiendaPersistence.findByCodigo(trabajadorDto.getCodigoTienda())).thenReturn(null);

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            trabajadorService.createTrabajador(trabajadorDto);
        });

        assertTrue(exception.getMessage().contains("No se encontró la tienda con código"));
        verify(tiendaPersistence).findByCodigo(trabajadorDto.getCodigoTienda());
        verify(trabajadorPersistence, never()).save(any(Trabajador.class));
    }

    @Test
    void updateTrabajador_DatosCorrectos_ActualizaTrabajador() {

        Long idTrabajador = 1L;
        when(trabajadorPersistence.findById(idTrabajador)).thenReturn(Optional.of(trabajador));
        when(tiendaPersistence.findByCodigo(trabajadorDto.getCodigoTienda())).thenReturn(tienda);
        when(trabajadorHelper.convertTrabajadorDtoToTrabajador(trabajadorDto)).thenReturn(trabajador);
        when(trabajadorPersistence.save(any(Trabajador.class))).thenReturn(trabajador);
        when(trabajadorHelper.convertTrabajadorToTrabajadorDto(trabajador)).thenReturn(trabajadorGridDto);

        TrabajadorGridDto resultado = trabajadorService.updateTrabajador(idTrabajador, trabajadorDto);

        assertNotNull(resultado);
        assertEquals(trabajadorGridDto, resultado);
        verify(trabajadorPersistence).findById(idTrabajador);
        verify(tiendaPersistence).findByCodigo(trabajadorDto.getCodigoTienda());
        verify(trabajadorHelper).convertTrabajadorDtoToTrabajador(trabajadorDto);
        verify(trabajadorPersistence).save(any(Trabajador.class));
        verify(trabajadorHelper).convertTrabajadorToTrabajadorDto(trabajador);
    }

    @Test
    void updateTrabajador_DatosNulos_LanzaExcepcion() {

        Long idTrabajador = 1L;
        TrabajadorDto trabajadorDtoNulo = null;

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            trabajadorService.updateTrabajador(idTrabajador, trabajadorDtoNulo);
        });

        assertTrue(exception.getMessage().contains("Datos del trabajador no válidos"));
        verify(trabajadorPersistence, never()).findById(anyLong());
    }

    @Test
    void updateTrabajador_TrabajadorNoEncontrado_LanzaExcepcion() {

        Long idTrabajadorInexistente = 999L;
        when(trabajadorPersistence.findById(idTrabajadorInexistente)).thenReturn(Optional.empty());

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            trabajadorService.updateTrabajador(idTrabajadorInexistente, trabajadorDto);
        });

        assertTrue(exception.getMessage().contains("No se encontró el trabajador con ID"));
        verify(trabajadorPersistence).findById(idTrabajadorInexistente);
        verify(tiendaPersistence, never()).findByCodigo(anyString());
    }

    @Test
    void updateTrabajador_TiendaNoEncontrada_LanzaExcepcion() {

        Long idTrabajador = 1L;
        when(trabajadorPersistence.findById(idTrabajador)).thenReturn(Optional.of(trabajador));
        when(tiendaPersistence.findByCodigo(trabajadorDto.getCodigoTienda())).thenReturn(null);

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            trabajadorService.updateTrabajador(idTrabajador, trabajadorDto);
        });

        assertTrue(exception.getMessage().contains("No se encontró la tienda con código"));
        verify(trabajadorPersistence).findById(idTrabajador);
        verify(tiendaPersistence).findByCodigo(trabajadorDto.getCodigoTienda());
        verify(trabajadorPersistence, never()).save(any(Trabajador.class));
    }

    @Test
    void deleteTrabajador_TrabajadorExistente_EliminaTrabajador() {

        Long idTrabajador = 1L;
        when(trabajadorPersistence.findById(idTrabajador)).thenReturn(Optional.of(trabajador));
        doNothing().when(trabajadorPersistence).delete(trabajador);

        Long resultado = trabajadorService.deleteTrabajador(idTrabajador);

        assertEquals(idTrabajador, resultado);
        verify(trabajadorPersistence).findById(idTrabajador);
        verify(trabajadorPersistence).delete(trabajador);
    }

    @Test
    void deleteTrabajador_IdNulo_LanzaExcepcion() {

        Long idTrabajadorNulo = null;

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            trabajadorService.deleteTrabajador(idTrabajadorNulo);
        });

        assertTrue(exception.getMessage().contains("ID del trabajador no válido"));
        verify(trabajadorPersistence, never()).findById(any());
        verify(trabajadorPersistence, never()).delete(any());
    }

    @Test
    void deleteTrabajador_TrabajadorNoEncontrado_LanzaExcepcion() {

        Long idTrabajadorInexistente = 999L;
        when(trabajadorPersistence.findById(idTrabajadorInexistente)).thenReturn(Optional.empty());

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            trabajadorService.deleteTrabajador(idTrabajadorInexistente);
        });

        assertTrue(exception.getMessage().contains("No se encontró el trabajador con ID"));
        verify(trabajadorPersistence).findById(idTrabajadorInexistente);
        verify(trabajadorPersistence, never()).delete(any());
    }

    @Test
    void getTrabajadorById_TrabajadorExistente_DevuelveTrabajador() {

        Long idTrabajador = 1L;
        when(trabajadorPersistence.findById(idTrabajador)).thenReturn(Optional.of(trabajador));

        Trabajador resultado = trabajadorService.getTrabajadorById(idTrabajador);

        assertNotNull(resultado);
        assertEquals(trabajador, resultado);
        verify(trabajadorPersistence).findById(idTrabajador);
    }

    @Test
    void getTrabajadorById_IdNulo_LanzaExcepcion() {

        Long idTrabajadorNulo = null;

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            trabajadorService.getTrabajadorById(idTrabajadorNulo);
        });

        assertTrue(exception.getMessage().contains("ID del trabajador no válido"));
        verify(trabajadorPersistence, never()).findById(any());
    }

    @Test
    void getTrabajadorById_TrabajadorNoEncontrado_LanzaExcepcion() {

        Long idTrabajadorInexistente = 999L;
        when(trabajadorPersistence.findById(idTrabajadorInexistente)).thenReturn(Optional.empty());

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            trabajadorService.getTrabajadorById(idTrabajadorInexistente);
        });

        assertTrue(exception.getMessage().contains("No se encontró el trabajador con ID"));
        verify(trabajadorPersistence).findById(idTrabajadorInexistente);
    }
}
