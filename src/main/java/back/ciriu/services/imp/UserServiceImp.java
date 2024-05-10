package back.ciriu.services.imp;

import back.ciriu.entities.CountryEntity;
import back.ciriu.entities.ProvinceEntity;
import back.ciriu.entities.UserEntity;
import back.ciriu.models.Request.UserRequest;
import back.ciriu.models.Response.UserResponse;
import back.ciriu.repositories.CountryJpaRepository;
import back.ciriu.repositories.ProvinceJpaRepository;
import back.ciriu.repositories.UserJpaRepository;
import back.ciriu.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ProvinceJpaRepository provinceJpaRepository;

    @Autowired
    private CountryJpaRepository countryJpaRepository;


    @Override
    public UserResponse getUser(UUID id) {
        UserEntity uE = userJpaRepository.getUserEntityById(id);
        if(uE.getId() != null) {
            UserResponse userResponse = new UserResponse();
            userResponse.setName(uE.getName());
            userResponse.setLastname(uE.getLastname());
            userResponse.setCity(uE.getCity());
            userResponse.setPhone(uE.getPhone());
            userResponse.setDepartament(uE.getDepartament());
            userResponse.setFloor(uE.getFloor());
            userResponse.setAddress(uE.getAddress());
            userResponse.setEmail(uE.getEmail());
            if(uE.getCountry() != null) {
                userResponse.setCountry(uE.getCountry().getId());
            }
            if(uE.getProvince() != null) {
                userResponse.setProvince(uE.getProvince().getId());
            }
            return userResponse;
        }
        return null;
    }

    @Override
    public UserResponse updateInfo(UserRequest request, UUID id) {
        UserEntity userEntity = userJpaRepository.getUserEntityById(id);
        if(userEntity.getId() != null) {
            userEntity.setName(request.getName());
            userEntity.setLastname(request.getLastname());
            userEntity.setPhone(request.getPhone());
            CountryEntity cE = countryJpaRepository.getReferenceById(request.getCountry());
            if(cE.getId() != null) {
                userEntity.setCountry(cE);
            } else {
                userEntity.setCountry(null);
            }
            ProvinceEntity pE = provinceJpaRepository.getReferenceById(request.getProvince());
            if(pE.getId() != null) {
                userEntity.setProvince(pE);
            } else {
                userEntity.setProvince(null);
            }
            userEntity.setCity(request.getCity());
            userEntity.setAddress(request.getAddress());
            userEntity.setFloor(request.getFloor());
            userEntity.setDepartament(request.getDepartament());
            userJpaRepository.save(userEntity);

            UserResponse userResponse = new UserResponse();
            UserEntity response = userJpaRepository.getUserEntityById(id);
            userResponse.setName(response.getName());
            userResponse.setLastname(response.getLastname());
            userResponse.setPhone(response.getPhone());
            userResponse.setCountry(response.getCountry().getId());
            userResponse.setProvince(response.getProvince().getId());
            userResponse.setCity(response.getCity());
            userResponse.setAddress(response.getAddress());
            userResponse.setFloor(response.getFloor());
            userResponse.setDepartament(response.getDepartament());
            userResponse.setEmail(response.getEmail());
            return userResponse;
        } else {
            throw new RuntimeException("No se encuntra el id");
        }
    }
}
