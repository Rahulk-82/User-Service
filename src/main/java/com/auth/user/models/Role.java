package com.auth.user.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity(name="user_role")
@Getter
@Setter
public class Role extends BaseModel{
    private String name;
}
