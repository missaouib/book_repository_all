package am.egs.bookRepository.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20)", nullable = false)
    private Long id;

    @Column(name = "title", columnDefinition = "VARCHAR(50)", nullable = false)
    private String title;

    @Column(name = "author", columnDefinition = "VARCHAR(50)", nullable = false)
    private String author;

    @OneToMany(mappedBy = "book")
    private Set<FavoriteBook> favoriteBooks = new HashSet<FavoriteBook>();


    public Book() {
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

    public Set<FavoriteBook> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void setFavoriteBooks(Set<FavoriteBook> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }


}
