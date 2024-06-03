package ge.epam.staffmanagement.exception;

//TODO: I see that you are throwing somewhere it by I don't that you handle it with GlobalExceptionHandler,
// instead you are handling it as Exception and returns 500 instead of 404 (if smth not found - 404, according to REST)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
