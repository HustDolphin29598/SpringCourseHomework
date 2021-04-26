package com.onrmount.uploadfilejava;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentStorageRepo extends JpaRepository<DocumentStorage, Integer> {

    DocumentStorage findDocumentStorageByName(String name);
}
