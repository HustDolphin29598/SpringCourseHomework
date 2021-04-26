package com.onrmount.uploadfilejava;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
public class DocumentController {

    @Autowired
    private DocumentStorageService documneStorageService;

    @PostMapping("/photo")
    public UploadDocumentResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String description) {
        if (file == null) {
            throw new DocumentStorageException("You must select the a file for uploading", HttpStatus.BAD_REQUEST);
        }
        String fileName = documneStorageService.storeFile(file, description);

        String fileDownloadUri = "http://localhost:8080/" + fileName;

        return new UploadDocumentResponse(fileName, fileDownloadUri, file.getSize());

    }

    @GetMapping("/photo/{fileName}")
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
