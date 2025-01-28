package hospital.hospital_system.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ModelAndView handleError(NotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", "Global" + e.getMessage());
        modelAndView.addObject("code", 404);
        modelAndView.addObject("back", e.getBackPath());
        return modelAndView;
    }
}
