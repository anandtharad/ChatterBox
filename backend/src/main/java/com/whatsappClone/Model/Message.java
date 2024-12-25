package com.whatsappClone.Model;

import java.time.LocalDateTime;

import jakarta.persistence.*;


import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String content; // Text content of the message

    @Lob
    @Column(name = "file_data", columnDefinition = "LONGBLOB")
    private byte[] fileData; // BLOB to store the file

    private String fileType; // File type (e.g., application/pdf)

    private String fileName; // Original filename for reference

    private LocalDateTime timestamp;

    @ManyToOne
    private Chat chat;

    @ManyToOne
    private User user;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Constructors
    public Message() {
    }

    public Message(Integer id, String content, byte[] fileData, String fileType, String fileName, LocalDateTime timestamp, Chat chat, User user) {
        this.id = id;
        this.content = content;
        this.fileData = fileData;
        this.fileType = fileType;
        this.fileName = fileName;
        this.timestamp = timestamp;
        this.chat = chat;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Message [id=" + id + ", content=" + content + ", fileType=" + fileType +
                ", fileName=" + fileName + ", timestamp=" + timestamp + ", chat=" + chat + ", user=" + user + "]";
    }
}

