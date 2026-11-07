package com.ashish.monopoly.controllers.form;

import com.ashish.monopoly.model.Player;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameFormData {
  private Integer initialBalance = 2000;
  private List<Player> players = new ArrayList<>();
}
