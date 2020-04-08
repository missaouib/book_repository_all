package am.egs.bookRepository.payload;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.net.URL;

public class BookDto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 256, message = "{Size.title}")
    private String title;

    @NotBlank
    @Size(min = 2, max = 256, message = "{Size.author}")
    private String author;

    private String info;


    public BookDto() {
    }

    public BookDto(Long id, String title, String author, String info) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.info = info;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
