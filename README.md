# WebCarRental
Car rental WebApp.
Customer can choose a car for reservation from a list 
by filling out the order form: passport data, reservation terms.
Administrator registers the return of the vehicle.
In the case of damage to the vehicle, Administrator makes information and an invoice for the repairs.
The administrator may reject the reservation, stating the reason for refusal.

Used FrameWorks, libraries: Maven, Apache Tiles, Bootstrap, Log4j, Servlet-JSP-api, BeanUtils, Javax.Mail.

Prerequisites for installation: MySql server, with carrental database created, installed maven.
To deploy: checkout repository, go to downloaded dir, with pom.xml in cmd.exe and start the app by "mvn clean  tomcat:run-war".

See short overview here - https://www.youtube.com/watch?v=GYlZDrNa72E