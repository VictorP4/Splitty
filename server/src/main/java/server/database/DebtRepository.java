package server.database;
import commons.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import commons.Debt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DebtRepository extends JpaRepository<Debt, Long>  {
    @Query("SELECT * FROM DEBT WHERE debtFOR = :event")
    List<Debt> getDebtsByEvent(@Param("event") Event event);
}
