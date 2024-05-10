package back.ciriu.services;

import back.ciriu.entities.BrandEntity;
import back.ciriu.entities.CategoryEntity;
import back.ciriu.models.Request.BrandRequest;
import back.ciriu.models.Request.CategoryRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BrandService {

    List<BrandEntity> getAllBrands();

    BrandEntity newBrand(BrandRequest request);

    BrandEntity editBrand(Long id, BrandRequest brandRequest);
}
