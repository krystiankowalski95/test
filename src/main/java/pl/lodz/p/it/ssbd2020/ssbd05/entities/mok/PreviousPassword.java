package pl.lodz.p.it.ssbd2020.ssbd05.entities.mok;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Klasa encyjna, która przechowuje skrót poprzedniego hasła użytkownika
 * w celu uniemożliwienia mu zmiany hasła na któreś z poprzednich.
 */
@Getter
@Setter
@Entity
@Table(name = "password_history", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"password", "account_id"})
})
@TableGenerator(name = "PasswordHistoryIdGen", table = "id_generator", pkColumnName = "class_name", valueColumnName = "id_range", pkColumnValue = "password_history")
@NamedQueries({
    @NamedQuery(name = "PreviousPassword.findAll", query = "SELECT p FROM PreviousPassword p"),
    @NamedQuery(name = "PreviousPassword.findById", query = "SELECT p FROM PreviousPassword p WHERE p.id = :id"),
    @NamedQuery(name = "PreviousPassword.findByPassword", query = "SELECT p FROM PreviousPassword p WHERE p.password = :password"),
    })
public class PreviousPassword implements Serializable {

    private static final long serialVersionUID = 1L;

    @Setter(lombok.AccessLevel.NONE)
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PasswordHistoryIdGen")
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    @Size(min = 64, max = 64, message = "{validation.size}")
    @Column(name = "password", nullable = false, length = 64, updatable = false)
    private String password;

    @Getter(lombok.AccessLevel.NONE)
    @Setter(lombok.AccessLevel.NONE)
    @Basic(optional = false)
    @Version
    @NotNull(message = "{validation.notnull}")
    @Column(name = "version", nullable = false, columnDefinition = "bigint default 1")
    private long version;

    @NotNull(message = "{validation.notnull}")
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private Account account;

    /**
     * Konstruktor bezparametrowy klasy PreviousPassword.
     */
    public PreviousPassword() {
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PreviousPassword)) {
            return false;
        }
        PreviousPassword other = (PreviousPassword) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2020.ssbd05.entities.mok.PreviousPassword[ id=" + id + " version=" + version + "  ]";
    }
    
}
