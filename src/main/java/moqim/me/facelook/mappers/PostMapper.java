package moqim.me.facelook.mappers;


import moqim.me.facelook.domain.dtos.CreatePostRequestDto;
import moqim.me.facelook.domain.dtos.UpdatePostRequestDto;
import moqim.me.facelook.domain.entities.Post;
import moqim.me.facelook.domain.dtos.PostDto;
import moqim.me.facelook.domain.requests.CreatePostRequest;
import moqim.me.facelook.domain.requests.UpdatePostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface PostMapper {

    PostDto toDto(Post post);
    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);
    UpdatePostRequest updatePostRequest(UpdatePostRequestDto dto);

}
