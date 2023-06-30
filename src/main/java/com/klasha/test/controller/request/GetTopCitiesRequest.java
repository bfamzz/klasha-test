package com.klasha.test.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTopCitiesRequest implements Cloneable{
    private int limit;
    private String order;
    private String orderBy;
    private String country;

    public Object cloneObj() throws CloneNotSupportedException {
        return this.clone();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
