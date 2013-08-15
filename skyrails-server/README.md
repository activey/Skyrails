Skyrails server
===============

This folder contains binary data of modified "Interactorium" application distribution created for visualising
protein-based interactions. I literally ripped off everything that have relation with "Interactorium" itself
leaving only necessary files to run Skyrails visualisation engine.

About Skyrails
--------------
Skyrails in general is a great looking, breath taking graph visualisation engine created by Yose Widjaja for his
private purposes. Currently project is abandoned and no development is planned in near future ... Sad, but true ...

Documentation and API
---------------------
Nope, you can't find anything related to Skyrails documentation anywhere. Although it's equipped with quire powerful
interactive console (like those in FPP computer games, really) and graph interaction API, all I could find is included
in SkeiLeinRoenSkripp.html file that is some kind of automatically generated documentation. Kinda.

Starting Skyrails
-----------------
After you run skyrails.exe, you should see a basic graph with two nodes and edge between them. You can fly around graph
scene using w,a,s,d keys and mouse to point camera. On large graphs it feels like in "Hackers" movie, that one with Angelina ;)

To go into server mode, hit ~ key and type:

```
openserver 9999
```
This will open server socket locally on port 9999. Now you can use provided client.py script to execute commands remotely.

Learning
--------
Learning curve is way to steep. I did everything by analyzing scripts in original "Interactorium" distribution. All I
know is included in "scripts" folder. Feel free to read it and ask questions. If you find something new you could
achieve in Skyrails, like changing edge style hehe, please let me know ;)

Credits
-------

I'd like to thank Yose for his absolutely enormous work on Skyrails engine. If you know a way how to convince him not to
abandon Skyrails - do it :)

Kudos to Felix Leder for creating client.py - I couldn't go any further with my Java client without it ;)
