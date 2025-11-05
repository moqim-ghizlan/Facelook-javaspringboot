package moqim.me.facelook.mappers;

import moqim.me.facelook.domain.dtos.AuthorDto;
import moqim.me.facelook.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface UserMapper {
    AuthorDto toAuthorDto(User u);
}
