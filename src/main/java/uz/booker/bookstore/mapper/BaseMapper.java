package uz.booker.bookstore.mapper;

import org.mapstruct.MappingTarget;

public interface BaseMapper<Entity, DTO>{

    DTO toDto(Entity entity);

    Entity toEntity(DTO dto);

    Entity merge(DTO dto, @MappingTarget Entity entity);

}



