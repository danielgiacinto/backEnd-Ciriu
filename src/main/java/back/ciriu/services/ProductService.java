package back.ciriu.services;


import back.ciriu.models.Request.ToyRequestDto;
import back.ciriu.models.Request.ToyUpdateRequestDto;
import back.ciriu.models.Response.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
public interface ProductService {

    Page<ProductResponseDto> getAllToys(Integer page, String sortBy, String searchTerm, Boolean nonStock, String category, String brand);

    ProductResponseDto getToyByCode(String code);

    ProductResponseDto createToy(ToyRequestDto toy);

    ProductResponseDto updateToy(String code, ToyUpdateRequestDto requestDto);
    Boolean deleteProduct(String code);
}
