package com.stellarlabs.authentication_and_authorization_service.dto.Payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {

    private String accountUuid;

    private boolean isPaid;

}
