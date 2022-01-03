package si.fri.rso.uniborrow.blogs.models.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "blogs")
@NamedQueries(value =
        {
                @NamedQuery(name = "BlogEntity.getAll",
                        query = "SELECT im FROM BlogEntity im")
        })
public class BlogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "timestamp")
    private Instant timestamp;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

}