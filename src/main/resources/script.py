import numpy as np
import mysql.connector
import sys
import re

f = open("application.properties","r")
lines = "".join(f.readlines())

username = re.findall("spring.datasource.username=.*", lines)[0].split("=")[1]
pwd = re.findall("spring.datasource.password=.*", lines)[0].split("=")[1]
host = re.findall("\/\/.*:", lines)[0][2:-1]
db = re.findall("spring.datasource.url=.*\/.*", lines)[0].split("/")[-1]

cnx = mysql.connector.connect(user=username, password=pwd,
                            host=host,
                            database=db)

cursor = cnx.cursor(buffered=True)
query = ("idNames")
cursor.callproc(query)
id_list = cursor.stored_results()

for result in id_list:
    for id in result:
        query_time = ("valuesById")
        cursor.callproc(query_time, id)
        times = cursor.stored_results()

        for item in times:
            all_items = item.fetchall()        
            times_list = [x[0] for x in all_items]
            mag_list = [x[1] for x in all_items]
            lum_list = [x[2] for x in all_items]

            mag = round(abs(np.trapz(y=[max(mag_list)] * len(times_list), x=times_list, axis=0) - np.trapz(y=mag_list, x=times_list, axis=0)), 2)
            lum = round(abs(np.trapz(y=[max(lum_list)] * len(times_list), x=times_list, axis=0) - np.trapz(y=lum_list, x=times_list, axis=0)), 2)
            
            query_update = ("updateCurves")
            cursor.callproc(query_update, (mag, lum, id[0]))
            cnx.commit()
cursor.close()
cnx.close()