package com.stellarlabs.authentication_and_authorization_service.model;

import lombok.*;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("purr_friend_info")
public class UserInfo {
    @Id
    private String id;

    private String userId;

    private boolean quiz;

    private byte question;

    private int failedCount = 0;

    private int blockedCount = 0;

    private boolean blocked = false;
    private boolean permanentlyBlocked = false;

    private LocalDateTime blockTime;

}
