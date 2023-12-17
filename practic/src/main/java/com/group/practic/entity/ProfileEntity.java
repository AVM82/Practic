package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;




@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "profile")
public class ProfileEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -4448191850458209643L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @OneToOne
    PersonEntity person;
    private String country;
    private String city;
    private boolean notificationEmail;
    private boolean notificationDiscord;
    @UpdateTimestamp
    Timestamp updatedAt;

    public ProfileEntity(PersonEntity person) {
        this.person = person;
    }

}
