| Key          | Value                                       |
|--------------|---------------------------------------------|
| Date:        | 02.04.2024                                  |
| Time:        | 14:45-15:30                                 |
| Location:    | Drebbelweg Backroom                         |
| Chair        | Lara Glamuzina                              |
| Minute Taker | Robin Taekema                               |
| Attendees:   | Robin, Lara, Stoyan, Bogdan, Victor, Egemen |

Agenda Items:
- Opening by chair (1 min)
- Check -in: How is everyone doing? (1 min)
    - Everyone said they were doing great.
- Approval of the agenda - Does anyone have any additions? (1 min)
    - OMG who would've thought. Everyone approved :)
- Announcements by the TA (2 min)
    - Brightspace bug where not all files are most recent
    - Look at 'TA might ask for pointers thing'
    - Product pitch & implemented features feedback are out
    - Do self reflection. it is formative, they be strict, so follow rubrics.
    - Add more description to our issues & no tags like 'to-do/doing'
- Presentation of the current app to TA (5 min)
    - Making sure that the selected are a bit darker
    - Contrast is good
    - JSON Import might be undefined in the

- Talking Points: (Inform/ brainstorm/ decision making/ discuss)
- Approaching deadlines (1 min)
    - self reflection (draft) -> friday week 8
    - self reflection, buddy check (again), code freeze -> friday week 9
- What did everyone do last week, problems they ran into, what do they have left? (5 min)
    - We added config file, currency feature, json import
    - Lots of bug fixing (statistics/tag, adding participants or there being overflow)
    - HCI requirements
- Checking Feedback
    - Implemented features
        - basic requirements
            - invite code to overview -> to be done
            - To add/edit/remove participants to an event -> already done
            - To edit or remove existing expenses -> has currently been fixed
        - Detailed Expenses
            - To add support for giving money from A to B
                - have a click for money transfer and check one person (safer)
                - Would modify the debt, so change the expenses themselves
                - Modify the backend to be negative number?
                    - subtract it from expense total
            - To see for each expense the date, who paid, how much, and the involved participants
                - Change toString or make into tableview
        - Foreign currency
            - Just make it say the preferred currency instead of 'currency'
        - Email Notifications
            - We need to send emails with our own email.
            - ALL needs to be on client side (no requests to server).
            - "I think the idea is that you connect to your smtp server with your own email credentials and have it send the emails from your own account. This is also why there is the requriement that the client sends the emails so you do not need to send your email password to the server that might run on someone elses pc" - Mihai (2/4/2024)

- Check requirements (10 min)
    - Email notifications (email in client?)
        - Not yet, but now we understand it and will make the needed changes.
        - a.k.a. we will change everything about it
    - Foreign currency (this is the last one we were adding, how is it doing)
        - Only thing missing is the currency showing in the currency dropbox
    - Importing duplicate events from JSON
        - DONE
    - make sure we have all the basic requirements!
        - Some are missing, but we will finish those this week
- Dividing the work for weeks 8 & 9 (10 min)
    - Dividing the work between ourselves.
    - Robin:
        - Add support for giving money from A to B
        - make listView into TableView (will also allow for sorting)
        - invite code on overviewCtrl
    - Lara:
        - Finishing HCI stuff - Lara  (2/4/2024)
    - Victor:
        - EventService & Client side tests (need to be divided)
    - Bogdan:
        - Tests Client side (needs to be divided)
        - currency showing up
    - Stoyan:
        - making the email feature be completely on the client side
    - Egemen:
        - Also testing. Mostly front-end I guess (needs to be divided)
- Plan a meeting for next week (4 min)
    - We will see what happens here. Maybe go through the rubrics
        - So, we found out there is a meeting, but we will not be graded on it :)
    - We will have a meeting this saturday for preparing the presentation
        - The time will still be decided (poll on whatsapp)

- Meeting Next Week (2 min)
    - Egemen is the chair next week
    - Minute taker will maybe be present
    - imma assume we will mostly talk about what still needs to be done
- Question round: Does anyone have anything to add before the meeting closes? (2 min)
    - nop
- Closure (1 min)
    - The meeting sure has ended

- TEST WHAT YOU IMPLEMENT PLEASE IT PART OF RUBRIC APPARENTLY