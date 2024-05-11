package back.ciriu.services.imp;

import back.ciriu.entities.BrandEntity;
import back.ciriu.entities.CategoryEntity;
import back.ciriu.models.Request.BrandRequest;
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

    @Override
    public BrandEntity newBrand(BrandRequest request) {
        BrandEntity brandEntity = brandJpaRepository.getByBrand(request.getBrand().toLowerCase());
        if(brandEntity != null){
            throw new RuntimeException("Ya existe esa marca");
        } else {
            BrandEntity brand = new BrandEntity();
            brand.setBrand(request.getBrand().toLowerCase());
            BrandEntity brandSaved = brandJpaRepository.save(brand);
            return brandSaved;
        }
    }

    @Override
    public BrandEntity editBrand(Long id, String brandRequest) {
        try{
            BrandEntity brand = brandJpaRepository.getReferenceById(id);
            if(brand != null){
                brand.setBrand(brandRequest.toLowerCase());
                return brandJpaRepository.save(brand);
            } else {
                throw new RuntimeException("No existe esa marca");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error, " + e.getMessage());
        }
    }
}
