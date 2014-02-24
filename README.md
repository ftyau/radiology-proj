Installing Tomcat
==============
Follow dis guy in this video.
* https://www.youtube.com/watch?v=IX8xb-suzVg   


To connect to the school DBs:   
1)  Use Putty to ssh to servers (Like in 291)   
 
2)Under login.jsp, you should see: (I think we use this one if were working on the school computers)   
`String dbstring = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";`   
Change that line to:   
`String dbstring = "jdbc:oracle:thin:@localhost:1525:crs";`   
 
Change this line:   
`conn = DriverManager.getConnection(dbstring,"your_user_id","your_pass_word");`   
to whatever your DB login is. In my case   
`conn = DriverManager.getConnection(dbstring,"chautran","password");`
