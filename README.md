# Splitty by Team 58

This guide will help the user to have a better understanding of the project and how to use it.

## How to Run the Application

First the server and client running configuration must be setup.

For the server running configuration make sure that it uses java 21.
For the client running configuration, chose Java 21, for the working directory chose the \client folder, and by pressing modify options check Add VM options.
Now in the VM options section add this string 

`--module-path="path\to\javafx\lib" --add-modules=javafx.controls,javafx.fxml`

Make sure to change the module-path to your local javafx library. 

Now that the configurations are set up you can start running the app. First start the server and wait for it be completely started(The admin code will be in the server console if needed).
Now you can start the client and using the app.
## How to Use the Application

When the application is run, a start screen is visible. There, a user can create a new event with a name, or join an event with the code, provided by the email shared by an event creator. Also the recently joined events are visible here.

Language switch is present in the StartScreen and Overview scene of the application, which helps the user to choose between English, Dutch, and Spanish interfaces.
Pressing the download template will download a language template in the download folder of the user.

After creating an event with a name, contact details of the user is asked, after filling and submitting, the overview of an event is shown.

There, the participants in the event can be seen and added(instructions on how participants can be deleted/edited are given when hovering over Participants text box). Expenses can be displayed (either all expenses or expenses for chosen people). Participants are added by pressing the icon.

Users can settle the debt in an event by clicking settle debt.

Users can see the statistics of the event.

Users can add a new expense. When adding expense, user can fill information like who, to who, how much etc. User can also choose a tag for this expense which will be displayed with the expense together. 

Expense can be deleted/edited by double right-clicking on them in the expense table in the overview scene of an event(The Undo option for expenses is available in the right upper when editing an expense).

Users can choose the currency they want to operate with for expense creation.

Users can press money transfer to partially settle a debt.

User can click the pencil and add/edit tags. If a known tag's name is written, it will be in edit mode, otherwise in adding mode.

## Websockets, Long-polling, HCI and other

Websockets are used in the OverviewCtrl, AddExpenseCtrl and OpenDebtCtrl to synchronize the data of an event.
The client side implementation ca be found in `client/src/main/java/client/utils/WebSocketUtils.java`, while the server-side implementation can be seen in 
`server/src/main/java/server/WebsocketConfig.java` and throughout the methods in the event controller using the SimpMessagingTemplate.

Long-polling is used in the AdminEventOverviewCtrl to synchronize the page with new/deleted events. Client side implementation is in `client/src/main/java/client/utils/ServerUtils.java`.
Server-side implementation can be found in the getUpdates method in `server/src/main/java/server/api/controllers/EventController.java`.

Keyboard-shortcuts for each button can be seen when hovering other them with the mouse.

The Undo functionality is implemented for expenses can be used by pressing the undo button when editing an expense.

Admin access is found in the settings page(click the settings button on the start screen).
