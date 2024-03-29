package com.example.restwithspringbootandjava.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.restwithspringbootandjava.dto.v1.UploadFileResponseDTO;
import com.example.restwithspringbootandjava.services.FileStorageService;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Tag(name = "File Endpoint")
@RestController
@RequestMapping("api/file/v1")

public class FileController {
        private Logger logger = Logger.getLogger(FileController.class.getName());

        @Autowired
        private FileStorageService fileStorageService;

        @PostMapping("/uploadFile")
        public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file){

            var fileName = fileStorageService.storeFile(file);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/file/v1/downloadFile/")
            .path(fileName)
            .toUriString();
 
          return new UploadFileResponseDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        }

        @PostMapping("/uploadMultipleFiles")
        public List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files){
 
          return Arrays.asList(files)
          .stream()
          .map(file -> uploadFile(file))
          .collect((Collectors.toList()));

        }

        
        @GetMapping("/downloadFile/{fileName:.+}")
        public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){

            Resource resource = fileStorageService.loadFileAsResource(fileName);
            String contentType = "";

            try {
              contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (Exception e) {
              logger.info("Could not determine file type");
            }
            if(contentType.isBlank()){
              contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName =\"" + resource.getFilename() + "\"")
            .body(resource);
          
        }

}
