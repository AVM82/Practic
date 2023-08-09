package com.group.practic.util;

import com.group.practic.dto.CourseDto;
import com.group.practic.dto.PersonDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public interface Converter {

  @Bean
  public static ModelMapper modelMapper() {
    return new ModelMapper();
  }


  public static CourseDto convert(CourseEntity courseEntity) {
    return modelMapper().map(courseEntity, CourseDto.class);
  }


  public static CourseEntity convert(CourseDto courseDto) {
    return modelMapper().map(courseDto, CourseEntity.class);
  }


  public static PersonDto convert(PersonEntity personEntity) {
    return modelMapper().map(personEntity, PersonDto.class);
  }


  public static PersonEntity convert(PersonDto personDto) {
    return modelMapper().map(personDto, PersonEntity.class);
  }



}
