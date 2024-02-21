package server.database;

import commons.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import commons.Event;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>{
    @Modifying
    @Query(value="UPDATE Event SET participants = ?1 WHERE id= ?2",
    nativeQuery=true)
    void addParticipant(List<Participant> participantList, long id);
}
