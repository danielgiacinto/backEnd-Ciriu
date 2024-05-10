package back.ciriu.services;

import back.ciriu.entities.BrandEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BrandService {

    List<BrandEntity> getAllBrands();
}
