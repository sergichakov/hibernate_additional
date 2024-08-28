package net.hibernate.additional.command.mapper;

import net.hibernate.additional.command.TagCommandDTO;
import net.hibernate.additional.mapper.TagEntityDtoMapper;
import net.hibernate.additional.model.TagEntity;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel="default")
public interface TagCommandDtoEntityMapper {
    TagCommandDtoEntityMapper INSTANCE= Mappers.getMapper(TagCommandDtoEntityMapper.class);
    TagCommandDTO toDTO(TagEntity tag);
    TagEntity toModel(TagCommandDTO tagDTO);
}

