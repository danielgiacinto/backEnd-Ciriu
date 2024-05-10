package back.ciriu.services.imp;

import back.ciriu.entities.CountryEntity;
import back.ciriu.repositories.CountryJpaRepository;
import back.ciriu.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImp implements CountryService {

    @Autowired
    private CountryJpaRepository countryJpaRepository;

    @Override
    public List<CountryEntity> getAllCountry() {
        return countryJpaRepository.findAll();
    }
}
