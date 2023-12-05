package com.example.web_2.brand;

import com.example.web_2.baseEntity.BaseEntity;
import com.example.web_2.model.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@Table(name = "brands")
public class Brand extends BaseEntity {
    private String name;
    private List<Model> models;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Brand's name can't be blank")
    public String getName() {
        return name;
    }
    @BatchSize(size = 10)
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "brand")
    public List<Model> getModels() {
        return models;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }
}
