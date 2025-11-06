package moqim.me.facelook.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import moqim.me.facelook.domain.dtos.CreateTopicRequestDto;
import moqim.me.facelook.domain.dtos.TopicDto;
import moqim.me.facelook.domain.dtos.UpdateTopicRequestDto;
import moqim.me.facelook.domain.entities.Topic;
import moqim.me.facelook.domain.entities.User;
import moqim.me.facelook.domain.requests.CreateTopicRequest;
import moqim.me.facelook.domain.requests.UpdateTopicRequest;
import moqim.me.facelook.mappers.TopicMapper;
import moqim.me.facelook.security.FBUserDetails;
import moqim.me.facelook.services.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;
    private final TopicMapper topicMapper;

    @GetMapping(path = "/all")
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        List<TopicDto> allTopics = topicService.listTopics().stream().map(topicMapper::toDto).toList();
        return ResponseEntity.ok(allTopics);
    }

    @GetMapping(path = "/one/{id}")
    public ResponseEntity<TopicDto> getAllTopics(@PathVariable long id) {
        Topic topic = topicService.getTopicById(id);
        TopicDto topicDto = topicMapper.toDto(topic);
        return ResponseEntity.ok(topicDto);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<TopicDto> createTopic(
            @Valid @RequestBody CreateTopicRequestDto createTopicRequestDto,
            @AuthenticationPrincipal FBUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        CreateTopicRequest req = topicMapper.toCreateTopicRequest(createTopicRequestDto);
        Topic topic = Topic.builder()
                .name(req.getTitle())
                .build();
        User creator = new User();
        creator.setId(user.getId());
        topic.setCreator(creator);

        Topic created = topicService.createTopic(topic);
        return ResponseEntity.ok(topicMapper.toDto(created));
    }

    @PutMapping(path = "/one/{id}/update")
    public ResponseEntity<TopicDto> updateTopic(
            @PathVariable long id,
            @Valid @RequestBody UpdateTopicRequestDto updateTopicRequestDto,
            @AuthenticationPrincipal FBUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        Topic existing = topicService.getTopicById(id);
        if (existing.getCreator() == null || existing.getCreator().getId() != user.getId()) {
            return ResponseEntity.status(403).build();
        }

        UpdateTopicRequest req = topicMapper.toUpdateTopicRequest(updateTopicRequestDto);
        existing.setName(req.getTitle());

        Topic updated = topicService.updateTopic(existing);
        return ResponseEntity.ok(topicMapper.toDto(updated));
    }

    @DeleteMapping(path = "/one/{id}/delete")
    public ResponseEntity<TopicDto> deleteTopic(
            @PathVariable long id,
            @AuthenticationPrincipal FBUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        Topic existing = topicService.getTopicById(id);
        if (existing.getCreator() == null || existing.getCreator().getId() != user.getId()) {
            return ResponseEntity.status(403).build();
        }
        TopicDto dto = topicMapper.toDto(existing);
        topicService.deleteTopic(id);
        return ResponseEntity.ok(dto);
    }
}
