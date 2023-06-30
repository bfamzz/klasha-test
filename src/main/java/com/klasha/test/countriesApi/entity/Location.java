package com.klasha.test.countriesApi.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private float lat;

    @SerializedName(value = "long")
    private float lng;
}
