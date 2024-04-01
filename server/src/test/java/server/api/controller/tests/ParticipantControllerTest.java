package server.api.controller.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Participant;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.api.controllers.ParticipantController;
import server.api.services.ParticipantService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ParticipantControllerTest {

    private final ParticipantService participantService = Mockito.mock(ParticipantService.class);
    private final SimpMessagingTemplate smt = Mockito.mock(SimpMessagingTemplate.class); // Mocking SimpMessagingTemplate
    private final ParticipantController participantController = new ParticipantController(participantService, smt);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(participantController).build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAddParticipant() throws Exception {
        Participant participant = new Participant("Ax", "ax@ac.c");
        Mockito.when(participantService.add(Mockito.any(Participant.class))).thenReturn(participant);

        mockMvc.perform(post("/api/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(participant)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(participant.getName()))
                .andExpect(jsonPath("$.email").value(participant.getEmail()));
    }

    @Test
    void testUpdateParticipant() throws Exception {
        long id = 1;
        Participant participant = new Participant("Ax", "ax@ac.c");
        Mockito.when(participantService.put(id, participant)).thenReturn(participant);

        mockMvc.perform(put("/api/participants/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(participant)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(participant.getName()))
                .andExpect(jsonPath("$.email").value(participant.getEmail()));
    }

    @Test
    void testDeleteParticipant() throws Exception {
        long id = 1;
        Participant participant = new Participant("Ax", "ax@ac.c");
        Mockito.when(participantService.delete(id)).thenReturn(participant);

        mockMvc.perform(delete("/api/participants/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(participant.getName()))
                .andExpect(jsonPath("$.email").value(participant.getEmail()));
    }
}
