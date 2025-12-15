package su.dikunia.zabbix_clone.exceptions;

public class SwitchAlreadyExistsException extends RuntimeException {
    public SwitchAlreadyExistsException(String message) {
        super(message);
    }
}
