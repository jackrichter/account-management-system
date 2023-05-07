package com.backend.test;

import com.backend.test.entity.SavingsAccount;
import com.backend.test.entity.Transaction;
import com.backend.test.exception.BalanceExceededException;
import com.backend.test.repository.SavingsAccountRepository;
import com.backend.test.service.AccountService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountManagementSystemApplicationTests {

	@Autowired
	SavingsAccountRepository accountRepo;

	@Autowired
	AccountService accountService;

	@Test
	@Order(1)
	void testCreateSavingsAccount() {
		SavingsAccount savingsAccount = new SavingsAccount();
		savingsAccount.setAccountNumber(999999999L);
		savingsAccount.setFirstName("Alvin");
		savingsAccount.setLastName("Lee");
		savingsAccount.setBalance(1000.00);
		SavingsAccount saved = accountRepo.save(savingsAccount);
		Assertions.assertNotNull(saved);
	}

	@Test
	@Order(2)
	void testCreateAccountWithSameNumberNotAllowed() {SavingsAccount savingsAccount = new SavingsAccount();
		SavingsAccount badAccount = new SavingsAccount();
		Exception exception = Assertions.assertThrows(Exception.class, () -> {
			badAccount.setAccountNumber(999999999L);
			badAccount.setFirstName("Albert");
			badAccount.setLastName("Einstein");
			badAccount.setBalance(5000.00);
			accountService.createAccount(badAccount);
		});

		String actualMessage = exception.getMessage();
		String firstExpectedMessage = "An account with number 999999999 exists already";
		String secondExpectedMessage = "query did not return a unique result";

		Assertions.assertTrue(actualMessage.contains(firstExpectedMessage) || actualMessage.contains(secondExpectedMessage));
	}

	@Test
	@Order(3)
	void testRetrievingLastTenTransactions() {
		List<Transaction> transactions = accountService.getLastTenTransactions();
		Assertions.assertTrue(transactions.size() == 10);
	}

	@Test
	@Order(4)
	void testRetrievingSingleAccount() {
		SavingsAccount account = accountService.getOneAccount(1L);
		Assertions.assertNotNull(account);
	}

	@Test
	@Order(5)
	void testUpdateAccount_withId_1_setNewBalance() {
		Double newBalance = 2435.05;
		SavingsAccount originalAccount = accountService.getOneAccount(1L);
		originalAccount.setBalance(newBalance);
		SavingsAccount updatedAccount = accountService.updateAccount(originalAccount);

		Assertions.assertEquals(newBalance, updatedAccount.getBalance());
	}

	@Test
	@Order(6)
	void testWithdrawMoreMoneyThanInAccount() {
		Double withdraw = 1000000000.0;
		SavingsAccount account = accountService.getOneAccount(1L);
		Exception exception = Assertions.assertThrows(Exception.class, () -> {
			accountService.withdrawAmount(account.getId(), withdraw);
		});

		String actualMessage = exception.getMessage();
		String expectedMessage = "Balance amount exceeded";

		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	@Order(7)
	void testWithdrawal() throws BalanceExceededException {
		double amount = 100.0;
		double previousBalance = accountService.getOneAccount(1L).getBalance();
		double newBalance = accountService.withdrawAmount(1l, amount).getBalance();

		Assertions.assertEquals(newBalance, previousBalance - amount);
	}

	@Test
	@Order(8)
	void testDeposit() {
		double amount = 150.0;
		double previousBalance = accountService.getOneAccount(1L).getBalance();
		double newBalance = accountService.depositAmount(1l, amount).getBalance();

		Assertions.assertEquals(newBalance, previousBalance + amount);
	}
}
