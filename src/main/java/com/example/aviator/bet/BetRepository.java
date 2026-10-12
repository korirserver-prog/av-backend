package com.example.aviator.bet;

import com.example.aviator.user.User;
import com.example.aviator.game.GameRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {

    List<Bet> findByUserOrderByCreatedAtDesc(User user);

    List<Bet> findByRound(GameRound round);
}