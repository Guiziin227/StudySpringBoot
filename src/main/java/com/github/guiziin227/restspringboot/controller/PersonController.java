package com.github.guiziin227.restspringboot.controller;

import com.github.guiziin227.restspringboot.dto.PersonDTO;
import com.github.guiziin227.restspringboot.file.exporter.MediaTypes;
import com.github.guiziin227.restspringboot.service.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/person/v1")
@Tag(name = "People", description = "Endpoints for managing persons")
public class PersonController implements com.github.guiziin227.restspringboot.controller.docs.PersonControllerDocs {

    @Autowired
    private PersonService personService;

    //@CrossOrigin(origins = {"http://localhost:8080"})
    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public PersonDTO create(@RequestBody PersonDTO person) {
        return personService.create(person);
    }

    @PostMapping(
            value = "/massCreation",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public List<PersonDTO> massCreation(
            @RequestParam("file") MultipartFile file
    ) {
        return personService.massCreation(file);
    }


    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //@CrossOrigin(origins = {"http://localhost:8080"})
    @GetMapping(value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            })
    @Override
    public PersonDTO findById(@PathVariable("id") Long id) {
        //person.setBirthDate(new Date()); // apenas para simular uma data, pois o model não possui data de nascimento
        //person.setPhoneNumber("55991654341"); // apenas para simular um telefone, pois o model não possui telefone
        // person.setPhoneNumber("");
        //person.setLastName(null);
        //person.setSensitiveData("I am sensitive data, do not show me!");
        return personService.findById(id);
    }


    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            })
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        Sort sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.by(Sort.Direction.DESC, "firstName") : Sort.by(Sort.Direction.ASC, "firstName");

        Pageable pageable = PageRequest.of(page, size, sortDirection);
        return ResponseEntity.ok(personService.findAll(pageable));
    }

    @GetMapping(
            value = "/exportPage",
            produces = {
                    MediaTypes.APPLICATION_CSV,
                    MediaTypes.APPLICATION_XLSX,
                    MediaTypes.APPLICATION_PDF,
            })
    @Override
    public ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestHeader(value = HttpHeaders.ACCEPT) String acceptHeader
    ) {

        Sort sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.by(Sort.Direction.DESC, "firstName")
                : Sort.by(Sort.Direction.ASC, "firstName");
        Pageable pageable = PageRequest.of(page, size, sortDirection);

        // O acceptHeader já foi validado pelo Spring. Passe-o para o serviço.
        Resource resource = personService.exportPage(pageable, acceptHeader);

        String fileName = "people_export" + getFileExtension(acceptHeader);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(acceptHeader))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);

    }


    @GetMapping(value = "/findByName/{firstName}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            })
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findByName(
            @PathVariable(value = "firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        Sort sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.by(Sort.Direction.DESC, "firstName") : Sort.by(Sort.Direction.ASC, "firstName");

        Pageable pageable = PageRequest.of(page, size, sortDirection);
        return ResponseEntity.ok(personService.findByName(firstName, pageable));
    }


    @PutMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            })
    @Override
    public PersonDTO update(@RequestBody PersonDTO person) {
        return personService.update(person);
    }

    @PatchMapping(value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            })
    @Override
    public PersonDTO disablePerson(@PathVariable("id") Long id) {
        return personService.disablePerson(id);
    }


    private String getFileExtension(String mediaType) {
        return switch (mediaType) {
            case MediaTypes.APPLICATION_CSV -> ".csv";
            case MediaTypes.APPLICATION_XLSX -> ".xlsx";
            case MediaTypes.APPLICATION_PDF -> ".pdf";
            default -> "";
        };
    }
}
