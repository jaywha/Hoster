# Hostr - Android Application
## Contributors
Evan Blackman - Owner <br/>
uzikid100 - Original Android Developer <br/>
Jay Whaley - Current Android Developer

## Breakdown
This is the initial attempt at an MVP for the Hostr applicaiton. <br/>
The following class diagram shows the configuration for the _original_ codebase. <br/>
![Image of Class Diagram](https://github.com/jaywha/Hoster/blob/master/Class%20Diagrams/Uzi-Code.png)

For a TODO-List:<br/>
- [ ] Events<br/>
  - [x] Event Creation --> Needs to save information to create an event.<br/>
  - [x] Event Search --> See Event Gallery<br/>
  - [ ] Event Management --> Attend, delete, edit, and etcetera to events.<br/>
- [ ] Profiles<br/>
  - [ ] Profile Creation --> Sign-up using Google and Facebook or just with Hostr.<br/>
  - [ ] Profile Search --> See Profile Search<br/>
  - [ ] Profile Management --> Friend, edit, and etcetera to profiles.<br/>
  
## Android Insights
[RecyclerViews](https://developer.android.com/training/material/lists-cards.html) are key for this kind of app.<br/>
Each list of content (like attendees for an event, event lists, friends list, etc.) will be some form of this View.<br/>

Two other concepts that plauge me are [content providers](https://developer.android.com/guide/topics/providers/content-provider-basics.html), for services and push notificaitons, and FireBase, although [this course](https://www.udacity.com/course/firebase-essentials-for-android--ud009) helped aleviate some of the pain.
