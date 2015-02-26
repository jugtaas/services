package org.jugtaas.services.entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * User: tiziano
 * Date: 19/02/15
 * Time: 15:15
 */
@Entity
@Table(name="event", schema="public")
@SequenceGenerator(name="genevent", sequenceName="event_id_seq", initialValue=1, allocationSize=1)
public class JugEvent {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genevent")
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="subtitle")
    private String subtitle;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "speaker",
            joinColumns = {@JoinColumn(name = "event", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "person", referencedColumnName = "id")})
    private Collection<Person> speakers;

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

    public String getSubtitle() {
        return "[s]" + subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Collection<Person> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(Collection<Person> speakers) {
        this.speakers = speakers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JugEvent)) return false;

        JugEvent jugEvent = (JugEvent) o;

        if (id != null ? !id.equals(jugEvent.id) : jugEvent.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
