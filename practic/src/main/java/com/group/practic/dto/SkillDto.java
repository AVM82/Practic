package com.group.practic.dto;


import com.group.practic.entity.SkillEntity;
import java.util.Objects;


public class SkillDto {

    private String name;


    public SkillDto(String name) {
        this.name = name;
    }


    public SkillDto() {}


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        return o != null && getClass() == o.getClass()
                && (this == o || name.equals(((SkillDto) o).name));
    }


    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }


    @Override
    public String toString() {
        return "SkillDto{" + "name='" + name + '\'' + '}';
    }


    public static SkillDto map(SkillEntity skill) {
        return new SkillDto(skill.getName());
    }

}
