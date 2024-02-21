package server.database;

import commons.Expense;
import commons.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import commons.Event;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>{
    /**
     * Query for updating the event
     * @param participantList
     * @param id
     * @param title
     * @param lastActivityDate
     * @param expenses
     */
    @Modifying
    @Query("UPDATE Event SET participants = :participantList, title = :title, " +
            "lastActivityDate = :lastActivityDate, expenses= :expenses  " +
            "WHERE id = :id")
    void modifyEvent(@Param("participantList") List<Participant> participantList, @Param("id") long id, @Param("title") String title,
                     @Param("lastActivityDate") Date lastActivityDate, @Param("expenses") List<Expense> expenses);

}
