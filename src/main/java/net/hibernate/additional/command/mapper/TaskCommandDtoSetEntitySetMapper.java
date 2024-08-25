package net.hibernate.additional.command.mapper;

import net.hibernate.additional.command.TagCommandDTO;

import net.hibernate.additional.model.TagEntity;
import org.mapstruct.Mapper;


import java.util.Set;

@Mapper(componentModel="default",uses=TagCommandDtoEntityMapper.class)
public interface TaskCommandDtoSetEntitySetMapper {
    Set<TagEntity> toEntitySet(Set<TagCommandDTO> dto);
    Set<TagCommandDTO> toDtoSet(Set<TagEntity> entity);
}
