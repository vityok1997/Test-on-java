/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pti.myatm;

import org.junit.Test;
import org.mockito.InOrder;
import ua.pti.myatm.Exceptions.CardIsBlockedException;
import ua.pti.myatm.Exceptions.NoCardInsertedException;
import ua.pti.myatm.Exceptions.NotEnoughMoneyInATMException;
import ua.pti.myatm.Exceptions.NotEnoughMoneyInAccountException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
/**
 *
 * @author andrii
 */
public class ATMTest {

    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_IncorrectSetMoneyInATM_IllegalArgumentException(){
        new ATM(-10);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_ZeroSetMoneyInATM(){
        new ATM(0);
    }

    @Test
    public void testConstructor_CorrectSetMoneyInATM(){
        ATM atm = new ATM(10);
        assertEquals(10, atm.getMoneyInATM(),0.0);
    }

    @Test (expected = NoCardInsertedException.class)
    public void testValidateCard_CardIsNull_NoCardInsertedException(){
        new ATM(1).validateCard(null, 7777);
    }

    @Test(expected = CardIsBlockedException.class)
    public void testValidateCard_CardIsBlocked(){
        Card card = mock(Card.class);

        when(card.isBlocked()).thenReturn(Boolean.TRUE);
        when(card.checkPin(1111)).thenReturn(Boolean.TRUE);

        assertFalse(new ATM(1).validateCard(card, 1111));
    }

    @Test
    public void testValidateCard_IncorrectPinCode(){
        Card card = mock(Card.class);

        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(1111)).thenReturn(Boolean.FALSE);

        assertFalse(new ATM(1).validateCard(card, 1111));
    }

    @Test(expected = CardIsBlockedException.class)
    public void testValidateCard_CardIsBlockedAndIncorrectPinCode(){
        Card card = mock(Card.class);

        when(card.isBlocked()).thenReturn(Boolean.TRUE);
        when(card.checkPin(1111)).thenReturn(Boolean.FALSE);

        assertFalse(new ATM(1).validateCard(card, 1111));
    }

    @Test
    public void testValidateCard_Correct(){
        Card card = mock(Card.class);

        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(7777)).thenReturn(Boolean.TRUE);

        assertTrue(new ATM(1).validateCard(card, 7777));
    }

/*
    @Test(expected = CardIsBlockedException.class)
    public void testCheckCard_CardIsBlocked(){
        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(Boolean.TRUE);

        new ATM(10).checkCard();
    }

    @Test (expected = NoCardInsertedException.class)
    public void testCheckCard_NoCardInserted(){
        Card card = mock(Card.class);

        new ATM(10).checkCard(card);
    }

    @Test
    public void testCheckCard_Correct(){
        Card card = mock(Card.class);

        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(7777)).thenReturn(Boolean.TRUE);

        ATM atm = new ATM(1);

        atm.validateCard(card, 7777);

        atm.checkCard(card);
    }
*/
    @Test(expected = CardIsBlockedException.class)
    public void testCheckBalance_CardIsBlocked(){
        Card card = mock(Card.class);
        Account account = mock(Account.class);

        when(card.isBlocked()).thenReturn(Boolean.TRUE);
        when(card.checkPin(7777)).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(21.0);

        ATM atm = new ATM(10);

        atm.checkBalance(card, 7777);
    }

    @Test
    public void testCheckBalance_Correct(){
        ATM atm = new ATM(1);

        Card card = mock(Card.class);
        Account account = mock(Account.class);

        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(7777)).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(21.0);

        atm.validateCard(card, 7777);

        assertEquals(21.0, atm.checkBalance(card, 7777), 0.001);
    }

    @Test(expected = NotEnoughMoneyInAccountException.class)
    public void testIsEnoughMoneyInAccount_IsNotEnoughMoney_NotEnoughMoneyInAccountException(){
        ATM atm = new ATM(100);
        double amount = 100;

        Card card = mock(Card.class);
        Account account = mock(Account.class);

        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(7777)).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(1d);

        atm.validateCard(card, 7777);
        atm.isEnoughMoneyInAccount(amount);
    }

    @Test
    public void testIsEnoughMoneyInAccount_Correct(){
        ATM atm = new ATM(100);
        double amount = 100;

        Card card = mock(Card.class);
        Account account = mock(Account.class);

        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(7777)).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(100d);

        atm.validateCard(card, 7777);
        atm.isEnoughMoneyInAccount(amount);
    }

    @Test(expected = NotEnoughMoneyInATMException.class)
    public void testIsEnoughMoneyInATM_IsNotEnoughMoney_NotEnoughMoneyInATMException(){
        ATM atm = new ATM(1);
        double amount = 100;

        atm.isEnoughMoneyInATM(amount);
    }

    @Test
    public void testIsEnoughMoneyInATM_Correct(){
        ATM atm = new ATM(100);
        double amount = 100;

        atm.isEnoughMoneyInATM(amount);
    }

    @Test(expected = NoCardInsertedException.class)
    public void testGetCash_CardIsNotValidated(){
        ATM atm = new ATM(1000);
        double amount = 1000;


        atm.getCash(amount, 1111, null);
    }


/*    @Test
    public void testGetCash_IncorrectPinCode(){
        ATM atm = new ATM(1000);
        double amount = 1000;

        Card card = mock(Card.class);
        Account account = mock(Account.class);

        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(7777)).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(100d);

        atm.validateCard(card, 7777);
        assertEquals(100d, atm.getCash(amount, 1234, card),0.0);
    }*/

    @Test(expected = NotEnoughMoneyInAccountException.class)
    public void testGetCash_NotEnoughMoneyInAccount_NotEnoughMoneyInAccountException(){
        ATM atm = new ATM(1000);
        double amount = 1000;

        Card card = mock(Card.class);
        Account account = mock(Account.class);

        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(7777)).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(999d).thenReturn(0d);
        when(account.withdrow(amount)).thenReturn(1000d);

        atm.validateCard(card, 7777);
        atm.getCash(amount,7777, card);
    }

    @Test(expected = NotEnoughMoneyInATMException.class)
    public void testGetCash_NotEnoughMoneyInATM_NotEnoughMoneyInAccountException(){
        ATM atm = new ATM(99);
        double amount = 100;

        Card card = mock(Card.class);
        Account account = mock(Account.class);

        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(7777)).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(1000d).thenReturn(0d);
        when(account.withdrow(amount)).thenReturn(1000d);

        atm.validateCard(card, 7777);
        atm.getCash(amount,7777, card);
    }

    @Test
    public void testGetCash_CorrectOrderCall(){
        ATM atm = new ATM(1000);
        double amount = 1000;

        Card card = mock(Card.class);
        Account account = mock(Account.class);

        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(7777)).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(1000d).thenReturn(0d);
        when(account.withdrow(amount)).thenReturn(1000d);

        InOrder inOrder = inOrder(card, account);

        atm.validateCard(card, 7777);
        assertEquals(0, atm.getCash(amount,7777, card), 0.0);

        inOrder.verify(card).isBlocked();
        inOrder.verify(card).checkPin(7777);
        inOrder.verify(card).getAccount();
        inOrder.verify(account).getBalance();
    }

    @Test
    public void testGetCash_CorrectOrderGetBalanceBeforeWithdraw(){
        ATM atm = new ATM(1000);
        double amount = 1000;

        Card card = mock(Card.class);
        Account account = mock(Account.class);

        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(7777)).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(1000d).thenReturn(0d);
        when(account.withdrow(amount)).thenReturn(1000d);

        InOrder inOrder = inOrder(account);

        atm.validateCard(card, 7777);
        assertEquals(0, atm.getCash(amount,7777, card), 0.0);

        inOrder.verify(account).getBalance();
        inOrder.verify(account).withdrow(amount);
    }

    @Test
    public void testGetCash_Correct(){
        ATM atm = new ATM(1000);
        double amount = 1000;

        Card card = mock(Card.class);
        Account account = mock(Account.class);

        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(7777)).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(1000d).thenReturn(0d);
        when(account.withdrow(amount)).thenReturn(1000d);

        atm.validateCard(card, 7777);
        assertEquals(0, atm.getCash(amount,7777, card), 0.0);
    }

    @Test
    public void testGetCash_CorrectMoneyTransactionInATM(){
        ATM atm = new ATM(120);
        double amount = 100;

        Card card = mock(Card.class);
        Account account = mock(Account.class);

        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.checkPin(7777)).thenReturn(Boolean.TRUE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(130d).thenReturn(30d);
        when(account.withdrow(amount)).thenReturn(100d);

        atm.validateCard(card, 7777);
        atm.getCash(amount, 7777, card);

        assertEquals(20, atm.getMoneyInATM(), 0.0);
    }



}