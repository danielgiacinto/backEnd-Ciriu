package back.ciriu.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;

    @Column
    private String name;

    @Column
    private String lastname;

    @Column
    private BigInteger phone;

    @ManyToOne
    @JoinColumn(name = "country")
    private CountryEntity country;

    @ManyToOne
    @JoinColumn(name = "province")
    private ProvinceEntity province;

    @Column
    private String city;

    @Column
    private String address;

    @Column
    private String floor;

    @Column
    private String departament;

    @Column
    private String email;

}
