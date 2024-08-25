package net.hibernate.additional.command.mapper;

import net.hibernate.additional.command.TagCommandDTO;
import net.hibernate.additional.model.TagEntity;

import org.mapstruct.Mapper;


@Mapper(componentModel="default")
public interface TagCommandDtoEntityMapper {
    TagCommandDTO toDTO(TagEntity tag);
    TagEntity toModel(TagCommandDTO tagDTO);
}

