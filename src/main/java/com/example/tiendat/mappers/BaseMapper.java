package com.example.tiendat.mappers;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

public interface BaseMapper <E, R, C, U> { // E: entity, R: resource, C: create, U:update

    //Entity -> Resource
    R tResource(E entity);

    // default R tResource(E entity) {
    //     throw new UnsupportedOperationException("khong the implement");
    // }

    // List Entity -> List Resource
    List<R> tList(List<E> entities);

    //Page
    default Page<R> toResourcePage(Page<E> page) {
        return page.map(this::tResource);
    }

    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    E toEntity(C createRequest);

    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(U updateRequest, @MappingTarget E entity);
}
