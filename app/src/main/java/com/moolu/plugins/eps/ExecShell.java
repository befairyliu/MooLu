package com.moolu.plugins.eps;

import com.moolu.framework.NananLog;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Nanan on 3/4/2015.
 */
public class ExecShell {
    private final static Logger Log = new NananLog(ExecShell.class);

    public static enum SHELL_CMD {
        check_su_binary(new String[] {"/system/xbin/which", "su"}), ;
        String[] command;
        SHELL_CMD(String[] command) {
            this.command = command;
        }
    }

    public ArrayList<String> executeCommand(SHELL_CMD shellCmd) {
        String line = null;
        ArrayList<String> fullResponse = new ArrayList<String>();
        Process localProcess = null;

        try {
            localProcess = Runtime.getRuntime().exec(shellCmd.command);
        } catch (Exception e) {
            return null;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
        try {
            while ((line = in.readLine()) != null) {
                fullResponse.add(line);
            }
        } catch (Exception e) {
            Log.debug(e.getMessage());
        }
        return fullResponse;
    }
}
