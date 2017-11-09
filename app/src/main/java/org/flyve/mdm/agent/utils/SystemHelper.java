package org.flyve.mdm.agent.utils;

import java.io.DataOutputStream;
import java.io.IOException;

/*
 *   Copyright © 2017 Teclib. All rights reserved.
 *
 *   This file is part of flyve-mdm-android
 *
 * flyve-mdm-android is a subproject of Flyve MDM. Flyve MDM is a mobile
 * device management software.
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
 * @date      8/11/17
 * @copyright Copyright © 2017 Teclib. All rights reserved.
 * @license   GPLv3 https://www.gnu.org/licenses/gpl-3.0.html
 * @link      https://github.com/flyve-mdm/flyve-mdm-android
 * @link      https://flyve-mdm.com
 * ------------------------------------------------------------------------------
 */
public class SystemHelper {

    private SystemHelper() {}

    private static void executecmd(String[] cmds) {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            for (String tmpCmd : cmds) {
                os.writeBytes(tmpCmd + "\n");
            }
            os.writeBytes("exit\n");
            os.flush();
        }
        catch (IOException ex){
            FlyveLog.d(ex.getMessage());
        }
    }

    public static void disableRoaming(boolean disable){
        String value = "1";
        if(disable) {
            value = "0";
        }

        String[] cmds = {"cd /system/bin" ,"settings put global data_roaming0 " + value};

        executecmd(cmds);
    }

    public static void disableNFC(boolean disable) {
        String value = "enable";
        if(disable) {
            value = "disable";
        }

        String[] cmds = {"svc nfc " + value};

        executecmd(cmds);
    }

    public static void disableMobileLine(boolean disable) {
        String value = "enable";
        if(disable) {
            value = "disable";
        }

        String[] cmds = {"svc data " + value};

        executecmd(cmds);
    }
}