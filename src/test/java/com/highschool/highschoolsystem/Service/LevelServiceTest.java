package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.LevelConverter;
import com.highschool.highschoolsystem.dto.request.LevelRequest;
import com.highschool.highschoolsystem.dto.response.LevelResponse;
import com.highschool.highschoolsystem.entity.LevelEntity;
import com.highschool.highschoolsystem.repository.LevelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LevelServiceTest {
    @Mock
    private LevelRepository levelRepository;

    private LevelService levelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        levelService = new LevelService(levelRepository);
    }

   @Test
   void findAll_ShouldReturnAllLevels() {
       LevelEntity levelEntity = new LevelEntity();
       when(levelRepository.findAll()).thenReturn(List.of(levelEntity));

       List<LevelResponse> levels = levelService.findAll();

       verify(levelRepository, times(1)).findAll();
   }

   @Test
   void create_ShouldReturnCreatedLevel() {
       LevelRequest levelRequest = LevelRequest.builder().levelNumber(0).build();
       LevelEntity levelEntity = LevelEntity.builder().levelNumber(0).build();
       MockedStatic<LevelConverter> levelConverterMockedStatic = mockStatic(LevelConverter.class);
       levelConverterMockedStatic.when(() -> LevelConverter.toResponse(levelEntity)).thenReturn(new LevelResponse());
       levelConverterMockedStatic.when(() -> LevelConverter.toEntity(levelRequest)).thenReturn(levelEntity);
       when(levelRepository.findByLevelNumber(levelRequest.getLevelNumber())).thenReturn(Optional.empty());
       when(levelRepository.save(levelEntity)).thenReturn(levelEntity);

       LevelResponse levelResponse = levelService.create(levelRequest);

       verify(levelRepository, times(1)).save(levelEntity);
       verify(levelRepository, times(1)).findByLevelNumber(levelRequest.getLevelNumber());
   }

   @Test
    void create_WhenLevelNumberAlreadyExists_ShouldThrowException() {
       LevelRequest levelRequest = LevelRequest.builder().levelNumber(0).build();
       LevelEntity levelEntity = LevelEntity.builder().levelNumber(0).build();
       when(levelRepository.findByLevelNumber(levelRequest.getLevelNumber())).thenReturn(Optional.of(levelEntity));

       assertThrows(RuntimeException.class, () -> levelService.create(levelRequest));
   }

   @Test
    void create_WhenLevelNumberIsNegative_ShouldThrowException() {
       LevelRequest levelRequest = LevelRequest.builder().levelNumber(-1).build();
       when(levelRepository.findByLevelNumber(levelRequest.getLevelNumber())).thenReturn(Optional.empty());

       assertThrows(RuntimeException.class, () -> levelService.create(levelRequest));
   }

}
