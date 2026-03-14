package exceptions;

public class ArchiveOperationException extends AppException{
    public ArchiveOperationException(String message) {
        super(message);
    }
    public ArchiveOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
