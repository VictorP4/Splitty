package client.scenes;

import client.utils.ServerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Inject;
import commons.Event;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;


public class AdminEventOverviewCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ObservableList<Event> events;
    @FXML
    public TableView<Event> eventsTable;
    @FXML
    public VBox container;
    @FXML
    public AnchorPane ap;
    @FXML
    private FileChooser fc;
    @FXML
    private Button importJSON;
    @FXML
    private Button ok;


    @Inject
    public AdminEventOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void refresh(){
        events = FXCollections.observableList(server.getAllEvents());
        displayEvents();
    }

    public void initialize() {
        ap.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                showStartScreen();
            }
        });
        server.registerForUpdates(event -> {
            if(events.stream().map(x -> x.getId()).anyMatch(x -> x.equals(event.getId()))){
                events.removeIf(x -> x.getId().equals(event.getId()));
                events.add(event);
            }
            else{
                events.add(event);
            }

        });
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
                var alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("Are you sure you want to delete this event?");
                alert.showAndWait().ifPresent((response)->{
                    if(response == ButtonType.OK){
                        server.deleteEvent(selectedEvent.getId());
                        events.remove(selectedEvent);
                    }
                });
                refresh();
            });
            return new SimpleObjectProperty<>(deleteButton);
        });

        TableColumn<Event, Button> backupColumn = new TableColumn<>("Backup");
        backupColumn.setCellValueFactory(param ->{
            Button backupButton = new Button("Backup");
            backupButton.setOnAction(event -> {
                Event selectedEvent = param.getValue();
                try{
                    Writer writer = new BufferedWriter(new FileWriter("event " + selectedEvent.getTitle() + ".json"));
                    writer.write(server.getEventJSON(selectedEvent.getId()));
                    writer.flush(); writer.close();
                    popup("Backup created");
                }
                catch (Exception e){
                    e.printStackTrace();
                    errorPopup(e.getMessage());
                }

            });
            return new SimpleObjectProperty<>(backupButton);
        });

        eventsTable.getColumns().addAll(titleColumn, creationDateColumn, lastActivityColumn, deleteColumn, backupColumn);
        eventsTable.setItems(events);

        eventsTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        deleteColumn.setPrefWidth(60);
        titleColumn.prefWidthProperty().bind(eventsTable.widthProperty()
                .subtract(creationDateColumn.widthProperty())
                .subtract(lastActivityColumn.widthProperty())
                .subtract(deleteColumn.widthProperty())
                .subtract(backupColumn.widthProperty())
        );

        container.setFillWidth(true);

        eventsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
                    if(selectedEvent!=null) mainCtrl.showEventOverviewFromAdmin(selectedEvent);
                }
            }
        });

    }

    @FXML
    public void showStartScreen(){
        mainCtrl.showStartScreen();
    }

    /**
     * Pops up when an error occurs
     * @param message error message
     */
    private void errorPopup(String message) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Popup that lets admin know backup was created successfully
     * @param message message to print on screen
     */
    private void popup(String message) {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.showAndWait().ifPresent((response)->{
            if(response == ButtonType.OK){

            }
        });
    }

    /**
     * stopping the thread for long polling
     */
    public void stop(){
        server.stop();
    }


    /**
     * creates a FileChooser window for an admin to be able to pick a file to import
     *  (a json version of an event), imports the event
     */
    public void importJSON(){

        ObjectMapper map = new ObjectMapper();
        map.registerModule(new JavaTimeModule());

        Stage stage = new Stage();
        stage.setTitle("FileChooser");
        Label label = new Label("nothing selected");
        fc = new FileChooser();
        fc.setInitialDirectory(new File("C:\\"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json")
                , new FileChooser.ExtensionFilter("All files", "*.*")
        );
        Button button = new Button("Select file");
        this.ok = new Button("Import");
        ok.setVisible(false);
        button.setOnAction(actionEvent -> {
            File file = fc.showOpenDialog(stage);
            label.setText(file.getAbsolutePath() + " selected");

            ok.setVisible(true);
            ok.setOnAction(action -> {
                Event event = null;
                try {
                    event = map.readValue(file, Event.class);
                }
                catch (Exception e){
                    e.printStackTrace();
                    errorPopup(e.getMessage());
                }
                if(event != null){
                    server.addEventJSON(event);
                }
                stage.close();
            });
        });

        VBox vbox = new VBox(30, label, button, ok);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}
