package com.group.practic.dto;


import java.util.Objects;


public class SkillDto {

    private String name;

    public SkillDto(String name) {
        this.name = name;
    }

    public SkillDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillDto skillDto)) {
            return false;
        }
        return Objects.equals(getName(), skillDto.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "SkillDto{"
                +
                "name='" + name + '\''
                +
                '}';
    }
}
