package net.hibernate.additional.mapper;

import net.hibernate.additional.model.TagEntity;
import net.hibernate.additional.dto.TagDTO;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel="default")
public interface TagEntityDtoMapper {
    @Mapping(target = "task", ignore = true)
    @IterableMapping(qualifiedByName="mapWithoutTask")
    TagDTO toDTO(TagEntity tag);
    @Named("mapWithoutTask")
    @Mapping(target = "task", ignore = true)
    TagDTO mapWithoutTask(TagEntity source);
    @Mapping(target = "task", ignore = true)
    @IterableMapping(qualifiedByName="dtoTask")
    TagEntity toModel(TagDTO tagDTO);
    @Named("dtoTask")
    @Mapping(target = "task", ignore = true)
    TagEntity dtoTask( TagDTO source);


}

