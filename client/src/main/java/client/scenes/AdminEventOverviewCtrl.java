package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.util.List;

public class AdminEventOverviewCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private List<Event> events;
    @FXML
    public TableView<Event> eventsTable;
    @FXML
    public VBox container;


    @Inject
    public AdminEventOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void refresh(){
        events = server.getAllEvents();
        displayEvents();
    }

    public void displayEvents(){
        eventsTable.getItems().clear();
        eventsTable.getColumns().clear();

        TableColumn<Event, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Event, String> creationDateColumn = new TableColumn<>("Creation Date");
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        TableColumn<Event, String> lastActivityColumn = new TableColumn<>("Last Activity");
        lastActivityColumn.setCellValueFactory(new PropertyValueFactory<>("lastActivityDate"));

        TableColumn<Event, Button> deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setCellValueFactory(param -> {
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> {
                Event selectedEvent = param.getValue();
                server.deleteEvent(selectedEvent.getId());
                events.remove(selectedEvent);
                displayEvents();
            });
            return new SimpleObjectProperty<>(deleteButton);
        });

        eventsTable.getColumns().addAll(titleColumn, creationDateColumn, lastActivityColumn, deleteColumn);

        eventsTable.getItems().addAll(events);

        eventsTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        deleteColumn.setPrefWidth(60);
        titleColumn.prefWidthProperty().bind(eventsTable.widthProperty()
                .subtract(creationDateColumn.widthProperty())
                .subtract(lastActivityColumn.widthProperty())
                .subtract(deleteColumn.widthProperty())
        );

        container.setFillWidth(true);

        eventsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
                    mainCtrl.showEventOverview(selectedEvent);
                }
            }
        });

    }

    @FXML
    public void showStartScreen(){
        mainCtrl.showStartScreen();
    }
}
