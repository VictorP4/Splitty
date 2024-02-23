package server.database;
import commons.Expense;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpensesRepository extends JpaRepository<Expense, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Expense SET title = :title, amount = :amount WHERE id = :id")
    void modifyExpense(@Param("id") long id,
                       @Param("title") String title, @Param("amount") double amount);
}
