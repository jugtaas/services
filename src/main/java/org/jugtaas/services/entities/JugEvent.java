package org.jugtaas.services.entities;

/**
 * User: tiziano
 * Date: 19/02/15
 * Time: 15:15
 */
public class JugEvent {

    private Long id;
    private String title;
    private String subtitle;

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
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
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
