package com.dcmd.dmiracore.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Document(collection = "comments")
public class Comment {
    @Id
    private String id;
    @NotBlank
    @Size(max = 2048)
    private String content;
    @DBRef
    private Task task;
}
