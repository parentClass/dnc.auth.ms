package com.dnc.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Document(collection = "credentials")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialDTO {
    @Id
    private String id;
    private String username;
    private String email;
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Field("is_deleted")
    @JsonProperty("is_deleted")
    private boolean isDeleted;
    @CreatedDate
    @Field("created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @Field("modified_at")
    @JsonProperty("modified_at")
    private LocalDateTime modifiedAt;
    @Field("deleted_at")
    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;
}
