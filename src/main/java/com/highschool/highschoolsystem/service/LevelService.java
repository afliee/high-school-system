package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.LevelConverter;
import com.highschool.highschoolsystem.dto.request.LevelRequest;
import com.highschool.highschoolsystem.dto.response.LevelResponse;
import com.highschool.highschoolsystem.entity.BaseEntity;
import com.highschool.highschoolsystem.entity.LevelEntity;
import com.highschool.highschoolsystem.repository.LevelRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class LevelService {
    @Autowired
    private LevelRepository levelRepository;

    public List<LevelResponse> findAll(boolean isDetail) {
        if (isDetail) {
            return LevelConverter.toResponse(levelRepository.findAll());
        }
        return LevelConverter.toResponse(levelRepository.findAll(), new String[] {"id", "levelNumber", "subjects", "name"});
    }

    public List<LevelResponse> findAll() {
        return levelRepository.findAll().stream().map(level -> LevelResponse.builder()
                .id(level.getId())
                .name(level.getName())
                .build()
        ).toList();
    }

    public LevelResponse create(LevelRequest levelRequest) {
        levelRepository.findByLevelNumber(levelRequest.getLevelNumber()).ifPresent(level -> {
            throw new RuntimeException("Level number already exists");
        });

        return LevelConverter.toResponse(levelRepository.save(LevelConverter.toEntity(levelRequest)));
    }

    public List<String> getAllLevelId() {
        return levelRepository.findAll().stream().map(BaseEntity::getId).toList();
    }
    public List<LevelEntity> getAllLevel() {
        return levelRepository.findAll().stream().toList();
    }
}
