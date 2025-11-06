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
import org.mapstruct.Named;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "likesCount", source = ".", qualifiedByName = "calculateLikesCount")
    @Mapping(target = "dislikesCount", source = ".", qualifiedByName = "calculateDislikesCount")
    @Mapping(target = "commentsCount", source = ".", qualifiedByName = "calculateCommentsCount")
    PostDto toDto(Post post);
    
    AuthorDto toAuthorDto(User user);
    
    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);
    UpdatePostRequest updatePostRequest(UpdatePostRequestDto dto);

    @Named("calculateLikesCount")
    default int calculateLikesCount(Post post) {
        if (post == null || post.getEmotions() == null) return 0;
        return (int) post.getEmotions().stream()
                .filter(e -> e.getStatus() == moqim.me.facelook.domain.enums.EmotionStatus.LIKE)
                .count();
    }

    @Named("calculateDislikesCount")
    default int calculateDislikesCount(Post post) {
        if (post == null || post.getEmotions() == null) return 0;
        return (int) post.getEmotions().stream()
                .filter(e -> e.getStatus() == moqim.me.facelook.domain.enums.EmotionStatus.DISLIKE)
                .count();
    }

    @Named("calculateCommentsCount")
    default int calculateCommentsCount(Post post) {
        if (post == null || post.getComments() == null) return 0;
        return post.getComments().size();
    }

}
