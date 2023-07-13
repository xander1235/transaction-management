package com.loco.transaction.management.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.loco.transaction.management.constants.LocoExceptionConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Min(value = 1, message = LocoExceptionConstants.AMOUNT_LESS_THAN_1)
    @NotNull(message = LocoExceptionConstants.AMOUNT_NULL)
    @JsonProperty("amount")
    private Double amount;

    @NotBlank(message = LocoExceptionConstants.TYPE_BLANK)
    @JsonProperty("type")
    private String type;

    @Min(value = 1, message = LocoExceptionConstants.PARENT_ID_LESS_THAN_1)
    @JsonProperty("parent_id")
    private Long parentId;
}
