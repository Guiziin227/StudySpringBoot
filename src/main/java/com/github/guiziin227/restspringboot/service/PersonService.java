package com.github.guiziin227.restspringboot.service;

import com.github.guiziin227.restspringboot.controller.PersonController;
import com.github.guiziin227.restspringboot.dto.mapper.custom.PersonMapper;
import com.github.guiziin227.restspringboot.dto.PersonDTO;
import static com.github.guiziin227.restspringboot.dto.mapper.ObjectMapper.parseObject;

import com.github.guiziin227.restspringboot.exception.BadRequestException;
import com.github.guiziin227.restspringboot.exception.FileStorageException;
import com.github.guiziin227.restspringboot.exception.RequiredObjectIsNullException;
import com.github.guiziin227.restspringboot.exception.ResourceNotFoundException;
import com.github.guiziin227.restspringboot.file.exporter.MediaTypes;
import com.github.guiziin227.restspringboot.file.exporter.contract.FileExporter;
import com.github.guiziin227.restspringboot.file.exporter.factory.FileExporterFactory;
import com.github.guiziin227.restspringboot.file.importer.contract.FileImporter;
import com.github.guiziin227.restspringboot.file.importer.factory.FileImporterFactory;
import com.github.guiziin227.restspringboot.model.Person;
import com.github.guiziin227.restspringboot.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    private final FileImporterFactory fileImporter;

    private final FileExporterFactory fileExporter;

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;


    @Transactional
    public PersonDTO create(PersonDTO person) {
        if (person == null) throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        logger.info("Creating one person!");
        var entity = parseObject(person, Person.class);
        logger.info("Convertendo entity para DTO: " + entity);
        var dto = parseObject(personRepository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PersonDTO update(PersonDTO person) {
        if (person == null) throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        logger.info("Updating one person!");
        Person p =  personRepository.findById(person.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Person not found!")
        );

        p.setAddress(person.getAddress());
        p.setFirstName(person.getFirstName());
        p.setLastName(person.getLastName());
        p.setGender(person.getGender());

        PersonDTO dto = parseObject(personRepository.save(p), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        log.info("Disabling person with id: " + id);
        personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found!"));
        personRepository.disablePerson(id);

        Person entity = personRepository.findById(id).get();
        PersonDTO dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        logger.info("Person disabled successfully!");
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting one person!");
        Person entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found!"));
        personRepository.delete(entity);
    }

    @Transactional
    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("findAll people!");

        var people = personRepository.findAll(pageable);

        return buildPagedModel(pageable, people);

    }

    @Transactional
    public Resource exportPage(Pageable pageable, String acceptHeader) {
        logger.info("Export a people page!");

        var people = personRepository.findAll(pageable).map(
                person -> parseObject(person, PersonDTO.class)
                ).getContent();

        try{
            FileExporter exporter = this.fileExporter.getFileExporter(acceptHeader);

            return exporter.exportFile(people);
        } catch (Exception e) {
            logger.error("Error exporting file: " + e.getMessage(), e);
            throw new FileStorageException("Error exporting file: " + e.getMessage());
        }
    }

    @Transactional
    public List<PersonDTO> massCreation(MultipartFile file) {
        logger.info("Importing people from file!");

        if (file.isEmpty()){
            throw new BadRequestException("It is not allowed to persist a null object!");
        }

        try(InputStream inputStream = file.getInputStream()){
            String fileName = Optional.ofNullable(file.getOriginalFilename()).orElseThrow(
                    () -> new BadRequestException("File name is null or empty")
            );

            FileImporter importer = this.fileImporter.getFileImporter(fileName);

            List<Person> entities = importer.importFile(inputStream).stream()
                    .map(dto -> personRepository.save(parseObject(dto, Person.class)))
                    .toList();

            return entities.stream().map(
                    person ->{
                        PersonDTO dto = parseObject(person, PersonDTO.class);
                        addHateoasLinks(dto);
                        return dto;
                    }
            ).toList();

        }catch (Exception e) {
            logger.error("Error processing file: " + e.getMessage(), e);
            throw new FileStorageException("Error processing file: " + e.getMessage());
        }
    }

    @Transactional
    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable) {
        logger.info("findPeopleByName!");

        var people = personRepository.findPeopleByName(firstName,pageable);

        return buildPagedModel(pageable, people);

    }

    @Transactional
    public PersonDTO findById(Long id) {
        logger.info("Finding one person!");
        var entity = personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Person not found! aqui")
        );
        PersonDTO dto =  parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> people) {
        var peopleWithLinks = people.map(person -> {
            PersonDTO dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withRel("findAll");

        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    private static void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(0,10,"asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findByName("", 0, 10, "asc"))
                .withRel("findByName").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class)).slash("massCreation")
                .withRel("massCreation").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).exportPage(0, 10, "asc", MediaTypes.APPLICATION_XLSX))
                .withRel("exportPage").withType("GET"));
    }

}
