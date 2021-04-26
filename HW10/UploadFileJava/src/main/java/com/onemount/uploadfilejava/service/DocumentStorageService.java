package com.onemount.uploadfilejava.service;

import com.onemount.uploadfilejava.repository.DocumentStorageRepo;
import com.onemount.uploadfilejava.exception.DocumentStorageException;
import com.onemount.uploadfilejava.model.DocumentStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DocumentStorageService {

    private final Path fileStorageLocation;

    @Autowired
    DocumentStorageRepo docStorageRepo;

    @Autowired
    public DocumentStorageService() {

        this.fileStorageLocation = Path.of("uploaded/");

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new DocumentStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public String storeFile(MultipartFile file, String description) {

        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new DocumentStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            DocumentStorage doc = docStorageRepo.findDocumentStorageByName(fileName);
            if (doc != null) {
                doc.setFormat(file.getContentType());
                doc.setName(fileName);
                doc.setDescription(description);
                docStorageRepo.save(doc);
            } else {
                DocumentStorage newDoc = new DocumentStorage();
                newDoc.setDescription(description);
                newDoc.setFormat(file.getContentType());
                newDoc.setName(fileName);
                newDoc.setLink(ServletUriComponentsBuilder.fromCurrentContextPath().path(fileName).toUriString());
                docStorageRepo.save(newDoc);
            }

            log.info("fileName: " + fileName);
            log.info("contentType: " + file.getContentType());
            log.info("size: " + file.getSize());
            log.info("description: " + description);

            return fileName;
        } catch (IOException ex) {
            throw new DocumentStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }


    public Resource loadFileAsResource(String fileName) throws Exception {

        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }

    public List<DocumentStorage> getAll(){
        return docStorageRepo.findAll();
    }

}

