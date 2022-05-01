import numpy as np
import mysql.connector
import sys

cnx = mysql.connector.connect(user='root', password='root',
                            host='localhost',
                            database='SUPERNOVAE')

cursor = cnx.cursor(buffered=True)
query = ("SELECT id FROM sn_names;")
cursor.execute(query)
id_list = cursor.fetchall()

for id in id_list:
    query_time = ("SELECT T.time_value, B.magnitude, B.luminosity FROM sn_time_association T JOIN sn_band_association B ON T.sn_id = B.sn_id WHERE T.sn_id = %s;")
    cursor.execute(query_time, id)
    times = cursor.fetchall()

    times_list = [x[0] for x in times]
    mag_list = [x[1] for x in times]
    lum_list = [x[2] for x in times]

    mag = round(abs(np.trapz(y=[max(mag_list)] * len(times_list), x=times_list, axis=0) - np.trapz(y=mag_list, x=times_list, axis=0)), 2)
    lum = round(abs(np.trapz(y=[max(lum_list)] * len(times_list), x=times_list, axis=0) - np.trapz(y=lum_list, x=times_list, axis=0)), 2)

    query_update = ("UPDATE sn_curves SET mag_curve = %s, lum_curve = %s WHERE sn_id = %s;")
    cursor.execute(query_update, (mag, lum, id[0]))
    cnx.commit()

cursor.close()
cnx.close()