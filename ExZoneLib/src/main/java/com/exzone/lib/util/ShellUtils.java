package com.exzone.lib.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 作者:李鸿浩
 * 描述:执行指令工具类
 * 时间:2016/7/9.
 */
public class ShellUtils {
    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    private ShellUtils() {
        throw new AssertionError();
    }

    /**
     * 检查是否有root权限
     */
    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    /**
     * 执行shell命令，默认返回结果信息
     *
     * @param isRoot 是否需要以root运行
     */
    public static CommandResult execCommand(String command, boolean isRoot) {
        return execCommand(new String[]{command}, isRoot, true);
    }

    /**
     * 执行shell命令，默认返回结果信息
     *
     * @param isRoot 是否需要以root运行
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot) {
        return execCommand(commands == null ? null : commands.toArray(new String[]{}), isRoot, true);
    }

    /**
     * 执行shell命令，默认返回结果信息
     *
     * @param isRoot 是否需要以root运行
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot) {
        return execCommand(commands, isRoot, true);
    }

    /**
     * 执行shell命令，默认返回结果信息
     *
     * @param isRoot          是否需要以root运行
     * @param isNeedResultMsg 是否需要结果信息
     */
    public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(new String[]{command}, isRoot, isNeedResultMsg);
    }

    /**
     * 执行shell命令，默认返回结果信息
     *
     * @param isRoot          是否需要以root运行
     * @param isNeedResultMsg 是否需要结果信息
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(commands == null ? null : commands.toArray(new String[]{}), isRoot, isNeedResultMsg);
    }

    /**
     * 执行shell命令，默认返回结果信息
     *
     * @param isRoot          是否需要以root运行
     * @param isNeedResultMsg 是否需要结果信息
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
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
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }

                // 不能使用os.writeBytes(command), 避免字符串编码错误
                os.write(command.getBytes(Charset.forName("UTF-8")));
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();
            //获取结果信息
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("UTF-8")));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream(), Charset.forName("UTF-8")));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //            try {
            //                if (os != null) {
            //                    os.close();
            //                }
            //                if (successResult != null) {
            //                    successResult.close();
            //                }
            //                if (errorResult != null) {
            //                    errorResult.close();
            //                }
            //            } catch (IOException e) {
            //                e.printStackTrace();
            //            }
            IOUtils.close(os);
            IOUtils.close(successResult);
            IOUtils.close(errorResult);
            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
                : errorMsg.toString());
    }

    /**
     * 命令的结果
     */
    public static class CommandResult {

        /**
         * 命令的结果:0表示正常,反之错误
         **/
        public int result;
        /**
         * 成功信息
         **/
        public String successMsg;
        /**
         * 错误信息
         **/
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
}
