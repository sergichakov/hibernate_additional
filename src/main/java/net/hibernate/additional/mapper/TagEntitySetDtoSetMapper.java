package net.hibernate.additional.mapper;

import net.hibernate.additional.model.TaskEntity;
import net.hibernate.additional.dto.TaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
@Mapper(componentModel="default")//,uses=TaskEntityDtoMapper.class )
public interface TagEntitySetDtoSetMapper {
    @Mapping(target = "task", ignore = true)
    Set<TaskEntity> toEntitySet(Set<TaskDTO> dto);
    @Mapping(target = "task", ignore = true)
    Set<TaskDTO> toDtoSet(Set<TaskEntity> entity);
}
