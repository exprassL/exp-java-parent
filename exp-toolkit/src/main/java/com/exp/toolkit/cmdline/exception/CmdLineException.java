package com.exp.toolkit.cmdline.exception;

/**
 * 命令行执行异常
 */
public class CmdLineException extends Exception {

    private static final long serialVersionUID = 6359913298441615030L;

    /**
     * 进程退出时的exit code
     */
    private int code;

    public int getCode() {
        return code;
    }

    public CmdLineException(String message, int code) {
        super(message);
        this.code = code;
    }

}
