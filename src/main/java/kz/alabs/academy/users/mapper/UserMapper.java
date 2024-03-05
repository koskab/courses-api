package kz.alabs.academy.users.mapper;

import kz.alabs.academy.types.User;
import kz.alabs.academy.types.UserCreateInput;
import kz.alabs.academy.types.UserUpdateInput;
import kz.alabs.academy.users.entity.UserEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity toEntity(UserCreateInput userCreate);

    UserEntity toEntity(@MappingTarget UserEntity userEntity, UserUpdateInput userUpdate);

    User toView(UserEntity userEntity);

}
