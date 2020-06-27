package com.duytran.kdtrace.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProducerModel extends ProducerInfoModel{
    private List<ProductModel> productModels;
}
