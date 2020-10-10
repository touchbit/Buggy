package org.touchbit.buggy.core.logback.appender;

import org.touchbit.buggy.core.model.ResultStatus;

import java.io.File;

public class TestLog {

    private File file;
    private String fileName;
    private ResultStatus resultStatus;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }
}
