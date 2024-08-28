package net.hibernate.additional.command.mapper;

import net.hibernate.additional.command.TaskCommandDTO;import net.hibernate.additional.dto.*;
////////////////////////////////////import net.hibernate_additional.mapper.CommentEntityDtoMapper;
import net.hibernate.additional.model.TaskEntity;
import org.mapstruct.Mapper;
import net.hibernate.additional.command.*;
import net.hibernate.additional.command.mapper.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="default",uses={
        TagCommandDtoEntityMapper.class, TagCommandDtoSetEntitySetMapper.class})
        ////////////////////CommentCommandDtoEntityMapper.class,CommentCommandDtoSetEntitySetMapper.class})
public interface TaskCommandDtoEntityMapper {
    TaskCommandDtoEntityMapper INSTANCE= Mappers.getMapper(TaskCommandDtoEntityMapper.class);

    TaskCommandDTO toDTO(TaskEntity task);
    TaskEntity toModel(TaskCommandDTO taskDTO);
}
