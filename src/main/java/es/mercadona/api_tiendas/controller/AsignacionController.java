package es.mercadona.api_tiendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.mercadona.api_tiendas.dto.AsignacionDto;
import es.mercadona.api_tiendas.service.AsignacionApiPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/asignaciones")
@Tag(name = "Asignaciones", description = "Operaciones relacionadas con la asignación y desasignación de trabajadores")
public class AsignacionController {

        @Autowired
        private AsignacionApiPort asignacionApiPort;

        @PutMapping("/asignar")
        @Operation(summary = "Asigna un trabajador", description = "Asigna un trabajador a una sección específica de una tienda.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Asignación realizada correctamente"),
                        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(examples = {
                                        @ExampleObject(value = "La suma de horas asignadas supera las horas máximas permitidas para el trabajador: ") })),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public ResponseEntity<Boolean> asignarTrabajador(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la asignación", required = true) @RequestBody AsignacionDto asignacionDto) {
                // Aquí se llamaría al servicio para realizar la asignación
                return ResponseEntity.ok(asignacionApiPort.asignarTrabajador(asignacionDto));
        }

        @PutMapping("/desasignar")
        @Operation(summary = "Desasigna un trabajador", description = "Desasigna un trabajador de una sección específica")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Desasignación realizada correctamente"),
                        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(examples = {
                                        @ExampleObject(value = "No existe una asignación para el trabajador en esta sección") })),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        public ResponseEntity<Boolean> desasignarTrabajador(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la desasignación", required = true) @RequestBody AsignacionDto asignacionDto) {
                // Aquí se llamaría al servicio para realizar la desasignación
                return ResponseEntity.ok(asignacionApiPort.desasignarTrabajador(asignacionDto));
        }

}
