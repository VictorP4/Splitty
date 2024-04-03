package client.scenes;

import client.Main;
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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;


public class AdminEventOverviewCtrl implements Main.UpdatableUI {
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
    public FileChooser fc;
    @FXML
    private Button importJSON;
    @FXML
    private Button ok;
    @FXML
    private Button home;
    @FXML
    private Text eventOverview;


    @Inject
    public AdminEventOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Refreshes the scene
     */
    public void refresh(){
        events = FXCollections.observableList(server.getAllEvents());
        displayEvents();
    }

    /**
     * Initializes the scene
     */
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
        setInstructions();
    }

    /**
     * Creates the table that displays all events from the db, with the backup & delete buttons
     */
    public void displayEvents(){
        eventsTable.getItems().clear();
        eventsTable.getColumns().clear();

        TableColumn<Event, String> titleColumn = new TableColumn<>(Main.getLocalizedString("title"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        //titleColumn.setStyle("-fx-border-color: #485a5c; -fx-background-color: ffeaed");

        TableColumn<Event, String> creationDateColumn = new TableColumn<>(Main.getLocalizedString("creationDate"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        TableColumn<Event, String> lastActivityColumn = new TableColumn<>(Main.getLocalizedString("lastActivityDate"));
        lastActivityColumn.setCellValueFactory(new PropertyValueFactory<>("lastActivityDate"));

        TableColumn<Event, Button> deleteColumn = new TableColumn<>(Main.getLocalizedString("delete"));
        deleteColumn.setCellValueFactory(param -> {
            Button deleteButton = new Button(Main.getLocalizedString("delete"));
            deleteButton.setStyle("-fx-background-color: #485a5c; -fx-border-color: 000000; -fx-border-radius: 4; " +
                    "-fx-border-style: solid; -fx-text-fill: white; -fx-font-style: bold");
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

        TableColumn<Event, Button> backupColumn = new TableColumn<>(Main.getLocalizedString("backup"));
        backupColumn.setCellValueFactory(param ->{
            Button backupButton = new Button(Main.getLocalizedString("backup"));
            backupButton.setStyle("-fx-background-color: #485a5c; -fx-border-color: 000000; -fx-border-radius: 4; " +
                    "-fx-border-style: solid; -fx-text-fill: white; -fx-font-style: bold");
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
        Label label = new Label(Main.getLocalizedString("nothingSelected"));
        fc = new FileChooser();
        fc.setInitialDirectory(new File("C:\\"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json")
                , new FileChooser.ExtensionFilter("All files", "*.*")
        );
        Button button = new Button(Main.getLocalizedString("selectFile"));
        this.ok = new Button(Main.getLocalizedString("import"));
        ok.setVisible(false);
        button.setOnAction(actionEvent -> {
            File file = fc.showOpenDialog(stage);
            label.setText(file.getAbsolutePath() + " "+Main.getLocalizedString("selected"));

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

    /**
     * updating the language
     */
    @Override
    public void updateUI() {
        importJSON.setText(Main.getLocalizedString("importBackup"));
        home.setText(Main.getLocalizedString("home"));
        eventOverview.setText(Main.getLocalizedString("eventOverview"));
    }

    /**
     * Sets the instruction popups for shortcuts.
     */
    public void setInstructions() {
        mainCtrl.instructionsPopup(new Label(" press ESC to go \n to start screen "), this.home);
    }
}
