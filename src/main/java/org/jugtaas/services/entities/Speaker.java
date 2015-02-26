package org.jugtaas.services.entities;

import javax.persistence.*;

/**
 * User: tiziano
 * Date: 26/02/15
 * Time: 09:42
 */
@Entity
@Table(name="speaker", schema="public")
@SequenceGenerator(name="genspeaker", sequenceName="speaker_id_seq", initialValue=1, allocationSize=1)
public class Speaker {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genspeaker")
    private Long id;

    @JoinColumn(name = "person", referencedColumnName = "id")
    @ManyToOne
    private Person person;

    @JoinColumn(name = "event", referencedColumnName = "id")
    @ManyToOne
    private JugEvent event;

}
