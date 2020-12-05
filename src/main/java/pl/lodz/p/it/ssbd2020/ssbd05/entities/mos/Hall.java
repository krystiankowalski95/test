package pl.lodz.p.it.ssbd2020.ssbd05.entities.mos;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2020.ssbd05.entities.mor.Reservation;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Klasa encyjna reprezentująca salę.
 */
@Getter
@Setter
@Entity
@Table(name = "hall", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
@TableGenerator(name = "HallIdGen", table = "id_generator", pkColumnName = "class_name", pkColumnValue = "hall", valueColumnName = "id_range")
@NamedQueries({
        @NamedQuery(name = "Hall.findAll", query = "SELECT h FROM Hall h"),
        @NamedQuery(name = "Hall.findById", query = "SELECT h FROM Hall h WHERE h.id = :id"),
        @NamedQuery(name = "Hall.findByName", query = "SELECT h FROM Hall h WHERE h.name = :name"),
        @NamedQuery(name = "Hall.findByCapacity", query = "SELECT h FROM Hall h WHERE h.capacity = :capacity"),
        @NamedQuery(name = "Hall.findByActive", query = "SELECT h FROM Hall h WHERE h.active = :active"),
        @NamedQuery(name = "Hall.findByArea", query = "SELECT h FROM Hall h WHERE h.area = :area"),
        @NamedQuery(name = "Hall.findByDescription", query = "SELECT h FROM Hall h WHERE h.description = :description"),
        @NamedQuery(name = "Hall.findByPrice", query = "SELECT h FROM Hall h WHERE h.price = :price"),
        @NamedQuery(name = "Hall.filterByNameAndAddress", query = "SELECT h FROM Hall h WHERE LOWER(h.name) LIKE CONCAT('%', LOWER(:filter), '%')" +
                "OR LOWER(h.address.street) LIKE CONCAT('%', LOWER(:filter), '%')" +
                "OR LOWER(h.address.city) LIKE CONCAT('%', LOWER(:filter), '%')")

})
public class Hall implements Serializable, Comparable<Hall> {

    private static final long serialVersionUID = 1L;

    @Setter(lombok.AccessLevel.NONE)
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "HallIdGen")
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    @Size(min = 1, max = 32, message = "{validation.size}")
    @Column(name = "name", nullable = false, length = 32, unique = true)
    private String name;

    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    @Column(name = "capacity", nullable = false)
    @Digits(integer = 7, fraction = 0, message = "{validation.digits}")
    private int capacity;

    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    @Column(name = "active", nullable = false)
    private boolean active;

    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    @Column(name = "area", nullable = false)
    @Digits(integer = 7, fraction = 2, message = "{validation.digits}")
    private double area;

    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    @Size(min = 1, max = 512, message = "{validation.size}")
    @Column(name = "description", nullable = false, length = 512)
    private String description;

    @Digits(integer = 7, fraction = 2, message = "{validation.digits}")
    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    @Column(name = "price", nullable = false)
    @Digits(integer = 7, fraction = 2, message = "{validation.digits}")
    private double price;

    @Getter(lombok.AccessLevel.NONE)
    @Setter(lombok.AccessLevel.NONE)
    @Basic(optional = false)
    @Version
    @NotNull(message = "{validation.notnull}")
    @Column(name = "version", nullable = false, columnDefinition = "bigint default 1")
    private long version;

    @ManyToMany(cascade = {CascadeType.DETACH})
    @JoinTable( name = "event_types_mapping")
    private Collection<EventType> event_type = new ArrayList<>();

    @NotNull
    @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(cascade = {CascadeType.DETACH}, optional = false)
    private Address address;

    @OneToMany(mappedBy = "hall")
    private Collection<Reservation> reservationCollection = new ArrayList<>();

    /**
     * Konstruktor bezparametrowy klasy Hall.
     */
    public Hall() {
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Hall)) {
            return false;
        }
        Hall other = (Hall) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2020.ssbd05.entities.mos.Hall[ id=" + id + " version=" + version + " ]";
    }

    @Override
    public int compareTo(Hall o) {
        if(this.capacity != o.getCapacity() ||
            this.price != o.getPrice() ||
            this.active != (o.isActive() ||
            this.area != o.getArea()) ||
            !this.name.equals(o.getName()) ||
            !this.description.equals(o.getDescription())) {
            return 1;
        } else {
            if (this.event_type.size() == o.getEvent_type().size()) {
                for (EventType eventType : this.event_type) {
                    if(!o.getEvent_type().contains(eventType))
                        return 1;
                }
            } else return 1;
        }
        return 0;
    }
}