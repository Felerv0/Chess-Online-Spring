package com.samsung.chess_online.security.component;

import com.samsung.chess_online.dto.GameDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class GameCheck {
    public boolean checkUsername(GameDto gameDto, Authentication authentication) {
        return gameDto.getPlayer1().equals(authentication.getName()) || gameDto.getPlayer2().equals(authentication.getName());
    }
}
