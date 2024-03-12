package com.transaction.bank.exception;


import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    //Handling 2 exception classes. Notice the parameter of handleNotFoundExceptions method (BusinessException exception)
    @ExceptionHandler(value = {BankTransactionException.class, InvalidHostelException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<MyExceptionResponse> handleNotFoundExceptions(Exception exception, HttpServletRequest request) throws IOException {
        MyExceptionResponse error = MyExceptionResponse.builder()
                .from("Exception Response")
                .errorMessage(exception.getMessage())
                .requestedURI(request.getRequestURI())
                .exceptionType(exception.getClass().getSimpleName())
                .methodName(request.getMethod())
                .errorCode("Business Error Code :: Yet to be determined")
                .thrownByMethod(exception.getStackTrace()[0].getMethodName())
                .thrownByClass(exception.getStackTrace()[0].getClassName())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS a z(O)")))
                .stackTrace(getStackTraceAsString(exception))
                .requestPayload(getRequestPayload(request))
                .environmentInfo(getEnvironmentInfo())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    private String getStackTraceAsString(Exception exception) {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement element : exception.getStackTrace()) {
            stackTrace.append(element.toString()).append("\n");
        }
        return stackTrace.toString();
    }

    public static String getRequestPayload(HttpServletRequest request) throws IOException {
        StringBuilder payload = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            payload.append(line).append('\n');
        }
        return payload.toString();
    }

    public static String getEnvironmentInfo() {
        StringBuilder environmentInfo = new StringBuilder();

        // Retrieve environment variables
        environmentInfo.append("Server Name: ").append(System.getenv("COMPUTERNAME")).append("\n");
        environmentInfo.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        environmentInfo.append("OS: ").append(System.getProperty("os.name")).append("\n");

        // Retrieve system properties
        Enumeration<String> propertyNames = (Enumeration<String>) System.getProperties().propertyNames();
        while (propertyNames.hasMoreElements()) {
            String propertyName = propertyNames.nextElement();
            environmentInfo.append(propertyName).append(": ").append(System.getProperty(propertyName)).append("\n");
        }

        return environmentInfo.toString();
    }

}
