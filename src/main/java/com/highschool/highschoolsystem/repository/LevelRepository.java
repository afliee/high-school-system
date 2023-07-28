package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.ClassEntity;
import com.highschool.highschoolsystem.entity.LevelEntity;
import com.highschool.highschoolsystem.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LevelRepository extends JpaRepository<LevelEntity, String> {
    Optional<LevelEntity> findByLevelNumber(int levelNumber);
}
