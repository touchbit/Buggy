package org.touchbit.buggy.core.helpful;

import java.io.*;

/**
 * Created by Oleg Shaburov on 18.10.2018
 * shaburov.o.a@gmail.com
 */
public class SystemOutLogger extends UnitTestLogger {

    public SystemOutLogger(File logFile){
        OutputStream outputStream = getPrintStream(logFile);
        LoggerPrintStream loggerPrintStream = new LoggerPrintStream(this, outputStream);
        System.setOut(loggerPrintStream);
        System.setErr(loggerPrintStream);
    }

    private class LoggerPrintStream extends PrintStream {

        private SystemOutLogger logger;

        public LoggerPrintStream(SystemOutLogger logger, OutputStream outputStream) {
            super(outputStream);
            this.logger = logger;
        }

        @Override
        public void println(Object line) {
            println(String.valueOf(line));
        }

        @Override
        public void println(String line) {
            logger.info(line);
            super.println(line);
        }

    }

    private static OutputStream getPrintStream(File logFile) {
        try {
            if (logFile.createNewFile()) {
                System.out.println("\nSystem.out log file path: " + logFile.getAbsolutePath() + "\n");
                return new PrintStream(new BufferedOutputStream(new FileOutputStream(logFile)), true);
            } else {
                return new FakeOutputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new FakeOutputStream();
        }
    }

    public static class FakeOutputStream extends OutputStream {
        @Override
        public void write(int b) { }
    }

}
