# Bernardo Santos Final Project CSC3810 Guidelines
This file contains the guidelines used to succesfully import and run the project.

## Steps

1. Download the project .zip file and unzip it. Import the unzipped folder into your IDE.
2. Run the `Bernardo_FinalProject_DatabaseCreation.sql`, and the `Bernardo_FinalProject_StoredProceduresCreation.sql` scripts in MySQL.
3. Open the `application.properties` file that can be found in the `resources` folder. Change the properties so that they match your local MySQL credentials. <b> Please note that the program will only run successfully if the SQL scripts in step (2) are executed prior to the program execution. </b>
4. Run the main file: `FinalProjectApplication.java`.
5. If you are running the project for the first time, wait for the data retrieval process to be over. This may take 1-2 minute(s), as the program collects data from a public API to feed the MySQL database. If it is not first time you are running the program and your MySQL database has already been populated, then you may skip this step.
6. Open the following URL in your browser: https://localhost:8080/
7. Each page of the website represents a workflow:

    &nbsp;&nbsp;&nbsp;i. Charts, where the user can see and interact with the data through charts.

    &nbsp;&nbsp;&nbsp;ii. Add New Data, where the user can add new data to the database through a csv file.

    &nbsp;&nbsp;&nbsp;iii. Delete Data, where the user can delete a specific Supernova from the database.

    &nbsp;&nbsp;&nbsp;iv. Drop Database, where the user can remove all the data from the database, leaving it empty.

    &nbsp;&nbsp;&nbsp;v. Reload Database, where the user can reload the database with the initial data.
8. Please note that I have attached a few csv files as examples inside the `CSV Files - Add New Data` folder. The Add New Data workflow will only work if the csv file uploaded follows the same exact structure of the files SN1970J and SN2007GF attached. Only 3 columns: name, time and magnitude. Name is a string, time and magnitude are numbers (integers/floats/doubles). If there is any sort of deviation from that structure, an error message will pop up to the user. The file SN1992P has errors in it and can be used to check that the program does not accept such a file.
9. Please note that there is an <b>.exe</b> file in the project, that is responsible for calculating all the areas under the curves of the supernovae. Initially this was a Python script, but problems would arise when trying to run it from Java, so the <b>.exe</b> file was the best option. If you run into issues when that file is executed, take a look at the Python script inside the <b>/resources/scripts</b> folder. Such <b>.exe</b> file is only executed when there is an update to the database, so in cases such as adding new data, reloading the database, or initially loading the database. 

If there are any questions, please contact me at santosbe@merrimack.edu or consult the `Bernardo_FinalProject_VideoDemo.mp4` video.