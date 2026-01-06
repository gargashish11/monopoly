package com.ashish.monopoly.controllers.form;

import com.ashish.monopoly.model.Player;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class GameFormData {
  private Integer initialBalance = 2000;
  private List<Player> players = new ArrayList<>();
}
