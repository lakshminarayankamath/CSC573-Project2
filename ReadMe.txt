Pre-requisites:
1. Super User Access in Linux OS
2. jdk 7u21 installed.

If running on eos, Linux Lab Machine (Realm RHEnterprise 6), install jdk using the following commands:

eos$add jdk
eos$add jdk7u21
----------------------------------------------------------------------------------------------------------------------------------------------------------------

STEP 1: Create a new folder called "Project2" and paste the files UDPServer.java, UDPCient.java, ServerHandler.java, StopWatch.java and file.txt into the folder. 

STEP 2: Compile the Server code by typing "javac UDPServer.java" and then "java UDPServer p2mpServer 7737 file2.txt 0.01". 

STEP 3: Open a new terminal and run the client code by typing "javac UDPCient.java" and "java UDPCient p2mpClient s1 s2 s3 7737 file.txt 500".

STEP 4: Enter the IP addresses of the 3 servers.

STEP 5: After the program terminates the received file will be saved in the folder by the name "file2.txt". 

NOTE: Step 3 has to be performed only by the Client. 

----------------------------------------------------------------------------------------------------------------------------------------------------------- 
