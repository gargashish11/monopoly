package com.ashish.monopoly.service;

import com.ashish.monopoly.model.Game;
import com.ashish.monopoly.model.GamePlayer;
import com.ashish.monopoly.model.Player;
import com.ashish.monopoly.model.Transaction;
import com.ashish.monopoly.repository.TransactionRepository;
import com.ashish.monopoly.service.exception.InsufficientBalanceException;
import com.ashish.monopoly.service.exception.NegativeTransactionAmountException;
import com.ashish.monopoly.service.exception.SameAccountTransaction;
import com.ashish.monopoly.service.impl.CreditDebit;
import com.ashish.monopoly.service.impl.DefaultTransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultTransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private GamePlayerService gamePlayerService;

    @InjectMocks
    private DefaultTransactionService transactionService;

    private Game game;
    private Player payer;
    private Player payee;

    @BeforeEach
    public void setUp() {
        game = new Game();
        game.setId(1);

        payer = new Player();
        payer.setId(10);
        payer.setName("Payer");

        payee = new Player();
        payee.setId(20);
        payee.setName("Payee");
    }

    @Test
    public void testNullTransactionThrows() {
        assertThrows(IllegalArgumentException.class, () -> transactionService.save(null));
    }

    @Test
    public void testNullAmountThrows() {
        Transaction tx = new Transaction();
        tx.setGame(game);
        tx.setPayer(payer);
        tx.setPayee(payee);
        tx.setAmount(null);

        assertThrows(NegativeTransactionAmountException.class, () -> transactionService.save(tx));
    }

    @Test
    public void testNegativeAmountThrowsException() {
        Transaction tx = new Transaction();
        tx.setGame(game);
        tx.setPayer(payer);
        tx.setPayee(payee);
        tx.setAmount(-50);

        assertThrows(NegativeTransactionAmountException.class, () -> transactionService.save(tx));
    }

    @Test
    public void testZeroAmountThrowsException() {
        Transaction tx = new Transaction();
        tx.setGame(game);
        tx.setPayer(payer);
        tx.setPayee(payee);
        tx.setAmount(0);

        assertThrows(NegativeTransactionAmountException.class, () -> transactionService.save(tx));
    }

    @Test
    public void testNullGameOrGameIdThrows() {
        Transaction tx1 = new Transaction();
        tx1.setAmount(100);
        tx1.setGame(null);
        tx1.setPayer(payer);
        tx1.setPayee(payee);
        assertThrows(IllegalArgumentException.class, () -> transactionService.save(tx1));

        Transaction tx2 = new Transaction();
        tx2.setAmount(100);
        tx2.setGame(new Game()); // null ID
        tx2.setPayer(payer);
        tx2.setPayee(payee);
        assertThrows(IllegalArgumentException.class, () -> transactionService.save(tx2));
    }

    @Test
    public void testNullPayerOrPayeeThrows() {
        Transaction tx1 = new Transaction();
        tx1.setGame(game);
        tx1.setAmount(100);
        tx1.setPayer(null);
        tx1.setPayee(payee);
        assertThrows(IllegalArgumentException.class, () -> transactionService.save(tx1));

        Transaction tx2 = new Transaction();
        tx2.setGame(game);
        tx2.setAmount(100);
        tx2.setPayer(payer);
        tx2.setPayee(null);
        assertThrows(IllegalArgumentException.class, () -> transactionService.save(tx2));
    }

    @Test
    public void testSameAccountByIdThrows() {
        Transaction tx = new Transaction();
        tx.setGame(game);
        tx.setPayer(payer);
        Player sameIdPlayer = new Player();
        sameIdPlayer.setId(10);
        sameIdPlayer.setName("DifferentName");
        tx.setPayee(sameIdPlayer);
        tx.setAmount(100);

        assertThrows(SameAccountTransaction.class, () -> transactionService.save(tx));
    }

    @Test
    public void testSameAccountByNameThrows() {
        Transaction tx = new Transaction();
        tx.setGame(game);
        Player p1 = new Player(); p1.setId(null); p1.setName("Alice");
        Player p2 = new Player(); p2.setId(null); p2.setName("alice");
        tx.setPayer(p1);
        tx.setPayee(p2);
        tx.setAmount(100);

        assertThrows(SameAccountTransaction.class, () -> transactionService.save(tx));
    }

    @Test
    public void testDifferentAccountWithMixedNullIdsAndNames() {
        Transaction tx = new Transaction();
        tx.setGame(game);
        Player p1 = new Player(); p1.setId(10); p1.setName(null);
        Player p2 = new Player(); p2.setId(null); p2.setName("Bob");
        tx.setPayer(p1);
        tx.setPayee(p2);
        tx.setAmount(100);

        GamePlayer payerGp = new GamePlayer(); payerGp.setBalance(500);
        GamePlayer payeeGp = new GamePlayer(); payeeGp.setBalance(300);

        when(gamePlayerService.findByGame_IdAndPlayer(1, p1)).thenReturn(payerGp);
        when(gamePlayerService.findByGame_IdAndPlayer(1, p2)).thenReturn(payeeGp);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.save(tx);
        assertTrue(result.getIsSuccess());
    }

    @Test
    public void testPayerNameNotNullPayeeNameNull() {
        Transaction tx = new Transaction();
        tx.setGame(game);
        Player p1 = new Player(); p1.setId(10); p1.setName("Alice");
        Player p2 = new Player(); p2.setId(20); p2.setName(null);
        tx.setPayer(p1);
        tx.setPayee(p2);
        tx.setAmount(100);

        GamePlayer payerGp = new GamePlayer(); payerGp.setBalance(500);
        GamePlayer payeeGp = new GamePlayer(); payeeGp.setBalance(300);

        when(gamePlayerService.findByGame_IdAndPlayer(1, p1)).thenReturn(payerGp);
        when(gamePlayerService.findByGame_IdAndPlayer(1, p2)).thenReturn(payeeGp);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.save(tx);
        assertTrue(result.getIsSuccess());
    }

    @Test
    public void testPayerNotInGameThrows() {
        Transaction tx = new Transaction();
        tx.setGame(game);
        tx.setPayer(payer);
        tx.setPayee(payee);
        tx.setAmount(100);

        when(gamePlayerService.findByGame_IdAndPlayer(1, payer)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> transactionService.save(tx));
    }

    @Test
    public void testPayeeNotInGameThrows() {
        Transaction tx = new Transaction();
        tx.setGame(game);
        tx.setPayer(payer);
        tx.setPayee(payee);
        tx.setAmount(100);

        when(gamePlayerService.findByGame_IdAndPlayer(1, payer)).thenReturn(new GamePlayer());
        when(gamePlayerService.findByGame_IdAndPlayer(1, payee)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> transactionService.save(tx));
    }

    @Test
    public void testSuccessfulTransactionDebitsPayerFirstThenCreditsPayee() {
        Transaction tx = new Transaction();
        tx.setGame(game);
        tx.setPayer(payer);
        tx.setPayee(payee);
        tx.setAmount(100);

        GamePlayer payerGp = new GamePlayer();
        payerGp.setBalance(500);

        GamePlayer payeeGp = new GamePlayer();
        payeeGp.setBalance(300);

        when(gamePlayerService.findByGame_IdAndPlayer(1, payer)).thenReturn(payerGp);
        when(gamePlayerService.findByGame_IdAndPlayer(1, payee)).thenReturn(payeeGp);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.save(tx);

        assertTrue(result.getIsSuccess());
        var inOrder = inOrder(gamePlayerService);
        inOrder.verify(gamePlayerService).updateBalance(payerGp, 100, CreditDebit.DEBIT);
        inOrder.verify(gamePlayerService).updateBalance(payeeGp, 100, CreditDebit.CREDIT);
    }

    @Test
    public void testCreditFailureThrowsAndAbortsTransaction() {
        Transaction tx = new Transaction();
        tx.setGame(game);
        tx.setPayer(payer);
        tx.setPayee(payee);
        tx.setAmount(100);

        GamePlayer payerGp = new GamePlayer();
        GamePlayer payeeGp = new GamePlayer();

        when(gamePlayerService.findByGame_IdAndPlayer(1, payer)).thenReturn(payerGp);
        when(gamePlayerService.findByGame_IdAndPlayer(1, payee)).thenReturn(payeeGp);
        doThrow(new RuntimeException("Database error during credit"))
                .when(gamePlayerService).updateBalance(payeeGp, 100, CreditDebit.CREDIT);

        assertThrows(RuntimeException.class, () -> transactionService.save(tx));
    }

    @Test
    public void testNameOnlyPlayersResolveAndSetManagedEntities() {
        Transaction tx = new Transaction();
        tx.setGame(game);

        Player nameOnlyPayer = new Player();
        nameOnlyPayer.setId(null);
        nameOnlyPayer.setName("Alice");

        Player nameOnlyPayee = new Player();
        nameOnlyPayee.setId(null);
        nameOnlyPayee.setName("Bob");

        tx.setPayer(nameOnlyPayer);
        tx.setPayee(nameOnlyPayee);
        tx.setAmount(100);

        Player resolvedPayer = new Player();
        resolvedPayer.setId(101);
        resolvedPayer.setName("Alice");

        Player resolvedPayee = new Player();
        resolvedPayee.setId(202);
        resolvedPayee.setName("Bob");

        GamePlayer payerGp = new GamePlayer();
        payerGp.setId(1);
        payerGp.setPlayer(resolvedPayer);

        GamePlayer payeeGp = new GamePlayer();
        payeeGp.setId(2);
        payeeGp.setPlayer(resolvedPayee);

        when(gamePlayerService.findByGame_IdAndPlayer(1, nameOnlyPayer)).thenReturn(payerGp);
        when(gamePlayerService.findByGame_IdAndPlayer(1, nameOnlyPayee)).thenReturn(payeeGp);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.save(tx);
        assertTrue(result.getIsSuccess());
        assertEquals(resolvedPayer, result.getPayer());
        assertEquals(resolvedPayee, result.getPayee());
    }

    @Test
    public void testSameAccountWhenPayerAndPayeeResolveToSameGamePlayer() {
        Transaction tx = new Transaction();
        tx.setGame(game);

        Player p1 = new Player();
        p1.setId(10);
        p1.setName("Alice");

        Player p2 = new Player();
        p2.setId(20);
        p2.setName("Bob");

        tx.setPayer(p1);
        tx.setPayee(p2);
        tx.setAmount(100);

        GamePlayer sameGp = new GamePlayer();
        sameGp.setId(5);

        when(gamePlayerService.findByGame_IdAndPlayer(1, p1)).thenReturn(sameGp);
        when(gamePlayerService.findByGame_IdAndPlayer(1, p2)).thenReturn(sameGp);

        assertThrows(SameAccountTransaction.class, () -> transactionService.save(tx));
    }

    @Test
    public void testInsufficientBalanceDoesNotCreditPayee() {
        Transaction tx = new Transaction();
        tx.setGame(game);
        tx.setPayer(payer);
        tx.setPayee(payee);
        tx.setAmount(500);

        GamePlayer payerGp = new GamePlayer();
        payerGp.setBalance(100);

        GamePlayer payeeGp = new GamePlayer();
        payeeGp.setBalance(300);

        when(gamePlayerService.findByGame_IdAndPlayer(1, payer)).thenReturn(payerGp);
        when(gamePlayerService.findByGame_IdAndPlayer(1, payee)).thenReturn(payeeGp);
        doThrow(new InsufficientBalanceException("Insufficient Balance"))
                .when(gamePlayerService).updateBalance(payerGp, 500, CreditDebit.DEBIT);

        assertThrows(InsufficientBalanceException.class, () -> transactionService.save(tx));

        verify(gamePlayerService, never()).updateBalance(eq(payeeGp), anyInt(), eq(CreditDebit.CREDIT));
    }

    @Test
    public void testDeleteByIdAndFindById() {
        doNothing().when(transactionRepository).deleteById(1);
        transactionService.deleteById(1);
        verify(transactionRepository).deleteById(1);

        Transaction tx = new Transaction();
        tx.setId(2);
        when(transactionRepository.findById(2)).thenReturn(Optional.of(tx));
        assertEquals(tx, transactionService.findById(2));

        when(transactionRepository.findById(3)).thenReturn(Optional.empty());
        assertNull(transactionService.findById(3));
    }

    @Test
    public void testFilter() {
        // null, empty, maxSize <= 0
        assertTrue(transactionService.filter(null, 5).isEmpty());
        assertTrue(transactionService.filter(Collections.emptySet(), 5).isEmpty());
        assertTrue(transactionService.filter(Set.of(new Transaction()), 0).isEmpty());
        assertTrue(transactionService.filter(Set.of(new Transaction()), -1).isEmpty());

        Transaction tx1 = new Transaction();
        tx1.setId(1);
        tx1.setCreatedDate(new Date(1000));

        Transaction tx2 = new Transaction();
        tx2.setId(2);
        tx2.setCreatedDate(new Date(3000));

        Transaction txNullDate = new Transaction();
        txNullDate.setId(3);
        txNullDate.setCreatedDate(null);

        Set<Transaction> set = new LinkedHashSet<>(List.of(tx1, tx2, txNullDate));
        Set<Transaction> filtered = transactionService.filter(set, 2);

        assertEquals(2, filtered.size());
        Iterator<Transaction> it = filtered.iterator();
        assertEquals(2, it.next().getId()); // newest date first
        assertEquals(1, it.next().getId());
    }
}
