package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.DayConverter;
import com.highschool.highschoolsystem.dto.response.DayResponse;
import com.highschool.highschoolsystem.entity.DayEntity;
import com.highschool.highschoolsystem.repository.DayRepository;
import com.highschool.highschoolsystem.util.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class DayService {
    @Autowired
    private DayRepository dayRepository;
    public List<DayResponse> getAllDays(){
//        order by dayofweek()
        List<DayEntity> days = dayRepository.findAll();
        var daySorted = days.stream().sorted((o1, o2) -> {
            DayOfWeek day1 = DayOfWeek.valueOf(o1.getName().toUpperCase());
            DayOfWeek day2 = DayOfWeek.valueOf(o2.getName().toUpperCase());

            return day1.compareTo(day2);
        }).toList();
        return DayConverter.toResponse(daySorted, new String[]{"id", "name"});
    }
}
