package com.exp.toolkit.cmdline.excutor.base;

import com.exp.toolkit.cmdline.exception.CmdLineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 命令行执行工具
 */
public abstract class CmdLineExcutor {

    private static final Logger logger = LoggerFactory.getLogger(CmdLineExcutor.class);

    /**
     * 执行命令行，按行返回命令行子进程的标准输出（空白行除外）
     *
     * 2021年6月21日
     *
     * @param cmdLine
     *            命令行脚本
     * @param workDir 工作目录
     * @return 命令行子进程的标准输出，按行存入{@link List}并返回
     * @throws Exception
     *             命令行执行失败，或者命令行输出读取失败
     */
    protected static List<String> execCmdLine(String cmdLine, String workDir) throws Exception {

        logger.info("执行脚本：" + cmdLine);

        List<String> outputLines = new ArrayList<>();

        // cmd子进程
        final Process process = Runtime.getRuntime().exec(cmdLine, null, new File(workDir));

        // 清空子进程的标准错误输出缓冲区，避免阻塞
        ((Runnable) () -> {
            // BufferedReader读取子进程的标准错误输出
            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    logger.error(line);
                }
            } catch (IOException e) {
                logger.error("标准错误输出读取异常", e);
            }
        }).run();

        // BufferedReader读取子进程的标准输出
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"))) {

            // 同步处理子进程的标准输出
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().length() > 0) { // 空白输出行忽略
                    outputLines.add(line);
                    logger.info(line);
                }
            }

            // 子进程阻塞直到结束，返回执行结果：0-正常，其他为出错
            int code = process.waitFor();
            if (code == 0) {
                logger.info("正常退出：" + cmdLine);
                return outputLines;
            } else {
                throw new CmdLineException("命令行异常结束，exit code为%s", code);
            }
        }
    }

}
