# -*- coding: utf-8 -*-
# Import libraries.
import numpy as np
import mysql.connector
import sys
import re

# Get content from application.properties.
file = open("application.properties","r")
lines = "".join(file.readlines())

# Parse content to credentials.
username = re.findall("spring.datasource.username=.*", lines)[0].split("=")[1]
pwd = re.findall("spring.datasource.password=.*", lines)[0].split("=")[1]
host = re.findall("\/\/.*:", lines)[0][2:-1]
db = re.findall("spring.datasource.url=.*\/.*", lines)[0].split("/")[-1]

# Connect to database.
cnx = mysql.connector.connect(user=username, password=pwd,
                            host=host,
                            database=db)
cursor = cnx.cursor(buffered=True)

# Call stored procedure to get all supernova names' IDs.
query = ("idNames")
cursor.callproc(query)
id_list = cursor.stored_results()

# Iterate over the IDs and update the curves table based on each of those IDs.
for result in id_list:

    # For item in the result set of stored procedure.
    for id in result:
        # Call stored procedure valuesById.
        query_time = ("valuesById")
        cursor.callproc(query_time, id)
        times = cursor.stored_results()

        # Iterate over values based on the ID.
        for item in times:
            all_items = item.fetchall() # Get all the rows from the query. 
            times_list = [x[0] for x in all_items] # Get only the time values.
            mag_list = [x[1] for x in all_items] # Get only the magnitude values.
            lum_list = [x[2] for x in all_items] # Get only the luminosity values.

            # Calculate areas based on the values found.
            mag = round(abs(np.trapz(y=[max(mag_list)] * len(times_list), x=times_list, axis=0) - np.trapz(y=mag_list, x=times_list, axis=0)), 2)
            lum = round(abs(np.trapz(y=[max(lum_list)] * len(times_list), x=times_list, axis=0) - np.trapz(y=lum_list, x=times_list, axis=0)), 2)
            
            # Call stored procedre updateCurves to update the database with the new curves data.
            query_update = ("updateCurves")
            cursor.callproc(query_update, (mag, lum, id[0]))
            cnx.commit() # Commit to the database so that changes persist.

# Close cursor and connection.
cursor.close()
cnx.close()