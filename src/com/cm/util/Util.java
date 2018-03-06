package com.cm.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * 工具类
 */

public class Util {
    // 关闭流
    public static void closeIO(Closeable... io) {
        for (Closeable stream : io) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
