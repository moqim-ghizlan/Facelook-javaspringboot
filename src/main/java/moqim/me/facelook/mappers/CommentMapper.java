package moqim.me.facelook.mappers;

import moqim.me.facelook.domain.dtos.AuthorDto;
import moqim.me.facelook.domain.dtos.CommentDto;
import moqim.me.facelook.domain.entities.Comment;
import moqim.me.facelook.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class})
public interface CommentMapper {

    @Mapping(target = "author", source = "user")
    CommentDto toDto(Comment comment);
}
