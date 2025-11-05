package moqim.me.facelook.mappers;


import moqim.me.facelook.domain.dtos.AuthorDto;
import moqim.me.facelook.domain.dtos.CreatePostRequestDto;
import moqim.me.facelook.domain.dtos.UpdatePostRequestDto;
import moqim.me.facelook.domain.entities.Post;
import moqim.me.facelook.domain.dtos.PostDto;
import moqim.me.facelook.domain.entities.User;
import moqim.me.facelook.domain.requests.CreatePostRequest;
import moqim.me.facelook.domain.requests.UpdatePostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface PostMapper {

    @Mapping(target = "author", source = "author")
    PostDto toDto(Post post);
    
    AuthorDto toAuthorDto(User user);
    
    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);
    UpdatePostRequest updatePostRequest(UpdatePostRequestDto dto);

}
