package com.stellarlabs.authentication_and_authorization_service.model;

import com.stellarlabs.authentication_and_authorization_service.dto.auth.UserType;
import lombok.*;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("purr_friend")
public class User {
    /**
     * Auto generated ID for entity, can not be null
     */

    @Id
    private String id;

    /**
     * Generating ID for entity based on UUID, can not be null
     */
    private String uuid = UUID.randomUUID().toString();

    private String firstName;

    private String lastName;

    private UserType userType;

    private String companyName;

    private String phone;

    private String email;

    private String password;

//    @ManyToOne
//    @JoinColumn(name = "user_role_id")
    private Permissions permissions;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean verification = false;


    private LocalDateTime expirationTime;

    private String passwordToken;

    private String verificationToken;


}
