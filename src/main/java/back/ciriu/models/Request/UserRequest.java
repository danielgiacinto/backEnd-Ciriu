package back.ciriu.models.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {


    private String name;


    private String lastname;


    private BigInteger phone;


    private Long country;


    private Long province;


    private String city;


    private String address;


    private String floor;


    private String departament;
}
