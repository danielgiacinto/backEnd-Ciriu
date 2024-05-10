package back.ciriu.services;

import back.ciriu.entities.CountryEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CountryService {

    List<CountryEntity> getAllCountry();
}
