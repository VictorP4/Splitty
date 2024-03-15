/**
 * Utility class for interacting with the server.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import commons.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;
import java.util.ArrayList;

public class ServerUtils {

	private static final String SERVER = "http://localhost:8080/";

	/**
	 * Sends invites via email.
	 *
	 * @param requestBody The request body containing email information.
	 * @return The response from the server.
	 */
	public Response sendInvites(EmailRequestBody requestBody) {
		return ClientBuilder.newClient()
				.target(SERVER).path("api/email/invites")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(requestBody, APPLICATION_JSON));
	}

	/**
	 * Adds an event.
	 *
	 * @param event The event to add.
	 * @return The added event.
	 */
	public Event addEvent(Event event) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(event, APPLICATION_JSON), Event.class);
	}

	/**
	 * Retrieves an event by its ID.
	 *
	 * @param id The ID of the event.
	 * @return The event.
	 */
	public Event getEvent(long id) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events/" + id)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get().readEntity(Event.class);
	}

	/**
	 * Deletes an event by its ID.
	 *
	 * @param id The ID of the event to delete.
	 * @return The response from the server.
	 */
	public Response deleteEvent(Long id) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events/" + id)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.delete();
	}

	/**
	 * Retrieves all events.
	 *
	 * @return The list of events.
	 */
	public ArrayList<Event> getAllEvents() {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get().readEntity(new GenericType<ArrayList<Event>>() {
				});
	}

	/**
	 * Updates an event.
	 *
	 * @param event The event to update.
	 * @return The updated event.
	 */
	public Event updateEvent(Event event) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("/api/events/" + event.getId())
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.entity(event, APPLICATION_JSON), Event.class);
	}

	/**
	 * Sends reminders via email.
	 *
	 * @param emailRequestBody The request body containing email information for
	 *                         reminders.
	 * @return The response from the server.
	 */
	public Response sendReminder(EmailRequestBody emailRequestBody) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/email/reminders")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(emailRequestBody, APPLICATION_JSON));
	}

	/**
	 * Adds an expense to an event.
	 *
	 * @param expense The expense to add.
	 * @param id      The ID of the event.
	 * @return The added expense.
	 */
	public Expense addExpense(Expense expense, Long id) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events/" + id + "/expenses")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
	}

	/**
	 * Adds a participant.
	 *
	 * @param participant The participant to add.
	 * @return The added participant.
	 */
	public Participant addParticipant(Participant participant) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/participants")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(participant, APPLICATION_JSON), Participant.class);
	}

	/**
	 * Updates a participant.
	 *
	 * @param participant The participant to update.
	 * @return The updated participant.
	 */
	public Participant updateParticipant(Participant participant) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/participants/" + participant.getId())
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.entity(participant, APPLICATION_JSON), Participant.class);
	}

	/**
	 * Deletes a participant.
	 *
	 * @param participant The participant to delete.
	 * @return The response from the server.
	 */
	public Response deleteParticipant(Participant participant) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/participants/" + participant.getId())
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.delete();
	}

	/**
	 * Fetches the tags from the server for a specific event.
	 *
	 * @param eventId The ID of the event.
	 * @return The list of tags.
	 */
	public List<Tag> getTags(Long eventId) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events/" + eventId)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get().readEntity(Event.class).getTags();
	}

	/**
	 * Adds a tag.
	 *
	 * @param tag The tag to add.
	 * @return The added tag.
	 */
	public Tag addTag(Tag tag) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/tags")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
	}

	public Response removeTag(Tag tag) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events/" + id + "/expenses" + expense.getId())
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.delete();
	}

	public Tag updateTag(Tag tag) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events/" + id + "/expenses" + expense.getId())
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.entity(expense, APPLICATION_JSON), Expense.class);
	}

	public Event getEventbyInviteCode(String inviteCode) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events/inviteCode/" + inviteCode)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get().readEntity(Event.class);
	}

}
