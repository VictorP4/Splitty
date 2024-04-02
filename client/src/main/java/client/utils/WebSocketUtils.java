package client.utils;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class WebSocketUtils {
    private StompSession session;
    private List<Consumer<Event>> eventListener;
    private List<Consumer<Participant>> participantListener;
    private List<Consumer<Expense>> expenseListener;
    private Runnable serverListener;

    /**
     * constructor for the websocket utils
     */
    public WebSocketUtils() {
        eventListener = new ArrayList<>();
        participantListener = new ArrayList<>();
        expenseListener = new ArrayList<>();
    }

    /**
     * adds event listener for updates
     * @param listener event listener
     */
    public void addEventListener(Consumer<Event> listener){
        eventListener.add(listener);
    }

    /**
     * adds participant listener for deletes
     * @param listener participant listener
     */
    public void addParticipantListener(Consumer<Participant> listener){
        participantListener.add(listener);
    }

    /**
     * adds expense listener for deletes
     * @param listener expense listener
     */
    public void addExpenseListener(Consumer<Expense> listener){
        expenseListener.add(listener);
    }

    /**
     * Creates stomp connection and registers for updates
     * @param url the server url
     */
    public void connect(String url){
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            this.session = stomp.connect(url, new StompSessionHandlerAdapter() {
                public void handleTransportError(StompSession session, Throwable exception) {
                    if(!session.isConnected()){
                        serverListener.run();
                    }
                }
            }).get();
            registerForUpdates("/topic/events",Event.class, event ->{
                eventListener.forEach(listener ->listener.accept(event));
            });
            registerForUpdates("/topic/participants", Participant.class, participant -> {
                participantListener.forEach(listener -> listener.accept(participant));
            });
            registerForUpdates("/topic/expenses", Expense.class, expense ->{
                expenseListener.forEach((listener -> listener.accept(expense)));
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * registers to the given destination for updates of the specified type
     * @param dest destination
     * @param type type of payload
     * @param action consumer faction for the received payload
     * @param <T> type of payload
     */
    private <T> void registerForUpdates(String dest, Class<T> type, Consumer<T> action) {
        session.subscribe(dest, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                action.accept((T)payload);
            }
        });
    }
    public void disconnect(){
        this.session.disconnect();
    }
    public void addServerListener(Runnable serverListener){
        this.serverListener = serverListener;
    }
}
