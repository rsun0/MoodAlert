# MoodAlert
An Android app allowing users to send alerts about friends in bad moods

This application enables users to inform their friends that a certain friend is in a bad mood, so that they can act accordingly (comfort, avoid, etc.). The user sends out an alert that goes out to all their friends on the app except for the specific friend involved (who may or may not be a user of the app). Alerts are timestamped, but senders are anonymous. Users must be authenticated with the system to send and receive alerts.

## Trying it out
The google-services.json file is not included in this repository, since it contains the credentials to edit the Firebase project linked to this app. Consequently, the app cannot be compiled from the source on this repository. If you wish to run the app, please install the provided app-release.apk instead of compiling yourself.

## Acknowledgements
* Google Firebase
  * Authentication
  * Realtime Database
