package com.radiuk.innotter_service.mapper;

import com.radiuk.innotter_service.dto.page.PageRequestDto;
import com.radiuk.innotter_service.dto.page.PageResponseDto;
import com.radiuk.innotter_service.entity.PageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PageMapper {

    PageResponseDto toDto(PageEntity pageEntity);

    PageEntity fromRequestDto(PageRequestDto dto);

    void updateFromDto(PageRequestDto dto, @MappingTarget PageEntity page);
}
