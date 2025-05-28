package es.mercadona.api_tiendas.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.mercadona.api_tiendas.dto.AsignacionDto;
import es.mercadona.api_tiendas.entity.Asignacion;
import es.mercadona.api_tiendas.entity.Seccion;
import es.mercadona.api_tiendas.entity.Tienda;
import es.mercadona.api_tiendas.entity.Trabajador;
import es.mercadona.api_tiendas.exception.ApiTiendasException;
import es.mercadona.api_tiendas.helper.AsignacionHelper;
import es.mercadona.api_tiendas.persistence.AsignacionPersistence;
import es.mercadona.api_tiendas.service.TiendaApiPort;
import es.mercadona.api_tiendas.service.TrabajadorApiPort;

@ExtendWith(MockitoExtension.class)
public class AsignacionServiceTest {

    @Mock
    private AsignacionPersistence asignacionPersistence;

    @Mock
    private TiendaApiPort tiendaApiPort;

    @Mock
    private TrabajadorApiPort trabajadorApiPort;

    @Mock
    private AsignacionHelper asignacionHelper;

    @InjectMocks
    private AsignacionService asignacionService;

    private AsignacionDto asignacionDto;
    private Trabajador trabajador;
    private Tienda tienda;
    private Seccion seccion;
    private Asignacion asignacionExistente;
    private List<Asignacion> asignacionesTrabajador;

    @BeforeEach
    void setUp() {

        asignacionDto = new AsignacionDto();
        asignacionDto.setIdTrabajador(1L);
        asignacionDto.setIdSeccion(10L);
        asignacionDto.setHoras(4);

        trabajador = new Trabajador();
        trabajador.setId(1L);
        trabajador.setNombre("Juan");
        trabajador.setApellidos("López");
        trabajador.setHorasTotales(8);

        seccion = new Seccion();
        seccion.setId(10L);
        seccion.setNombre("Cajas");
        seccion.setHorasNecesarias(16);

        tienda = new Tienda();
        tienda.setId(100L);
        tienda.setCodigo("T001");
        tienda.setNombre("Tienda Central");
        tienda.setSecciones(Arrays.asList(seccion));

        asignacionExistente = new Asignacion();
        asignacionExistente.setId(50L);
        asignacionExistente.setTrabajador(trabajador);
        asignacionExistente.setSeccion(seccion);
        asignacionExistente.setHorasAsignadas(2);

        asignacionesTrabajador = new ArrayList<>();
        asignacionesTrabajador.add(asignacionExistente);
    }

    @Test
    void asignarTrabajador_PrimeraAsignacion_DebeCrearNuevaAsignacion() {

        when(trabajadorApiPort.getTrabajadorById(asignacionDto.getIdTrabajador())).thenReturn(trabajador);
        when(tiendaApiPort.getTiendaByTrabajadorId(asignacionDto.getIdTrabajador())).thenReturn(tienda);
        when(asignacionPersistence.findByTrabajadorIdAndSeccionId(
                asignacionDto.getIdTrabajador(), asignacionDto.getIdSeccion()))
                .thenReturn(null);

        when(asignacionPersistence.findByTrabajadorId(asignacionDto.getIdTrabajador()))
                .thenReturn(new ArrayList<>());

        Asignacion nuevaAsignacion = new Asignacion();
        nuevaAsignacion.setId(100L);
        when(asignacionPersistence.save(any(Asignacion.class))).thenReturn(nuevaAsignacion);

        boolean resultado = asignacionService.asignarTrabajador(asignacionDto);

        assertTrue(resultado);
        verify(asignacionHelper).validateAsignacion(asignacionDto);
        verify(trabajadorApiPort).getTrabajadorById(asignacionDto.getIdTrabajador());
        verify(tiendaApiPort).getTiendaByTrabajadorId(asignacionDto.getIdTrabajador());
        verify(asignacionPersistence).findByTrabajadorIdAndSeccionId(
                asignacionDto.getIdTrabajador(), asignacionDto.getIdSeccion());
        verify(asignacionPersistence).findByTrabajadorId(asignacionDto.getIdTrabajador());
        verify(asignacionPersistence).save(any(Asignacion.class));
    }

    @Test
    void asignarTrabajador_AsignacionExistente_DebeActualizarHoras() {

        when(trabajadorApiPort.getTrabajadorById(asignacionDto.getIdTrabajador())).thenReturn(trabajador);
        when(tiendaApiPort.getTiendaByTrabajadorId(asignacionDto.getIdTrabajador())).thenReturn(tienda);
        when(asignacionPersistence.findByTrabajadorIdAndSeccionId(
                asignacionDto.getIdTrabajador(), asignacionDto.getIdSeccion()))
                .thenReturn(asignacionExistente);

        when(asignacionPersistence.findByTrabajadorId(asignacionDto.getIdTrabajador()))
                .thenReturn(asignacionesTrabajador);

        when(asignacionPersistence.save(any(Asignacion.class))).thenReturn(asignacionExistente);

        boolean resultado = asignacionService.asignarTrabajador(asignacionDto);

        assertTrue(resultado);
        assertEquals(6, asignacionExistente.getHorasAsignadas());
        verify(asignacionPersistence).save(asignacionExistente);
    }

    @Test
    void asignarTrabajador_SuperaHorasMaximas_DebeLanzarExcepcion() {

        when(trabajadorApiPort.getTrabajadorById(asignacionDto.getIdTrabajador())).thenReturn(trabajador);
        when(tiendaApiPort.getTiendaByTrabajadorId(asignacionDto.getIdTrabajador())).thenReturn(tienda);

        Asignacion asignacionExistente1 = new Asignacion();
        asignacionExistente1.setHorasAsignadas(4);

        Asignacion asignacionExistente2 = new Asignacion();
        asignacionExistente2.setHorasAsignadas(2);

        List<Asignacion> asignacionesExistentes = Arrays.asList(asignacionExistente1, asignacionExistente2);
        when(asignacionPersistence.findByTrabajadorId(asignacionDto.getIdTrabajador()))
                .thenReturn(asignacionesExistentes);

        asignacionDto.setHoras(4);

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            asignacionService.asignarTrabajador(asignacionDto);
        });

        assertTrue(exception.getMessage().contains("La suma de horas asignadas supera las horas máximas"));
        verify(asignacionPersistence, never()).save(any(Asignacion.class));
    }

    @Test
    void asignarTrabajador_SeccionNoEncontrada_DebeLanzarExcepcion() {

        when(trabajadorApiPort.getTrabajadorById(asignacionDto.getIdTrabajador())).thenReturn(trabajador);
        when(tiendaApiPort.getTiendaByTrabajadorId(asignacionDto.getIdTrabajador())).thenReturn(tienda);

        asignacionDto.setIdSeccion(999L);

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            asignacionService.asignarTrabajador(asignacionDto);
        });

        assertEquals("Sección no encontrada en la tienda", exception.getMessage());
        verify(asignacionPersistence, never()).save(any(Asignacion.class));
    }

    @Test
    void desasignarTrabajador_DesasignacionCompleta_DebeEliminarAsignacion() {

        asignacionDto.setHoras(2);
        when(asignacionPersistence.findByTrabajadorIdAndSeccionId(
                asignacionDto.getIdTrabajador(), asignacionDto.getIdSeccion()))
                .thenReturn(asignacionExistente);

        boolean resultado = asignacionService.desasignarTrabajador(asignacionDto);

        assertTrue(resultado);
        verify(asignacionHelper).validateAsignacion(asignacionDto);
        verify(trabajadorApiPort).getTrabajadorById(asignacionDto.getIdTrabajador());
        verify(tiendaApiPort).getTiendaByTrabajadorId(asignacionDto.getIdTrabajador());
        verify(asignacionPersistence).delete(asignacionExistente);
        verify(asignacionPersistence, never()).save(any(Asignacion.class));
    }

    @Test
    void desasignarTrabajador_DesasignacionParcial_DebeActualizarHoras() {

        asignacionDto.setHoras(1);
        when(asignacionPersistence.findByTrabajadorIdAndSeccionId(
                asignacionDto.getIdTrabajador(), asignacionDto.getIdSeccion()))
                .thenReturn(asignacionExistente);

        boolean resultado = asignacionService.desasignarTrabajador(asignacionDto);

        assertTrue(resultado);
        assertEquals(1, asignacionExistente.getHorasAsignadas());
        verify(asignacionPersistence).save(asignacionExistente);
        verify(asignacionPersistence, never()).delete(any(Asignacion.class));
    }

    @Test
    void desasignarTrabajador_AsignacionNoExiste_DebeLanzarExcepcion() {

        when(asignacionPersistence.findByTrabajadorIdAndSeccionId(
                asignacionDto.getIdTrabajador(), asignacionDto.getIdSeccion()))
                .thenReturn(null);

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            asignacionService.desasignarTrabajador(asignacionDto);
        });

        assertEquals("No existe una asignación para el trabajador en esta sección", exception.getMessage());
        verify(asignacionPersistence, never()).save(any(Asignacion.class));
        verify(asignacionPersistence, never()).delete(any(Asignacion.class));
    }

    @Test
    void desasignarTrabajador_HorasExcesivas_DebeLanzarExcepcion() {

        asignacionDto.setHoras(3);
        when(asignacionPersistence.findByTrabajadorIdAndSeccionId(
                asignacionDto.getIdTrabajador(), asignacionDto.getIdSeccion()))
                .thenReturn(asignacionExistente);

        ApiTiendasException exception = assertThrows(ApiTiendasException.class, () -> {
            asignacionService.desasignarTrabajador(asignacionDto);
        });

        assertEquals("No se puede desasignar más horas de las asignadas", exception.getMessage());
        verify(asignacionPersistence, never()).save(any(Asignacion.class));
        verify(asignacionPersistence, never()).delete(any(Asignacion.class));
    }
}
