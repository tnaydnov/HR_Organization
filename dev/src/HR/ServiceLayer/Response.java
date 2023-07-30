package HR.ServiceLayer;

import java.util.Objects;

public class Response {

    private final String errorMessage;

    public Response(String errorMessage){
        this.errorMessage = errorMessage;
    }
    public boolean errorOccurred(){
        return !Objects.equals(errorMessage, "");
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
