package am.egs.socialSyte.payload;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "error")
public class ErrorDto {

    private String message;

    private List<String> details;

    public ErrorDto(String message, List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }

    public ErrorDto(String s) {
    }

    @XmlElement(name = "General error message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @XmlElement(name = "Specific errors")
    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}