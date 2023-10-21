package com.group.practic.dto;

import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ReferenceTitleEntity;
import java.util.Set;
import lombok.Getter;

@Getter
public class AdditionalMaterialsDto {

    long id;

    int number;

    String name;

    Set<ReferenceTitleEntity> refs;

    boolean checked;


    public static AdditionalMaterialsDto map(AdditionalMaterialsEntity entity, boolean checked) {
        AdditionalMaterialsDto dto = new AdditionalMaterialsDto();
        dto.id = entity.getId();
        dto.number = entity.getNumber();
        dto.name = entity.getName();
        dto.refs = entity.getRefs();
        dto.checked = checked;
        return dto;
    }

}
