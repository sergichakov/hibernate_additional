package net.hibernate.additional.command.mapper;

import net.hibernate.additional.command.TaskCommandDTO;
import net.hibernate.additional.model.TaskEntity;
import org.mapstruct.Mapper;

import java.util.Set;
@Mapper(componentModel="default",uses=TaskCommandDtoEntityMapper.class )
public interface TagCommandDtoSetEntitySetMapper {
    Set<TaskEntity> toEntitySet(Set<TaskCommandDTO> dto);
    Set<TaskCommandDTO> toDtoSet(Set<TaskEntity> entity);
}
