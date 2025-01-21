package com.example.admission.admissionswebsite.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminPostDto {
    private int id; // ID của trường đại học

    private String title;
    private int statusCode;
    private String message;
    private String error;
    // Tiêu đề bài đăng
    private String content;      // Nội dung bài đăng
    private MultipartFile image; // Ảnh bài đăng
    private String category;     // Danh mục bài đăng
    private String status;       // Trạng thái (Ví dụ: "Published", "Draft")
    private String description;
    // Trạng thái (Ví dụ: "Published", "Draft")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date postDate;

    private LocalDateTime publishDate; // Hoặc sử dụng java.util.Date nếu cần

    public AdminPostDto() {

    }

    // Getters và Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public AdminPostDto(int id, String title, int statusCode, String message, String error, String content, MultipartFile image, String category, String status, String description, Date postDate, LocalDateTime publishDate) {
        this.id = id;
        this.title = title;
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
        this.content = content;
        this.image = image;
        this.category = category;
        this.status = status;
        this.description = description;
        this.postDate = postDate;
        this.publishDate = publishDate;
    }
}
