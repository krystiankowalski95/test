package pl.lodz.p.it.ssbd2020.ssbd05.entities.mos;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Klasa encyjna reprezentująca typ imprezy, jaki może się odbyć na sali.
 */
@Getter
@Setter
@Entity
@Table(name = "event_types", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"type_name"})
})
@TableGenerator(name = "EventTypesIdGen", table = "id_generator", pkColumnName = "class_name", pkColumnValue = "event_types", valueColumnName = "id_range")
@NamedQueries({
    @NamedQuery(name = "EventTypes.findAll", query = "SELECT e FROM EventType e"),
    @NamedQuery(name = "EventTypes.findById", query = "SELECT e FROM EventType e WHERE e.id = :id"),
    @NamedQuery(name = "EventTypes.findByTypeName", query = "SELECT e FROM EventType e WHERE e.typeName = :typeName")
})
public class EventType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Setter(lombok.AccessLevel.NONE)
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "EventTypesIdGen")
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    @Size(min = 1, max = 32, message = "{validation.size}")
    @Column(name = "type_name", nullable = false, length = 32, unique = true, updatable = false)
    private String typeName;

    @Getter(lombok.AccessLevel.NONE)
    @Setter(lombok.AccessLevel.NONE)
    @Basic(optional = false)
    @Version
    @NotNull(message = "{validation.notnull}")
    @Column(name = "version", nullable = false, columnDefinition = "bigint default 1")
    private long version;

    /**
     * Konstruktor bezparametrowy klasy EventType.
     */
    public EventType() {
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EventType)) {
            return false;
        }
        EventType other = (EventType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2020.ssbd05.entities.mos.EventTypes[ id=" + id + " version=" + version + " ]";
    }
    
}
