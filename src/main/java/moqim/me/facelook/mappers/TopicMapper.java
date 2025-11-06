package moqim.me.facelook.mappers;


import jakarta.validation.Valid;
import moqim.me.facelook.domain.dtos.CreateTopicRequestDto;
import moqim.me.facelook.domain.dtos.TopicDto;
import moqim.me.facelook.domain.dtos.UpdateTopicRequestDto;
import moqim.me.facelook.domain.entities.Post;
import moqim.me.facelook.domain.entities.Topic;
import moqim.me.facelook.domain.requests.CreateTopicRequest;
import moqim.me.facelook.domain.requests.UpdateTopicRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TopicMapper {


    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    @Mapping(target = "creatorId", source = "creator.id")
    @Mapping(target = "title", source = "name")
    TopicDto toDto(Topic topic);

    @Mapping(target = "name", source = "title")
    Topic fromDto(TopicDto topicDto);

    CreateTopicRequest toCreateTopicRequest(CreateTopicRequestDto createTopicRequestDto);
    UpdateTopicRequest toUpdateTopicRequest(UpdateTopicRequestDto updateTopicRequestDto);


    @Named("calculatePostCount")
    default Integer calculatePostCount(List<Post> posts) {
        if (posts == null) {
            return 0;
        }
        return posts.size();
    }
}
