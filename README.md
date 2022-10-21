# Fit3164
## Name
Team 12 Data for Firebase Database

## Description
This repository contains all of the JSON data files that should be in the Firebase Database for Team 12's Women's Safety in Melbourne app. There are 7 files:

emergencydata.json : This file contains data about hospital locations in Melbourne.

fixedcameralocation.json : This file contains data about camera location in Melbourne, mostly in the CBD.

melbsuburbs.json : This file contains data about the suburbs in Melbourne, including names and latitude and longitude.

policestationdata.json : This file contains data about police stations in Melbourne.

suburbcrimestats2020.json : The file contains data about suburb crime rates in 2020 for 6 different types of crime; Crimes against the person, Property and deception offences, Drug offences, Public order and security offences, Justice procedures offences and other offences.

suburbcrimestats2021.json : The file contains data about suburb crime rates in 2021.
trainstationdata.json : The file contains data about train station locations in Melbourne.

## Installation
To create a Firebase Database from this repository, first download the files. 

Go to https://firebase.google.com/ which is where you can create a Firebase Database. You can sign in to Firebase using your Gmail account. After you have signed in , on the home page there is a 'Create Project' button. Name your project, we had named ours 'Fit3164 women safety in Mel' then press continue. After you have named your file, the next step is to enable Google Analytics then press continue. Leave the Analytics Location as the United States and accept their terms, then create the project.

Your project is now created. In the sidebar, there should be "Build". Press that and then select 'Realtime Database' and press 'Create Database'. Set the Realtime Database Location to United States (us-central1), then press next and select 'Start in test mode', then press the 'Enable' button. The database has now been created.

The database is currently empty. If you hover over the link in the white box, a plus sign and bin will appear next to it. Press on the plus sign, it will then ask you for a Key and Value. In the Key, write 'emergency data' and then press 'Add'. Next, press on the 'emergency data' key that has just been created. Press the three dots in the corner and then press 'Import JSON' and locate or drop in the 'emergencydata.json' file and press import once it has been loaded. You have now loaded one JSON file into the database. Press on the reference url at the top to go back and do the same for all of the other files. Create a key 'fixed camera location' and import 'fixedcameralocation.json'. Create a key 'melbsuburbs' and import 'melbsuburbs.json'. Create a key 'police station data' and import 'policestationdata.json'. Create a key 'suburb crime stats 2020' and import 'suburbcrimestats2020.json'. Create a key 'suburb crime stats 2021' and import 'suburbcrimestats2021.json'. Create a key 'train station data' and import 'trainstationdata.json'. The database has now been created and filled.

## Authors and acknowledgment
Team 12 FIT3164

## Project status
Development finished. 
