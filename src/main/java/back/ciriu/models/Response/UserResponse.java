package back.ciriu.models.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String name;


    private String lastname;


    private BigInteger phone;


    private Long country;


    private Long province;


    private String city;


    private String address;


    private String floor;


    private String departament;

    private String email;
}
