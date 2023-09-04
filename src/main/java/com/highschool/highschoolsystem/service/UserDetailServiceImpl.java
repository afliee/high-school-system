package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.UserConverter;
import com.highschool.highschoolsystem.entity.AdminEntity;
import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.entity.UserEntity;
import com.highschool.highschoolsystem.repository.AdminRepository;
import com.highschool.highschoolsystem.util.principal.UserPrincipal;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AdminRepository adminRepository;

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

        AdminEntity admin = adminRepository.findByUsername(username).orElse(null);

        if (admin != null) {
            return UserConverter.toPrincipal(admin);
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    public Object findByUsername(String username) {
        TeacherEntity teacher = teacherRepository.findByName(username).orElse(null);

        if (teacher != null) {
            return teacher;
        }

        StudentEntity student = studentRepository.findByName(username).orElse(null);

        if (student != null) {
            return student;
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }


    public long count() {
        return teacherRepository.count() + studentRepository.count() + adminRepository.count();
    }
}
