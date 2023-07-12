package com.loco.transaction.management.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Min(value = 1, message = "Amount must be greater than 0")
    @NotNull(message = "Amount must not be null")
    @JsonProperty("amount")
    private Double amount;

    @NotBlank(message = "Type must not be blank")
    @JsonProperty("type")
    private String type;

    @Min(value = 1, message = "Parent id can not be 0")
    @JsonProperty("parent_id")
    private Long parentId;
}
