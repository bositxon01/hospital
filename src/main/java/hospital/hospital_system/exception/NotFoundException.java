package hospital.hospital_system.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not found")
public class NotFoundException extends RuntimeException {
    private final String backPath;

    public NotFoundException(String message, String backPath) {
        super(message);
        this.backPath = backPath;
    }

}
