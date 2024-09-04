package back.ciriu.services.imp;

import back.ciriu.entities.*;
import back.ciriu.models.Request.UserRequest;
import back.ciriu.models.Response.*;
import back.ciriu.repositories.*;
import back.ciriu.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ProvinceJpaRepository provinceJpaRepository;

    @Autowired
    private CountryJpaRepository countryJpaRepository;

    @Autowired
    private GiftJpaRepository giftJpaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderJpaRepository orderJpaRepository;


    @Override
    public UserResponse getUser(UUID id) {
        UserEntity uE = userJpaRepository.getUserEntityById(id);
        if(uE.getId() != null) {
            UserResponse userResponse = new UserResponse();
            userResponse.setName(uE.getName());
            userResponse.setLastname(uE.getLastname());
            userResponse.setCity(uE.getCity());
            userResponse.setPhone(uE.getPhone());
            userResponse.setDepartament(uE.getDepartament());
            userResponse.setFloor(uE.getFloor());
            userResponse.setAddress(uE.getAddress());
            userResponse.setEmail(uE.getEmail());
            if(uE.getCountry() != null) {
                userResponse.setCountry(uE.getCountry().getId());
            }
            if(uE.getProvince() != null) {
                userResponse.setProvince(uE.getProvince().getId());
            }
            return userResponse;
        }
        return null;
    }

    @Override
    public UserResponse updateInfo(UserRequest request, UUID id) {
        UserEntity userEntity = userJpaRepository.getUserEntityById(id);
        if(userEntity.getId() != null) {
            userEntity.setName(request.getName());
            userEntity.setLastname(request.getLastname());
            userEntity.setPhone(request.getPhone());
            CountryEntity cE = countryJpaRepository.getReferenceById(request.getCountry());
            if(cE.getId() != null) {
                userEntity.setCountry(cE);
            } else {
                userEntity.setCountry(null);
            }
            ProvinceEntity pE = provinceJpaRepository.getReferenceById(request.getProvince());
            if(pE.getId() != null) {
                userEntity.setProvince(pE);
            } else {
                userEntity.setProvince(null);
            }
            userEntity.setCity(request.getCity());
            userEntity.setAddress(request.getAddress());
            userEntity.setFloor(request.getFloor());
            userEntity.setDepartament(request.getDepartament());
            userJpaRepository.save(userEntity);

            UserResponse userResponse = new UserResponse();
            UserEntity response = userJpaRepository.getUserEntityById(id);
            userResponse.setName(response.getName());
            userResponse.setLastname(response.getLastname());
            userResponse.setPhone(response.getPhone());
            userResponse.setCountry(response.getCountry().getId());
            userResponse.setProvince(response.getProvince().getId());
            userResponse.setCity(response.getCity());
            userResponse.setAddress(response.getAddress());
            userResponse.setFloor(response.getFloor());
            userResponse.setDepartament(response.getDepartament());
            userResponse.setEmail(response.getEmail());
            return userResponse;
        } else {
            throw new RuntimeException("No se encuntra el id");
        }
    }

    @Override
    public Page<OrderResponse> getAllOrderByIdUser(Integer page, UUID id) {
        List<OrderEntity> orderEntities = orderJpaRepository.getOrderEntityByUserId(id);

        // Ordenar las ordenes de forma descendente
        orderEntities.sort(Comparator.comparing(OrderEntity::getDate).reversed());
        List<OrderResponse> responses = new ArrayList<>();
        for(OrderEntity oE : orderEntities) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(oE.getId());
            orderResponse.setStatus(oE.getStatus().getStatus());
            orderResponse.setDelivery_status(oE.getDelivery_status().getDelivery_status());
            orderResponse.setDate(oE.getDate());
            UserResponse userResponse = new UserResponse();
            userResponse.setName(oE.getUser().getName());
            userResponse.setLastname(oE.getUser().getLastname());
            userResponse.setAddress(oE.getUser().getAddress());
            userResponse.setCountry(oE.getUser().getCountry().getId());
            userResponse.setProvince(oE.getUser().getProvince().getId());
            userResponse.setCity(oE.getUser().getCity());
            userResponse.setFloor(oE.getUser().getFloor());
            userResponse.setDepartament(oE.getUser().getDepartament());
            userResponse.setPhone(oE.getUser().getPhone());
            userResponse.setEmail(oE.getUser().getEmail());
            orderResponse.setUser(userResponse);
            orderResponse.setId_payment(oE.getId_payment());
            GiftResponse giftResponse = new GiftResponse();
            GiftEntity giftEntity = giftJpaRepository.getOrderEntityByOrderId(oE.getId());
            if(giftEntity != null) {
                giftResponse.setBy(giftEntity.getBy());
                giftResponse.setDestination(giftEntity.getDestination());
                giftResponse.setMessage(giftEntity.getMessage());
            }
            orderResponse.setGift(giftResponse);
            if(oE.getFormat_payment().equals("credit_card")) {
                orderResponse.setFormat_payment("Tarjeta de credito");
            } else if(oE.getFormat_payment().equals("debit_card")) {
                orderResponse.setFormat_payment("Tarjeta de debito");
            } else if (oE.getFormat_payment().equals("account_money")) {
                orderResponse.setFormat_payment("Dinero en cuenta");
            } else {
                orderResponse.setFormat_payment(oE.getFormat_payment());
            }
            if(oE.getFormat_method().equals("amex")){
                orderResponse.setFormat_method("American Express");
            } else if(oE.getFormat_method().equals("debvisa")) {
                orderResponse.setFormat_method("Visa");
            } else if (oE.getFormat_method().equals("account_money")) {
                orderResponse.setFormat_method("Mercado pago");
            } else if(oE.getFormat_method().equals("master")){
                orderResponse.setFormat_method("Mastercard");
            } else if(oE.getFormat_method().equals("cash")) {
                orderResponse.setFormat_method("Efectivo");
            } else {
                orderResponse.setFormat_method(oE.getFormat_method());
            }
            orderResponse.setShipping(oE.getShipping().getShipping());

            List<OrderDetailsResponse> orderDetailsResponseList = new ArrayList<>();
            for(OrderDetailEntity oDE : oE.getOrderDetails()) {
                OrderDetailsResponse orderDetailsResponse = new OrderDetailsResponse();
                orderDetailsResponse.setPrice(oDE.getPrice());
                orderDetailsResponse.setQuantity(oDE.getQuantity());
                orderDetailsResponse.setProduct(modelMapper.map(oDE.getProduct(), ProductResponseDetail.class));
                orderDetailsResponseList.add(orderDetailsResponse);
            }
            orderResponse.setOrderDetails(orderDetailsResponseList);

            responses.add(orderResponse);
        }
        int pageSize = 10;
        int totalElements = responses.size();
        List<OrderResponse> responseList = new ArrayList<>();
        if(!responses.isEmpty()) {
            int totalPages = (int) Math.ceil((double) totalElements / pageSize);
            page = Math.min(Math.max(page, 0), totalPages - 1);
            int fromIndex = page * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalElements);
            responseList = responses.subList(fromIndex, toIndex);
        }
        return new PageImpl<>(responseList, PageRequest.of(page, pageSize), totalElements);
    }
}
