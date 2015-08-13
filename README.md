# Pitch
This is the android app for pitching to another guy in this network

Bussiness Canvas for this project: 
  https://canvanizer.com/canvas/92FcXHRhGds
  
App screen:
  http://invis.io/MX3NFHNCZ
  
Draw IO Design Diagram:
https://drive.draw.io/#G0Bz9aG93bURIFU2ZqLXZZdW15UHc

Class Diagram: 
https://drive.draw.io/#G0Bz9aG93bURIFUVhmbXZ0RGtyaFE
# Screens design
Please check screen_design/ folder.

# Presentation Tier
Intent 1 : Login Activity -> Pitch Activity
   User Profile data

Intent 2 : Pitch Activity -> Pitch Chat Activity
   User profile data and pitch destination data

Intent 3 : Pitch Activity -> Pitch Video Chat Activity
   User profile data and pitch destination data

# Content Provider
User profile data will be persisted.
The pitch list will be feed from the backend api.
    User profile table including :    name, work experience(one to many), avatar, tag line
I'm planing to use RealmDB for persistence

# Application Tier
This app is designed as dead easy on the client side. I'm planing to use FireBase for the chat part.
And use video chat api for the video chat part.

# Integration Tier
Since I'm heavily relying on the Linkedin API in my app, I'm planning to have a API request package
using volley to handle the network request.
