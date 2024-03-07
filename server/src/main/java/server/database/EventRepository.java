package server.database;

import commons.Participant;
import commons.Tag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import commons.Event;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long>{
    /**
     * Query for updating the event
     * @param id
     * @param title
     * @param lastActivityDate
     */
    @Transactional
    @Modifying
    @Query("UPDATE Event SET title = :title, " +
            "lastActivityDate = :lastActivityDate, inviteCode = :inviteCode, participants= :participants, tags = :tags " +
            "WHERE id = :id")
    void modifyEvent(@Param("id") long id, @Param("title") String title, @Param("lastActivityDate") Date lastActivityDate,
                     @Param("inviteCode") String inviteCode, @Param("participants") List<Participant> participants, @Param("tags") List<Tag> tags);

    /**
     * Returns the requested event using the invite code
     * @param inviteCode
     * @return
     */
    @Query("SELECT Event FROM Event WHERE inviteCode = :inviteCode")
    Collection<Event> getByInviteCode(@Param("inviteCode") String inviteCode);

}
