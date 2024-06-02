package com.stellarlabs.authentication_and_authorization_service.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {

    private AccountResponseDto accountResponseDto;

    @Override
    public String toString() {
        return "AccountDTO{" +
                "accountResponseDto=" + accountResponseDto +
                '}';
    }
}
