package com.effortguy.jwtdemo.web;

import com.effortguy.jwtdemo.config.JwtTokenProvider;
import com.effortguy.jwtdemo.domain.user.User;
import com.effortguy.jwtdemo.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/join")
    public Long join(@RequestBody User user) {
        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User findUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(user.getPassword(), findUser.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return jwtTokenProvider.createToken(user.getEmail(), Arrays.asList("ROLE_USER"));
    }
}
