package back.ciriu.services.imp;

import back.ciriu.entities.BrandEntity;
import back.ciriu.repositories.BrandJpaRepository;
import back.ciriu.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImp implements BrandService {

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Override
    public List<BrandEntity> getAllBrands() {
        return brandJpaRepository.findAll();
    }
}
