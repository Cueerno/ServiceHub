package com.radiuk.innoter_service.mapper;

import com.radiuk.innoter_service.dto.post.PostRequestDto;
import com.radiuk.innoter_service.dto.post.PostResponseDto;
import com.radiuk.innoter_service.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {

    PostResponseDto toDto(Post post);

    Post fromRequestDto(PostRequestDto dto);

    void updateFromDto(PostRequestDto dto, @MappingTarget Post post);
}
