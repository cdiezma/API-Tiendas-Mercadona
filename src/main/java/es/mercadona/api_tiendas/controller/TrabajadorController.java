package es.mercadona.api_tiendas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.mercadona.api_tiendas.dto.TrabajadorDto;
import es.mercadona.api_tiendas.dto.TrabajadorGridDto;
import es.mercadona.api_tiendas.service.TrabajadorApiPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Trabajadores", description = "Operaciones relacionadas con los trabajadores de tiendas")
@RestController
@RequestMapping("/trabajadores")
@Slf4j
public class TrabajadorController {

        @Autowired
        private TrabajadorApiPort trabajadorApiPort;

        @GetMapping("{codigoTienda}")
        @Operation(summary = "Listar trabajadores por tienda", description = "Obtiene la lista de trabajadores asociados a una tienda específica.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de trabajadores obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrabajadorGridDto.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(examples = {
                                        @ExampleObject(value = "Código de tienda no válido") })),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public ResponseEntity<List<TrabajadorGridDto>> listTrabajadoresTienda(
                        @Parameter(description = "Código de la tienda para filtrar los trabajadores", required = true, example = "SC001") @PathVariable String codigoTienda) {
                return ResponseEntity.ok(trabajadorApiPort.getTrabajadoresByTiendaId(codigoTienda));
        }

        @PostMapping
        @Operation(summary = "Crear trabajador", description = "Crea un nuevo trabajador en una tienda.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Trabajador creado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrabajadorGridDto.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(examples = {
                                        @ExampleObject(value = "Código de tienda no válido") })),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public ResponseEntity<TrabajadorGridDto> createTrabajador(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del trabajador a crear", required = true, content = @Content(schema = @Schema(implementation = TrabajadorDto.class))) @RequestBody TrabajadorDto trabajadorDto) {
                log.info("Creando trabajador: {}", trabajadorDto.getIdentificacion());
                return ResponseEntity.ok(trabajadorApiPort.createTrabajador(trabajadorDto));
        }

        @PutMapping("/{idTrabajador}")
        @Operation(summary = "Actualizar trabajador", description = "Actualiza la información de un trabajador existente.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Trabajador actualizado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TrabajadorGridDto.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(examples = {
                                        @ExampleObject(value = "Código de tienda no válido") })),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public ResponseEntity<TrabajadorGridDto> updateTrabajador(
                        @Parameter(description = "ID del trabajador a actualizar", required = true, example = "1") @PathVariable Long idTrabajador,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del trabajador", required = true, content = @Content(schema = @Schema(implementation = TrabajadorDto.class))) @RequestBody TrabajadorDto trabajadorDto) {
                log.info("Actualizando trabajador con ID: {}", idTrabajador);
                return ResponseEntity.ok(trabajadorApiPort.updateTrabajador(idTrabajador, trabajadorDto));
        }

        @DeleteMapping("/{idTrabajador}")
        @Operation(summary = "Eliminar trabajador", description = "Elimina un trabajador de la tienda.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Trabajador eliminado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(examples = {
                                        @ExampleObject(value = "Código de tienda no válido") })),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public ResponseEntity<Long> deleteTrabajador(
                        @Parameter(description = "ID del trabajador a eliminar", required = true, example = "1") @PathVariable Long idTrabajador) {
                log.info("Eliminando trabajador con ID: {}", idTrabajador);
                return ResponseEntity.ok(trabajadorApiPort.deleteTrabajador(idTrabajador));
        }
}
