package com.ashish.monopoly.service.impl;

import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.model.PlayerType;
import com.ashish.monopoly.repository.PlayerProjection;
import com.ashish.monopoly.repository.PlayerRepository;
import com.ashish.monopoly.service.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultPlayerService implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    @Transactional
    public Player save(Player player) {
        assignTypeByName(player);
        return playerRepository.save(player);
    }

    @Override
    @Transactional
    public List<Player> saveAll(List<Player> players) {
        players.forEach(this::assignTypeByName);
        return playerRepository.saveAll(players);
    }

    @Override
    public Optional<Player> findByName(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return playerRepository.findByNameIgnoreCase(name.trim());
    }

    @Override
    @Transactional
    public Boolean deleteById(Integer id) {
        Optional<Player> playerOpt = playerRepository.findById(id);

        if (playerOpt.isEmpty()) {
            return Boolean.FALSE;
        }

        Player player = playerOpt.get();

        if (player.getType() == PlayerType.BANK || player.getType() == PlayerType.BOT) {
            return Boolean.FALSE;
        }

        // 3. Delete safely
        try {
            playerRepository.deleteById(id);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.warn("Failed to delete player with ID {}: {}", id, e.getMessage());
            return Boolean.FALSE;
        }
    }

    @Override
    public List<Player> findByNames(List<String> names) {
        if (names == null || names.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> lowerNames = names.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .distinct()
                .toList();
        if (lowerNames.isEmpty()) {
            return Collections.emptyList();
        }
        return playerRepository.findByNamesIgnoreCase(lowerNames);
    }

    @Override
    public Optional<Player> findById(Integer id) {
        return playerRepository.findById(id);
    }

    @Override
    public Set<PlayerProjection> findAllProjectedByIdNotNull() {
        return playerRepository.findAllProjectedByIdNotNull();
    }

    private void assignTypeByName(Player player) {
        if (player.getName() == null) return;

        if ("Bank".equalsIgnoreCase(player.getName())) {
            player.setType(PlayerType.BANK);
        } else if ("Bot".equalsIgnoreCase(player.getName())) {
            player.setType(PlayerType.BOT);
        } else {
            if (player.getType() == null) {
                player.setType(PlayerType.HUMAN);
            }
        }
    }

    @Override
    @Transactional
    public Player updateName(Integer id, String name) {
        return playerRepository.findById(id)
                .map(existingPlayer -> {
                    existingPlayer.setName(name);
                    return this.save(existingPlayer);
                })
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + id));
    }
}
