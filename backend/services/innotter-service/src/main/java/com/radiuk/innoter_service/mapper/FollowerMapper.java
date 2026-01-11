package com.radiuk.innoter_service.mapper;

import com.radiuk.innoter_service.dto.follower.FollowerResponseDto;
import com.radiuk.innoter_service.entity.Follower;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FollowerMapper {

    FollowerResponseDto toDto(Follower follower);
}
