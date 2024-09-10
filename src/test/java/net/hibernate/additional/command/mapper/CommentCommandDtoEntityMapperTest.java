package net.hibernate.additional.command.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentCommandDtoEntityMapperTest {
    @Test
    void INSTANCE_shouldReturnInstanceOfObject(){
        CommentCommandDtoEntityMapper INSTANCE= Mappers.getMapper(CommentCommandDtoEntityMapper.class);
        String given = INSTANCE.getClass().toString().substring(0,79);
        assertEquals("class net.hibernate.additional.command.mapper.CommentCommandDtoEntityMapperImpl",given );
    }
}
