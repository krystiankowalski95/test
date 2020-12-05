package pl.lodz.p.it.ssbd2020.ssbd05.entities.mor;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2020.ssbd05.entities.mok.Client;
import pl.lodz.p.it.ssbd2020.ssbd05.entities.mos.EventType;
import pl.lodz.p.it.ssbd2020.ssbd05.entities.mos.Hall;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Klasa encyjna reprezentująca rezerwację złożoną na salę przez klienta
 */
@Getter
@Setter
@Entity
@Table(name = "reservation", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"reservation_number"})
})
@TableGenerator(name = "ReservationIdGen", table = "id_generator", pkColumnName = "class_name", pkColumnValue = "reservation", valueColumnName = "id_range")
@NamedQueries({
    @NamedQuery(name = "Reservation.findAll", query = "SELECT r FROM Reservation r"),
    @NamedQuery(name = "Reservation.findById", query = "SELECT r FROM Reservation r WHERE r.id = :id"),
    @NamedQuery(name = "Reservation.findByStartDate", query = "SELECT r FROM Reservation r WHERE r.startDate = :startDate"),
    @NamedQuery(name = "Reservation.findByEndDate", query = "SELECT r FROM Reservation r WHERE r.endDate = :endDate"),
    @NamedQuery(name = "Reservation.findByClientId", query = "SELECT r from Reservation r WHERE r.client.account.id = :id"),
    @NamedQuery(name = "Reservation.findByTotalPrice", query = "SELECT r FROM Reservation r WHERE r.totalPrice = :totalPrice"),
    @NamedQuery(name = "Reservation.findByReservationNumber", query = "SELECT r FROM Reservation r WHERE r.reservationNumber = :reservationNumber"),
    @NamedQuery(name = "Reservation.findByGuestsNumber", query = "SELECT r FROM Reservation r WHERE r.guestsNumber = :guestsNumber"),
    @NamedQuery(name = "Reservation.filterByLoginAndNames", query = "SELECT r FROM Reservation r WHERE lower(r.client.account.firstname) like concat('%',lower(:filter),'%')" +
                                                                                                     " or lower(r.client.account.lastname) like concat('%',lower(:filter),'%') " +
                                                                                                     " or lower(r.reservationNumber) like concat('%',lower(:filter),'%') " +
                                                                                                      "or lower(r.client.account.login) like concat('%',lower(:filter),'%')")})

public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Setter(lombok.AccessLevel.NONE)
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ReservationIdGen")
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Digits(integer = 8, fraction = 2, message = "{validation.digits")
    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Getter(lombok.AccessLevel.NONE)
    @Setter(lombok.AccessLevel.NONE)
    @Basic(optional = false)
    @Version
    @NotNull(message = "{validation.notnull}")
    @Column(name = "version", nullable = false, columnDefinition = "bigint default 1")
    private long version;

    @NotNull(message = "{validation.notnull}")
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Status status;

    @ManyToMany
    @JoinTable(name = "extra_service_mapping",
            uniqueConstraints = @UniqueConstraint(columnNames = {"reservation_id", "extra_service_id"})

    )
    private Collection<ExtraService> extra_service = new ArrayList<>();


    @NotNull(message = "{validation.notnull}")
    @JoinColumn(name = "hall_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Hall hall;

    @NotNull(message = "{validation.notnull}")
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private Client client;

    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    @Size(min = 32, max = 32, message = "{validation.size}")
    @Column(name = "reservation_number", nullable = false, length = 32)
    private String reservationNumber;

    @NotNull(message = "{validation.notnull}")
    @JoinColumn(name = "event_type_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private EventType eventType;

    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    @Column(name = "guests_number", nullable = false)
    private Long guestsNumber;

    @OneToOne(mappedBy = "reservation")
    private Review review;

    /**
     * Konstruktor bezparametrowy klasy Reservation.
     */
    public Reservation() {
        this.reservationNumber = UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2020.ssbd05.entities.mor.Reservation[ id=" + id + " version=" + version + " ]";
    }
    
}
