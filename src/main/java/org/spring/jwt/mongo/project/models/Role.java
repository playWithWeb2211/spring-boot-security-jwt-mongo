package org.spring.jwt.mongo.project.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@NoArgsConstructor
@Getter
@Setter
public class Role {

    private String id;

    private ERole name;

    public Role(ERole name) {
        this.name = name;
    }
}
