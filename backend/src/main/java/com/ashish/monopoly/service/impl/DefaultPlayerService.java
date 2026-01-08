package com.ashish.monopoly.service.impl;

import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.model.PlayerType;
import com.ashish.monopoly.repository.PlayerProjection;
import com.ashish.monopoly.repository.PlayerRepository;
import com.ashish.monopoly.service.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class DefaultPlayerService implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public Player save(Player player) {
        assignTypeByName(player);
        return playerRepository.save(player);
    }

    @Override
    public List<Player> saveAll(List<Player> players) {
        players.forEach(this::assignTypeByName);
        return playerRepository.saveAll(players);
    }

    @Override
    public Optional<Player> findByName(String name) {
        return playerRepository.findByName(name);
    }

    @Override
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
            return Boolean.FALSE;
        }
    }

    @Override
    public List<Player> findByNames(List<String> names) {
        return playerRepository.findByNameIn(names);
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
    public Player updateName(Integer id, String name) {
        return playerRepository.findById(id)
                .map(existingPlayer -> {
                    existingPlayer.setName(name);
                    return this.save(existingPlayer);
                })
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + id));
    }
}
