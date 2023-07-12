package com.loco.transaction.management.pojo.response;

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
public class Response implements Serializable {

    private static final Long serialVersionUID = 1L;

    @JsonProperty("status")
    private String status;

}
