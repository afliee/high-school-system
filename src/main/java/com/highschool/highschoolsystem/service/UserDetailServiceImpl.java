package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.util.principal.UserPrincipal;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TeacherEntity teacher = teacherRepository.findByName(username).orElse(null);

        if (teacher != null) {
            return new UserPrincipal(teacher);
        }

        StudentEntity student = studentRepository.findByName(username).orElse(null);

        if (student != null) {
            return new UserPrincipal(student);
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

}
