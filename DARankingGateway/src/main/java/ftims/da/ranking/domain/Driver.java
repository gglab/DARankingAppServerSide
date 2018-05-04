package ftims.da.ranking.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Driver.
 */
@Entity
@Table(name = "driver")
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "rank", nullable = false)
    private Double rank;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @JoinTable(name = "driver_awards",
               joinColumns = @JoinColumn(name="drivers_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="awards_id", referencedColumnName="id"))
    private Set<DriverAward> awards = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRank() {
        return rank;
    }

    public Driver rank(Double rank) {
        this.rank = rank;
        return this;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public Driver name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public Driver user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<DriverAward> getAwards() {
        return awards;
    }

    public Driver awards(Set<DriverAward> driverAwards) {
        this.awards = driverAwards;
        return this;
    }

    public Driver addAwards(DriverAward driverAward) {
        this.awards.add(driverAward);
        driverAward.getDrivers().add(this);
        return this;
    }

    public Driver removeAwards(DriverAward driverAward) {
        this.awards.remove(driverAward);
        driverAward.getDrivers().remove(this);
        return this;
    }

    public void setAwards(Set<DriverAward> driverAwards) {
        this.awards = driverAwards;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Driver driver = (Driver) o;
        if (driver.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), driver.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Driver{" +
            "id=" + getId() +
            ", rank=" + getRank() +
            ", name='" + getName() + "'" +
            "}";
    }
}
