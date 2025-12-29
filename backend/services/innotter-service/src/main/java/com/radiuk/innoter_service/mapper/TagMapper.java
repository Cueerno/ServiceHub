package com.radiuk.innoter_service.mapper;

import com.radiuk.innoter_service.dto.tag.TagRequestDto;
import com.radiuk.innoter_service.dto.tag.TagResponseDto;
import com.radiuk.innoter_service.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TagMapper {

    TagResponseDto toDto(Tag tag);

    Tag fromRequestDto(TagRequestDto dto);
}
