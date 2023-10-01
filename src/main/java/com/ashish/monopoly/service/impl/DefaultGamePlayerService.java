package com.ashish.monopoly.service.impl;

import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.repository.GamePlayerRepository;
import com.ashish.monopoly.service.GamePlayerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultGamePlayerService implements GamePlayerService {

    @Resource
    private GamePlayerRepository gamePlayerRepository;

    @Override
    public Optional<GamePlayer> findById(Integer id) {
        return gamePlayerRepository.findById(id);
    }
}
