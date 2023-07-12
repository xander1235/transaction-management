package com.loco.transaction.management.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDetails implements Serializable {

    private static final Long serialVersionUID = 1L;

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("status")
    private String status;

    @JsonProperty("error_code")
    private Integer errorCode;

}
