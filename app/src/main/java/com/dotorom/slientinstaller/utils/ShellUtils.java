package com.dotorom.slientinstaller.utils;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.text.TextUtils;
import android.util.Log;

/**
 * ShellUtils
 */
public class ShellUtils {

    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";
    public static final String TAG = "ShellUtils";
    public static boolean pingBaiduOk = false;

    private ShellUtils() {
        throw new AssertionError();
    }

    /**
     * check whether has root permission
     * 
     * @return
     */
    public static boolean checkRootPermission() {
        Log.i(TAG, "checkRootPermission()");
        return execCommand(new String[] { "echo root", "" }, true, false).result == 0;
    }

    /**
     * execute shell command, default return result msg
     * 
     * @param command
     *            command
     * @param isRoot
     *            whether need to run with root
     * @return
     * @see ShellUtils#execCommand1(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String command, boolean isRoot) {
        Log.d(TAG, "execCommand() has one param");
        return execCommand(new String[] { command }, isRoot, true);
    }

    /**
     * execute shell commands, default return result msg
     * 
     * @param commands
     *            command list
     * @param isRoot
     *            whether need to run with root
     * @return
     * @see ShellUtils#execCommand1(String[], boolean, boolean)
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot) {
        Log.d(TAG, "execCommand() has list params");
        return execCommand(
                commands == null ? null : commands.toArray(new String[] {}),
                isRoot, true);
    }

    /**
     * execute shell commands, default return result msg
     * 
     * @param commands
     *            command array
     * @param isRoot
     *            whether need to run with root
     * @return
     * @see ShellUtils#execCommand1(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot) {
        Log.d(TAG, "execCommand() has two params");
        return execCommand(commands, isRoot, true);
    }

    /**
     * execute shell command
     * 
     * @param objects
     *            command
     * @param isRoot
     *            whether need to run with root
     * @param isNeedResultMsg
     *            whether need result msg
     * @return
     * @see ShellUtils#execCommand1(String[], boolean, boolean)
     */
    public static CommandResult execCommand(Object[] commands, boolean isRoot,
            boolean isNeedResultMsg) {
        Log.d(TAG, "execCommand() has Object params");
        return execCommand(new Object[] { commands }, isRoot, isNeedResultMsg);
    }

    /**
     * execute shell commands
     * 
     * @param commands
     *            command list
     * @param isRoot
     *            whether need to run with root
     * @param isNeedResultMsg
     *            whether need result msg
     * @return
     * @see ShellUtils#execCommand1(String[], boolean, boolean)
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot,
            boolean isNeedResultMsg) {
        return execCommand(
                commands == null ? null : commands.toArray(new String[] {}),
                isRoot, isNeedResultMsg);
    }

    /**
     * execute shell commands
     * 
     * @param commands
     *            command array
     * @param isRoot
     *            whether need to run with root
     * @param isNeedResultMsg
     *            whether need result msg
     * @return
     * 
     * 
     * 
     *         if isNeedResultMsg is false, {@link CommandResult#successMsg} is
     *         null and {@link CommandResult#errorMsg} is null.
     * 
     *         if {@link CommandResult#result} is -1, there maybe some
     *         excepiton.
     * 
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot,
            boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(
                    isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null || command.equals("")) {
                    continue;
                }

                // donnot use os.writeBytes(commmand), avoid chinese charset
                // error
                os.write(command.getBytes());
                Log.d(TAG, "command=" + command);
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();
            if (result == 0) {
                pingBaiduOk = true;
            } else {
                pingBaiduOk = false;
            }
            // get command result
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(
                        process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(
                        process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(result, successMsg == null ? null
                : successMsg.toString(), errorMsg == null ? null
                : errorMsg.toString());
    }

    /**
     * result of command
     * 
     * 
     * 
     * 
     * {@link CommandResult#result} means result of command, 0 means normal,
     * else means error, same to excute in linux shell
     * 
     * {@link CommandResult#successMsg} means success message of command result
     * 
     * {@link CommandResult#errorMsg} means error message of command result
     * 
     */
    public static class CommandResult {

        /** result of command **/
        public int result;
        /** success message of command result **/
        public String successMsg;
        /** error message of command result **/
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }

    /**
     * if exec success ,return true
     * */
    public static boolean execCommand(String command,
            List<List<Map<String, String>>> listinfo, List<String> listtitleinfo) {
        if (TextUtils.isEmpty(command)) {
            return false;
        }

        Process process = null;
        BufferedReader successResult = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            os.write(command.getBytes());
            Log.d(TAG, "command=" + command);
            os.writeBytes(COMMAND_LINE_END);
            os.flush();
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            if (process.waitFor() == 0) {
                pingBaiduOk = true;
            } else {
                pingBaiduOk = false;
                return false;
            }
            // get command result
            successResult = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String s;
            List<Map<String, String>> list = null;
            int index = 0;
            while ((s = successResult.readLine()) != null) {
                Log.d(TAG, s);
                if (s.contains("info:") || s.contains("Physical Address:")
                        || s.contains("PUK:")) {
                    if (list != null && list.size() > 0) {
                        listinfo.add(list);
                    }
                    list = new ArrayList<Map<String, String>>();
                    listtitleinfo.add(s.substring(0, s.length()-1));
                } else if (s.contains("=")) {
                    if (list != null) {
                        addInfo(list, s.split("="));
                    }
                }
            }
            if (list != null && list.size() > 0) {
                listinfo.add(list);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, " e :" + e);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " e :" + e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    private static void addInfo(List<Map<String, String>> list, String[] split) {
        Map<String, String> info = new HashMap<String, String>();
        if (split != null && split.length >= 1) {
            info.put("itemTitle", split[0].substring(1, split[0].length()));
            if (split.length == 1) {
                info.put("itemContent", "");
            } else {
                info.put("itemContent", split[1]);
            }
            list.add(info);
        }
    }
}
