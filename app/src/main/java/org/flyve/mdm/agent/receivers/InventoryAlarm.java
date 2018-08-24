/*
 * Copyright Teclib. All rights reserved.
 *
 * Flyve MDM is a mobile device management software.
 *
 * Flyve MDM is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * Flyve MDM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * ------------------------------------------------------------------------------
 * @author    Rafael Hernandez
 * @copyright Copyright Teclib. All rights reserved.
 * @license   GPLv3 https://www.gnu.org/licenses/gpl-3.0.html
 * @link      https://github.com/flyve-mdm/android-mdm-agent
 * @link      https://flyve-mdm.com
 * ------------------------------------------------------------------------------
 */

package org.flyve.mdm.agent.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import org.flyve.inventory.InventoryTask;
import org.flyve.mdm.agent.utils.FlyveLog;

public class InventoryAlarm extends BroadcastReceiver {

    /**
     * If the success XML is created, it sends the inventory
     * @param context in which the receiver is running
     * @param intent being received
     */
    @Override
    public void onReceive(final Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        final InventoryTask inventory = new InventoryTask(context.getApplicationContext(), "MDM-Agent-Android_v1.0");
        inventory.getXML(new InventoryTask.OnTaskCompleted() {
            @Override
            public void onTaskSuccess(String data) {

            }

            @Override
            public void onTaskError(Throwable error) {
                FlyveLog.e(error.getMessage());
            }
        });

        wl.release();
    }

    /**
     * Schedules the alarm
     * @param context
     */
    public void setAlarm(Context context, int minutes) {

        FlyveLog.d("Set Alarm");

        AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, InventoryAlarm.class);
        i.setAction("org.flyve.mdm.agent.inventory.ALARM");
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);


        long time = minutes * 60000;
        try {
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), time, pi);
        } catch (NullPointerException ex) {
            FlyveLog.e(ex.getMessage());
        }
    }

    /**
     * Removes the alarm with a matching argument
     * @param context
     */
    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, InventoryAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
