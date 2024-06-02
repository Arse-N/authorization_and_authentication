package com.stellarlabs.authentication_and_authorization_service.model;

import lombok.*;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("permissions")
public class Permissions {
    @Id
    private String id;

    private UserRole userRole;

}
