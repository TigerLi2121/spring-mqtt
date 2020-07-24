package com.mm.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具
 *
 * @author shmily
 * @date 2019-09-09
 */
public class ThrowableUtils {

    /**
     * 获取堆栈信息
     *
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}
