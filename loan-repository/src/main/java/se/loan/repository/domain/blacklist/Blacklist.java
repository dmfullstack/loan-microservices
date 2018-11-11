package se.loan.repository.domain.blacklist;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Blacklist {
    @Id
    private String registrationNumber;
    @NotNull
    private Date createdDate = new Date();

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Blacklist that = (Blacklist) o;

        if (getCreatedDate() != null ? !getCreatedDate().equals(that.getCreatedDate()) : that.getCreatedDate() != null)
            return false;
        if (getRegistrationNumber() != null ? !getRegistrationNumber().equals(that.getRegistrationNumber()) : that.getRegistrationNumber() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getRegistrationNumber() != null ? getRegistrationNumber().hashCode() : 0;
        result = 31 * result + (getCreatedDate() != null ? getCreatedDate().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BlacklistEntity{" +
                "registrationNumber='" + getRegistrationNumber() + '\'' +
                ", createdDate=" + getCreatedDate() +
                '}';
    }
}
