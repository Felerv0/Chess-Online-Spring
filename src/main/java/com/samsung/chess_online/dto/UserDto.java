package com.samsung.chess_online.dto;

import com.samsung.chess_online.domain.Country;
import com.samsung.chess_online.domain.User;
import com.samsung.chess_online.repository.CountryRepository;
import com.samsung.chess_online.services.impl.CountryServiceImpl;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class UserDto {
    private final String username;
    private final String fullname;
    private final String email;
    private final String password;
    private final Long country_id;

    public static UserDto toDto(User user) {
        return UserDto.builder().username(user.getUsername()).fullname(user.getFullname())
                .email(user.getEmail()).password(user.getPassword()).country_id(user.getCountry_id()).build();
    }

    public static User fromDto(UserDto userDto) {
        return User.builder().username(userDto.getUsername()).fullname(userDto.getFullname())
                .email(userDto.getEmail()).password(userDto.getPassword()).country_id(userDto.getCountry_id()).build();
    }
}
