package server.api.service.tests;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.ParticipantRepository;
import server.api.services.ParticipantService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private ParticipantService participantService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case to verify the addition of a new participant.
     */
    @Test
    public void testAddParticipant() {
        Participant participant = new Participant();
        when(participantRepository.save(participant)).thenReturn(participant);
        Participant result = participantService.add(participant);
        assertNotNull(result);
        assertEquals(participant, result);
        verify(participantRepository, times(1)).save(participant);
    }

    /**
     * Test case to verify the addition of a new participant fails when participant is null.
     */
    @Test
    public void testAddParticipantWithNullParticipant() {
        Participant participant = null;

        Participant result = participantService.add(participant);

        assertNull(result);
    }

    /**
     * Test case to verify the modification of an existing participant.
     */
    @Test
    public void testUpdateParticipant() {
        long id = 1;
        Participant existingParticipant = new Participant();
        when(participantRepository.existsById(id)).thenReturn(true);
        when(participantRepository.save(existingParticipant)).thenReturn(existingParticipant);
        Participant result = participantService.put(id, existingParticipant);
        assertNotNull(result);
        assertEquals(existingParticipant, result);
        verify(participantRepository, times(1)).existsById(id);
        verify(participantRepository, times(1)).save(existingParticipant);
    }

    /**
     * Test case to verify the deletion of an existing participant.
     */
    @Test
    public void testDeleteParticipant() {
        long id = 1;
        Participant existingParticipant = new Participant();
        when(participantRepository.existsById(id)).thenReturn(true);
        when(participantRepository.findById(id)).thenReturn(Optional.of(existingParticipant));
        Participant result = participantService.delete(id);
        assertNotNull(result);
        assertEquals(existingParticipant, result);
        verify(participantRepository, times(1)).existsById(id);
        verify(participantRepository, times(1)).deleteById(id);
    }
}

