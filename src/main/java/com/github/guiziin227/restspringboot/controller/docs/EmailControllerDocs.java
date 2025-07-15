package com.github.guiziin227.restspringboot.controller.docs;

import com.github.guiziin227.restspringboot.dto.PersonDTO;
import com.github.guiziin227.restspringboot.dto.request.EmailRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface EmailControllerDocs {

    @Operation(
            summary = "Send Email",
            tags = {"E-mail"},
            description = "This endpoint allows you to send an email.",
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<String> sendEmail(EmailRequestDTO emailRequestDTO);

    @Operation(
            summary = "Send Email with Attachment",
            tags = {"E-mail"},
            description = "This endpoint allows you to send an email with an attachment.",
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<String> sendEmail(String emailRequestJSON, MultipartFile multipartFile);
}
