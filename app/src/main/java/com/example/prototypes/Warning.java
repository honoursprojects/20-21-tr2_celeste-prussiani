package com.example.prototypes;

public class Warning {

    boolean warning;
    String message;
    public Warning(boolean warning, String message) {
        setMessage(message);
        setWarning(warning);

    }

    public boolean getWarning() {
        return this.warning;
    }

    public String getMessage() {
        return this.message;
    }

    public void setWarning(boolean flag) {
        this.warning = flag;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }
}
