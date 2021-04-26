package com.onemount.uploadfilejava.model;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "uploaded_documents")
@Table(name = "uploaded_documents")
@Data
public class DocumentStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private String description;

    private String name;

    private String link ;

    private String format;
}
