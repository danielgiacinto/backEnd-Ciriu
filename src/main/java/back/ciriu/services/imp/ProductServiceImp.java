package back.ciriu.services.imp;


import back.ciriu.entities.*;
import back.ciriu.models.Request.ToyRequestDto;
import back.ciriu.models.Request.ToyUpdateRequestDto;
import back.ciriu.models.Response.ImageResponse;
import back.ciriu.models.Response.ProductResponseDto;
import back.ciriu.repositories.*;
import back.ciriu.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryJpaRepository subCategoryJpaRepository;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private ImageJpaRepository imageJpaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<ProductResponseDto> getAllToys(Integer page, String sortBy, String searchTerm, Boolean nonStock, String category, String brand) {
        List<ProductEntity> listProducts;
        if(nonStock) {
            listProducts = productJpaRepository.findAllByActiveTrueAndStockEquals(0);
        } else {
            listProducts = productJpaRepository.findAllByActiveTrue();
        }

        // Filtro de búsqueda searchTerm
        List<ProductEntity> filteredProducts = listProducts.stream()
                .filter(productEntity -> productEntity.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        productEntity.getCode().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        productEntity.getBrand().getBrand().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        // Filtro de busqueda category
        if(category != null && !category.isEmpty()) {
            filteredProducts = filteredProducts.stream()
                    .filter(productEntity -> productEntity.getSubCategoryEntity().getSubcategory().equalsIgnoreCase(category) ||
                            productEntity.getCategoryEntity().getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        // Filtro de busqueda brand
        if(brand != null && !brand.isEmpty()) {
            filteredProducts = filteredProducts.stream()
                    .filter(productEntity -> productEntity.getBrand().getBrand().equalsIgnoreCase(brand))
                    .collect(Collectors.toList());
        }
        // Ordenar
        if (sortBy.equals("asc")) {
            filteredProducts.sort(Comparator.comparing(ProductEntity::getPrice));
        } else if (sortBy.equals("desc")) {
            filteredProducts.sort(Comparator.comparing(ProductEntity::getPrice).reversed());
        }

        // Paginación
        int pageSize = 9;
        int totalElements = filteredProducts.size();
        List<ProductResponseDto> pageResponse = new ArrayList<>();
        if(!filteredProducts.isEmpty()){
            // Calcular el número total de páginas
            int totalPages = (int) Math.ceil((double) totalElements / pageSize);

            // Asegurar que la página esté dentro de los límites válidos
            page = Math.min(Math.max(page, 0), totalPages - 1);

            // calcula los indices
            int fromIndex = page * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalElements);

            List<ProductEntity> pageProducts = filteredProducts.subList(fromIndex, toIndex);

            pageResponse = pageProducts.stream()
                    .map(productEntity -> {
                        ProductResponseDto productResponseDto = modelMapper.map(productEntity, ProductResponseDto.class);
                        // Extraer las URLs de las imágenes y agregarlas al ProductResponseDto
                        List<String> imageUrls = productEntity.getImages().stream()
                                .map(ImageEntity::getUrl)
                                .collect(Collectors.toList());
                        productResponseDto.setImage(imageUrls);
                        return productResponseDto;
                    })
                    .collect(Collectors.toList());
        }

        return new PageImpl<>(pageResponse, PageRequest.of(page, pageSize), totalElements);
    }

    @Override
    public ProductResponseDto getToyByCode(String code) {
        ProductEntity productEntity = productJpaRepository.getByCodeAndActiveTrue(code);
        ProductResponseDto productResponseDto = modelMapper.map(productEntity, ProductResponseDto.class);

        // Extraer las URLs de las imágenes y asignarlas al ProductResponseDto
        List<String> imageUrls = productEntity.getImages().stream()
                .map(ImageEntity::getUrl)
                .collect(Collectors.toList());
        productResponseDto.setImage(imageUrls);

        return productResponseDto;
    }


    @Override
    public ProductResponseDto createToy(ToyRequestDto toy) {
        try {
            CategoryEntity categoryEntity = categoryRepository.getReferenceById(toy.getCategory());
            SubCategoryEntity subCategoryEntity = subCategoryJpaRepository.getReferenceById(toy.getSubcategory());
            BrandEntity brandEntity = brandJpaRepository.getReferenceById(toy.getBrand());
            ProductEntity productEntity = new ProductEntity();
            UUID id = UUID.randomUUID();
            productEntity.setId(id);
            ProductEntity existsCode = productJpaRepository.findByCodeEquals(toy.getCode());
            if(existsCode != null && toy.getCode().equals(existsCode.getCode())) {
                throw new EntityNotFoundException("ya existe ese codigo");
            } else {
                productEntity.setCode(toy.getCode());
            }
            productEntity.setName(toy.getName());
            productEntity.setDescription(toy.getDescription());
            productEntity.setCategoryEntity(categoryEntity);
            productEntity.setSubCategoryEntity(subCategoryEntity);
            productEntity.setBrand(brandEntity);
            productEntity.setPrice(toy.getPrice());
            productEntity.setStock(toy.getStock());
            productEntity.setActive(true);
            ProductEntity toySaved = productJpaRepository.save(productEntity);
            if(toySaved.getId() != null) {
                if (toy.getImage() != null && !toy.getImage().isEmpty()) {
                    for (String imageUrl : toy.getImage()) {
                        ImageEntity imageEntity = new ImageEntity();
                        imageEntity.setUrl(imageUrl);
                        imageEntity.setProduct(toySaved);
                        imageJpaRepository.save(imageEntity);
                    }
                }
                ProductResponseDto productResponseDto = modelMapper.map(toySaved, ProductResponseDto.class);
                return productResponseDto;
            } else {
                throw new RuntimeException("No se pudo crear el juguete correctamente.");
            }
        } catch (Exception e) {
            throw new RuntimeException("No se puede crear el producto, " + e.getMessage());
        }
    }

    @Override
    public ProductResponseDto updateToy(String code, ToyUpdateRequestDto requestDto) {
        try {
            ProductEntity productEntity = productJpaRepository.getByCodeAndActiveTrue(code);
            if(productEntity != null) {
                productEntity.setName(requestDto.getName());
                CategoryEntity category = categoryRepository.getReferenceById(requestDto.getCategory());
                productEntity.setCategoryEntity(category);
                SubCategoryEntity subCategoryEntity = subCategoryJpaRepository.getReferenceById(requestDto.getSub_category());
                productEntity.setSubCategoryEntity(subCategoryEntity);
                productEntity.setDescription(requestDto.getDescription());
                productEntity.setPrice(requestDto.getPrice());
                BrandEntity brandEntity = brandJpaRepository.getReferenceById(requestDto.getBrand());
                productEntity.setBrand(brandEntity);
                ProductEntity toyUpdate = productJpaRepository.save(productEntity);
                if(toyUpdate.getId() != null) {
                    if(requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
                        List<ImageEntity> imageEntities = imageJpaRepository.getAllByProduct(toyUpdate);
                        imageJpaRepository.deleteAll(imageEntities);
                        for (String imageUrl : requestDto.getImage()) {
                            ImageEntity imageEntity = new ImageEntity();
                            imageEntity.setUrl(imageUrl);
                            imageEntity.setProduct(toyUpdate);
                            imageJpaRepository.save(imageEntity);
                        }
                    }
                    ProductResponseDto productResponseDto = modelMapper.map(toyUpdate, ProductResponseDto.class);
                    return productResponseDto;
                }else {
                    throw new RuntimeException("No se pudo actualizar el juguete correctamente.");
                }
            } else {
                throw new RuntimeException("No existe ese producto");
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar el producto" + e.getMessage());
        }
    }

    @Override
    public Boolean deleteProduct(String code) {
        ProductEntity productEntity = productJpaRepository.getByCodeAndActiveTrue(code);
        if(productEntity != null) {
            productEntity.setActive(false);
            productJpaRepository.save(productEntity);
            return true;
        } else {
            return false;
        }
    }
}
