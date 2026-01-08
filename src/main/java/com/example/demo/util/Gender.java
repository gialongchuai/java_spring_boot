package com.example.demo.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Gender {
    @JsonProperty("female")
    FEMALE,
    @JsonProperty("male")
    MALE,
    @JsonProperty("other")
    OTHER
}
