package back.ciriu.services;

import back.ciriu.entities.ProvinceEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProvinceService {

    List<ProvinceEntity> getAllProvinces();
}
