package com.testquack.liken.external.error;

public class ExternalPluginException extends RuntimeException {
    public ExternalPluginException() {
        super();
    }

    public ExternalPluginException(String message) {
        super(message);
    }

    public ExternalPluginException(String message, Throwable cause) {
        super(message, cause);
    }
}
