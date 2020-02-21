# Abalone_Project
the code of the abalone project of the module Software Systems from the University of Twente
Software Systems Project instructions
Sam Freriks and Ayla van der Wal
Group 2.6

you will need all the files from the src folder inside the zip
the doc folder has all the javadoc as html
For the report, a high res classdiagram is available in the root folder of this zip

----INSTALATION----
1. make a new javaproject in eclipse
2. copy all the files from the src in the zip to the new src folder of the project
3. make sure your module-info.java has the correct name
4. make sure that in the build path JUnit 5 is added

----STARTING A SERVER----

1. run the AbaloneServer class
2. Type the port you want to connect to
3. The server is now running on the printed IP (This should be your own IPV4)

----STARTING A CLIENT----

1. To start a client run the AbalaloneClient class 
2. Type in your wanted player name
3. type "no" if you dont want to play as a AI (go to step: 6)
4. Type "yes" if you want to play as a AI
5. Type 0 for the randomAI, type 1 for the SmartyAi, type 2 for the AttackAI
6. Type the port you want to connect to
7. type the IP you want to connect to
8. When connection failed type "yes" to try again
9. You will now be asked to which queue you want to join
10. Type in "2" , "3" or "4" to select the wanted queue
11. The server will now assign you to the queue
12. Connect another client to start a game
13. When a queue has enough players the game will automaticly be started

----MAKING A MOVE----
1. type "m" followed by the direction you want ("ll"(lower left), ""lr"(lower right) "l"(left) "r"(right) "ul"(upperleft) "ur"(upperright)) and the marbles you want to move (only select neighboring marbles)
2. if your move is invalid you will be asked to try again. 
3. Now wait again till it is your turn, you will get a message that it is your turn

----ASKING FOR HELP----
1. type "h" to print the help menu

----CHATTING----
1. type "c" followed by your message to send a chat message during a game
<c><space><message>

----ASKING A HINT----
1. type "t" to ask for a hint
2. a hint will appear on the screen

----JOINING A NEW GAME AFTER A GAME IS FINISHED----
1. if a game is finished you will be asked to type in the wanted queue to join
2. give your wanted queue to join by typing "2", "3" or "4" 
3. beware sometimes you need to type in your wanted queue multiple times*
