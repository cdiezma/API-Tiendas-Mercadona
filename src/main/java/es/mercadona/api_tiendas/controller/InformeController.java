package es.mercadona.api_tiendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.mercadona.api_tiendas.dto.InformeTiendaDto;
import es.mercadona.api_tiendas.dto.InformeTiendaSeccionesDto;
import es.mercadona.api_tiendas.service.TiendaApiPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Informes", description = "Operaciones relacionadas con los informes de tiendas")
@RequestMapping("/informes")
@RestController
public class InformeController {

        @Autowired
        private TiendaApiPort tiendaApiPort;

        @Operation(summary = "Obtener informe de estado de tienda", description = "Devuelve el informe de estado de una tienda dado su código")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Informe generado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InformeTiendaDto.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(examples = {
                                        @ExampleObject(value = "Código de tienda no válido") })),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/estado-tienda/{codigoTienda}")
        public ResponseEntity<InformeTiendaDto> getInformeEstadoTienda(
                        @Parameter(description = "Código de la tienda", required = true, example = "SC001") @PathVariable String codigoTienda) {
                return ResponseEntity.ok(tiendaApiPort.getInformeEstadoTienda(codigoTienda));
        }

        @Operation(summary = "Obtener informe de secciones incompletas de tienda", description = "Devuelve el informe de secciones de una tienda dado su código")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Informe generado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InformeTiendaSeccionesDto.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(examples = {
                                        @ExampleObject(value = "Código de tienda no válido") })),
        })
        @GetMapping("/secciones-libres-tienda/{codigoTienda}")
        public ResponseEntity<InformeTiendaSeccionesDto> getInformeSeccionesTienda(
                        @Parameter(description = "Código de la tienda", required = true, example = "SC001") @PathVariable String codigoTienda) {
                return ResponseEntity.ok(tiendaApiPort.getInformeSeccionesTienda(codigoTienda));
        }
}
