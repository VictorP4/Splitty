# Splitty by Team 58

This guide will help the user to have a better understanding of the project and how to use it.
Splitty is an expense tracking & management application. You can use it to create events and track expenses in them.

## How to Run the Application

When starting the server you will see an admin password in the terminal which can be used to access the admin page on the client.

### Making a configuration
First the server and client running configuration must be setup.

For the server running configuration make sure that it uses java 21.
For the client running configuration, chose Java 21, for the working directory chose the \client folder, and by pressing modify options check Add VM options.
Now in the VM options section add this string

    --module-path="path\to\javafx\lib" --add-modules=javafx.controls,javafx.fxml

Make sure to change the module-path to your local javafx library.


It is also possible to create a gradle configuration. In the VM options section add the following

    client:run


### Through the terminal

To start the server go to the project path on your terminal and write the following commands:

Linux and Mac:

    ./gradlew bootRun

Windows:

    ./gradlew.bat bootRun

Once the server is completely started up, you can use the following command to start the client

Linux and Mac:

    ./gradlew run

Windows:

    ./gradlew.bat run


Now you can start the client and using the app.

## How to Use the Application

When the application is run, a start screen is visible. Here, a user can create a new event with a name, or join an event with an invite code which is provided by the email shared by an event creator. Furthermore, your most recently accessed events will be visible here.

By hovering over button and text, you can find that there are some shortcuts you can use to make the navigation easier.

After creating an event with a name, contact details of the user is asked, after filling and submitting, the overview of an event is shown. the participants in the event can be seen and added (hover over the Participants text box for further instructions). Participants are added by pressing the icon.

### Detailed Expenses and Settling Debts

Expenses can be displayed (either all expenses or expenses for chosen people). Expenses have a title, amount, who paid, involved participants and tags. You can sort the expenses on these things by pressing the column you'd like to sort it on.  
Users can also add a new expense. When adding expense, user can fill information like who, to who, how much etc. User can also choose a tag for this expense which will be displayed with the expense together.
Expense can be deleted/edited by double right-clicking on them in the expense table in the overview scene of an event (The Undo option for expenses is available in the right upper when editing an expense).

To make it easy for you, we made some buttons to select everyone quickly. You can settle a debt, by pressing the settle debts button. Here you can see exactly who owes who money, how much they owe and their bank information. You can either settle the debt completely here or if you want to partially settle a debt, you can go to the transfer money page (you can select to who and what amount here).


### Statistics and Tags

Tags can be added to expenses so you can organise expenses by type. As stated before, these are displayed in the Overview of an event. There are three standard tags per event: food, entrance fees, and travel. When creating an expense, if you don't find any tags that suit your needs, you can just create a new one by pressing the add button in the tag menu. You can also edit/remove tags by first selecting a tag and then pressing the pen next to it.

To find some statistics on the events, you can press the statistics button. Here you will find a pie chart which displays the amount spend on each type of expense (tags). This page will also show how much is spend in total (in the event).

### Language Switch

We currently have 3 available languages you can use: English, Dutch and Spanish.

You can change the language in the overview of an event and start screen using the language button. This button will be featuring a flag whcih corresponds to the current language configuration. Changing the language is live so restarting the app will not be necessary. Also, the language choice is persisted, so you will not have to change the language back to your preferred one on restarts.

If you would like to add your own language to this project, there is a language template that you can download as an option in the language button. You can then fill the template in your own lanuage and send it to us. We will then add it to the project.

### Foreign Currency

Our app supports foreign currencies, which just like with the language will be persisted throughout restarts. The current supported currencies are: EUR, USD and CHF. You can specify your preferred currency in the overview page of an event right next to the statistics button.
The calculations for converting between currencies will be done automatically to the correct amount (specific to the date chosen in the expense).


### Email Notification

You can invite your friends and set notifications using your own email. In order to do this you need to set up an email configuration which can be done in the settings page. You will need to fill in your email and create an app password for the password field. Please go to the following link for instructions:
https://myaccount.google.com/apppasswords (make sure 2FA is activated).
Once done, you can send a confirmation email to check if it is properly configured. Furthermore the email is also persisted throughout restarts, so you won't have to do this more than once.

## Websockets, Long-polling, HCI and other

Websockets are used in the OverviewCtrl, AddExpenseCtrl and OpenDebtCtrl to synchronize the data of an event.
The client side implementation can be found in `client/src/main/java/client/utils/WebSocketUtils.java`, while the server-side implementation can be seen in
`server/src/main/java/server/WebsocketConfig.java` and throughout the methods in the event controller using the SimpleMessagingTemplate.

Long-polling is used in the AdminEventOverviewCtrl to synchronize the page with new/deleted events. Client side implementation is in `client/src/main/java/client/utils/ServerUtils.java`.
Server-side implementation can be found in the getUpdates method in `server/src/main/java/server/api/controllers/EventController.java`.

Keyboard-shortcuts for each button can be seen when hovering other them with the mouse.

Multi-modal visualization is achieved by having red delete buttons, home icons for buttons leading back to start screen, and a plus icon on the add expense button.

The Undo functionality is implemented for expenses can be used by pressing the undo button when editing an expense.

Admin access is found in the settings page(click the settings button on the start screen). When creating a backup, the file will be located in the client folder of the project. 

### HCI: User feedback messages
User gets an error message when trying an action that can't be done, like creating an event without a title.
User (Admin) gets informative feedback when completing an action like creating a backup of an event.
User (Admin) is asked for conformation before key actions like deleting events.
