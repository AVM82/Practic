package com.group.practic.dto;


import lombok.Data;

import java.util.Objects;

@Data
public class SkillDto {

    private String name;

    public SkillDto(String name) {
        this.name = name;
    }

    public SkillDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkillDto)) return false;
        SkillDto skillDto = (SkillDto) o;
        return Objects.equals(getName(), skillDto.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
