package com.ashish.monopoly.service.impl;

import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.repository.PlayerRepository;
import com.ashish.monopoly.service.PlayerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultPlayerService implements PlayerService {

    @Resource
    private PlayerRepository playerRepository;

    @Override
    public Player save(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @Override
    public Optional<Player> findByName(String name) {
        return playerRepository.findByName(name);
    }

    @Override
    public Boolean deleteById(Integer id) {
        try {
            playerRepository.deleteById(id);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public Player findById(Integer id) {
        return playerRepository.findById(id).orElse(null);
    }
}
