package com.group.practic.dto;

import java.util.Objects;

public final class CourseDTO {
    private final String name;
    private final String description;

    CourseDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (CourseDTO) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "CourseDTO[" +
                "name=" + name + ", " +
                "description=" + description + ']';
    }

}
