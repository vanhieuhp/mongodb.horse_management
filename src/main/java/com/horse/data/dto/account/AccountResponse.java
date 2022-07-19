package com.horse.data.dto.account;

import com.horse.data.dto.AbstractResponseDTO;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse extends AbstractResponseDTO {

    private String username;
    private String role;
    private List<Map<String, String>> trainers;
    private List<Map<String, String>> horses;

    public AccountResponse(String id, String username, String role) {
        this.setId(id);
        this.username = username;
        this.role = role;
    }

    public AccountResponse(String id, String username, String role, List<Map<String, String>> trainers,
                           List<Map<String, String>> horses, Date createdAt, Date modifiedAt) {

        this.setId(id);
        this.setUsername(username);
        this.setRole(role);
        this.setTrainers(trainers);
        this.setHorses(horses);
        this.setCreatedAt(createdAt);
        this.setModifiedAt(modifiedAt);
    }
}
