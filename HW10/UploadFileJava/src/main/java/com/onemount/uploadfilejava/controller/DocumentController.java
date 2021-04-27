package com.onemount.uploadfilejava.controller;

import com.onemount.uploadfilejava.model.UploadDocumentResponse;
import com.onemount.uploadfilejava.model.DocumentStorage;
import com.onemount.uploadfilejava.exception.DocumentStorageException;
import com.onemount.uploadfilejava.service.DocumentStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
public class DocumentController {

    @Autowired
    private DocumentStorageService documneStorageService;

    @PostMapping("/photo")
    public ResponseEntity<UploadDocumentResponse> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String description) {
        if (file == null) {
            throw new DocumentStorageException("You must select the a file for uploading", HttpStatus.BAD_REQUEST);
        }
        String fileName = documneStorageService.storeFile(file, description);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path(fileName).toUriString();
        return ResponseEntity.ok().body(new UploadDocumentResponse(fileName, fileDownloadUri, file.getSize()));

    }

    @GetMapping(value = "/photo/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) {

        Resource resource = null;
        if(fileName !=null && !fileName.isEmpty()) {
            try {
                resource = documneStorageService.loadFileAsResource(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok()

                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/photo")
    public ResponseEntity<List<DocumentStorage>> getAll(){
        try{
            List<DocumentStorage> documentStorages = documneStorageService.getAll();
            return ResponseEntity.ok().body(documentStorages);
        } catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }
}
