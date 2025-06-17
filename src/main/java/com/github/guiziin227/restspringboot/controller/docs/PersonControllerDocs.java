package com.github.guiziin227.restspringboot.controller.docs;

import com.github.guiziin227.restspringboot.dto.PersonDTO;
import com.github.guiziin227.restspringboot.file.exporter.MediaTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PersonControllerDocs {
    @Operation(
            summary = "Create a new person",
            tags = {"People"},
            description = "Creates a new person in the system",
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PersonDTO.class)
                            )),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonDTO create(@RequestBody PersonDTO person);

    @Operation(
            summary = "Delete person by ID",
            tags = {"People"},
            description = "Deletes a person by ID",
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<?> delete(@PathVariable("id") Long id);

    @Operation(
            summary = "Find person by ID",
            tags = {"People"},
            description = "Returns a person by ID",
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = PersonDTO.class)
                            )),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonDTO findById(@PathVariable("id") Long id);

    @Operation(
            summary = "Find all persons",
            tags = {"People"},
            description = "Returns a list of all persons in the system",
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    );

    @Operation(
            summary = "Export People persons",
            tags = {"People"},
            description = "Exports a page of persons in the system",
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaTypes.APPLICATION_XLSX
                                    ),
                                    @Content(
                                            mediaType = MediaTypes.APPLICATION_CSV
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            HttpServletRequest request
    );

    @Operation(
            summary = "Massive People creation",
            tags = {"People"},
            description = "Massive People creation with uploaded file",
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = PersonDTO.class)
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    List<PersonDTO> massCreation(
            MultipartFile file
    );



    @Operation(
            summary = "Find by first name",
            tags = {"People"},
            description = "Returns a list of persons by first name",
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "204",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findByName(
            @PathVariable(value = "firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    );

    @Operation(
            summary = "Update a person",
            tags = {"People"},
            description = "Updates an existing person in the system",
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PersonDTO.class)
                            )),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonDTO update(@RequestBody PersonDTO person);

    @Operation(
            summary = "Disable a person by ID",
            tags = {"People"},
            description = "Disable a person by ID",
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = PersonDTO.class)
                            )),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonDTO disablePerson(@PathVariable("id") Long id);
}
