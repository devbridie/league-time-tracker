## league-time-tracker
Exports your League of Legends Match History time data.
Upload to Google Calendar or something similar for visualizations.
 
### Usage
1. Get an API key from [Riot's Developer Dashboard](https://developer.riotgames.com/).
2. Complete the configuration in `src/main/kotlin/.../Configuration.example.kt`.
3. Use `./gradlew run` to start the export. 

#### Docker (optional)
1. Use steps 1 and 2 from above.
2. `sudo docker build --rm . -t league-tracker`
3. `sudo docker run --name lt -t -d -i -v=/some/directory:/app/output league-tracker`

Docker will setup a cronjob periodically run the export process.

### Integrations
#### Google Calendar
* An `ics` file is generated by default. This can be uploaded to a new calendar in Google ([see FAQ](https://support.google.com/calendar/answer/37118?hl=en)).
* Google Calendar can pull in `ics` files from the internet. Make sure `/some/directory` is publicly accessible and import it using an URL in Google Calendar.

#### Toggl (WIP)