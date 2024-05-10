package back.ciriu.services.imp;

import back.ciriu.entities.ProvinceEntity;
import back.ciriu.repositories.ProvinceJpaRepository;
import back.ciriu.services.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceServiceImp implements ProvinceService {

    @Autowired
    private ProvinceJpaRepository provinceJpaRepository;
    @Override
    public List<ProvinceEntity> getAllProvinces() {
        return provinceJpaRepository.findAll();
    }
}
