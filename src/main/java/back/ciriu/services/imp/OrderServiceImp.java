package back.ciriu.services.imp;

import back.ciriu.entities.*;
import back.ciriu.models.Request.OrderDetailRequest;
import back.ciriu.models.Request.OrderRequest;
import back.ciriu.models.Response.OrderDetailsResponse;
import back.ciriu.models.Response.OrderResponse;
import back.ciriu.models.Response.ProductResponseDetail;
import back.ciriu.models.Response.UserResponse;
import back.ciriu.repositories.*;
import back.ciriu.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private StatusJpaRepository statusJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ShippingJpaRepository shippingJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<OrderResponse> getAllOrders(LocalDateTime fromDate, LocalDateTime toDate, Long status) {
        List<OrderEntity> orderEntities;
        StatusEntity statusEntity = null;
        if (status != 0) {
            statusEntity = statusJpaRepository.getReferenceById(status);
        }
        // filtrar los resultados de la entidadad
        if (fromDate != null && toDate != null && statusEntity != null) {
            // Filtrar por fecha y estado
            orderEntities = orderJpaRepository.findByDateBetweenAndStatus(fromDate, toDate, statusEntity);
        } else if (fromDate != null && toDate != null) {
            // Filtrar solo por fecha
            orderEntities = orderJpaRepository.findByDateBetween(fromDate, toDate);
        } else if (statusEntity != null) {
            // Filtrar solo por estado
            orderEntities = orderJpaRepository.findByStatus(statusEntity);
        } else {
            // Sin filtros, obtener todas las órdenes
            orderEntities = orderJpaRepository.findAll();
        }

        // Ordenar las ordenes de forma descendente
        Collections.sort(orderEntities, Comparator.comparing(OrderEntity::getDate).reversed());

        // Maneja la respuesta
        List<OrderResponse> responses = new ArrayList<>();
        for(OrderEntity oE : orderEntities) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(oE.getId());
            orderResponse.setStatus(oE.getStatus().getStatus());
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
            if(oE.getFormat_payment().equals("credit_card")) {
                orderResponse.setFormat_payment("Tarjeta de credito");
            } else if(oE.getFormat_payment().equals("debit_card")) {
                orderResponse.setFormat_payment("Tarjeta de debito");
            } else if (oE.getFormat_payment().equals("account_money")) {
                orderResponse.setFormat_payment("Dinero en cuenta");
            } else {
                orderResponse.setFormat_payment(oE.getFormat_payment());
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
        return responses;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public OrderResponse createOrder(OrderRequest request) {
        OrderEntity order = new OrderEntity();
        order.setId(UUID.randomUUID());
        StatusEntity statusEntity = statusJpaRepository.getReferenceById(request.getStatus());
        if(statusEntity.getId() == null){
            throw new EntityNotFoundException("No existe ese estado");
        } else {
            order.setStatus(statusEntity);
        }
        order.setDate(request.getDate());
        UserEntity userEntity = userJpaRepository.getUserEntityById(request.getId_user());
        if(userEntity.getId() == null) {
            throw new EntityNotFoundException("No existe ese usuario");
        } else {
            order.setUser(userEntity);
        }
        order.setFormat_payment(request.getFormat_payment());
        order.setId_payment(request.getId_payment());
        ShippingEntity shippingEntity = shippingJpaRepository.getReferenceById(request.getId_shipping());
        if(shippingEntity.getId() == null) {
            throw new EntityNotFoundException("No existe esa forma de envio");
        } else {
            order.setShipping(shippingEntity);
        }

        OrderEntity orderEntitySaved = orderJpaRepository.save(order);
        if(orderEntitySaved.getId() != null) {
            List<OrderDetailEntity> listOrderDetail = new ArrayList<>();
            for (OrderDetailRequest detail : request.getOrderDetails()) {
                OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
                orderDetailEntity.setId(UUID.randomUUID());
                orderDetailEntity.setOrder(orderEntitySaved);
                ProductEntity productEntity = productJpaRepository.getByCodeAndActiveTrue(detail.getCode());
                if(productEntity.getId() == null) {
                    throw new EntityNotFoundException("No existe este codigo de producto");
                } else {
                    orderDetailEntity.setProduct(productEntity);
                }
                orderDetailEntity.setQuantity(detail.getQuantity());
                orderDetailEntity.setPrice(detail.getPrice());
                listOrderDetail.add(orderDetailEntity);
            }
            orderEntitySaved.setOrderDetails(listOrderDetail);
            orderJpaRepository.save(orderEntitySaved);
        }
        // Respuesta
        OrderResponse response = new OrderResponse();
        response.setId(orderEntitySaved.getId());
        response.setStatus(orderEntitySaved.getStatus().getStatus());
        response.setDate(orderEntitySaved.getDate());

        UserResponse userResponse = new UserResponse();
        userResponse.setName(orderEntitySaved.getUser().getName());
        userResponse.setLastname(orderEntitySaved.getUser().getLastname());
        userResponse.setAddress(orderEntitySaved.getUser().getAddress());
        userResponse.setCountry(orderEntitySaved.getUser().getCountry().getId());
        userResponse.setProvince(orderEntitySaved.getUser().getProvince().getId());
        userResponse.setCity(orderEntitySaved.getUser().getCity());
        userResponse.setFloor(orderEntitySaved.getUser().getFloor());
        userResponse.setDepartament(orderEntitySaved.getUser().getDepartament());
        userResponse.setPhone(orderEntitySaved.getUser().getPhone());
        userResponse.setEmail(orderEntitySaved.getUser().getEmail());
        response.setUser(userResponse);
        response.setId_payment(orderEntitySaved.getId_payment());
        response.setFormat_payment(orderEntitySaved.getFormat_payment());
        response.setShipping(orderEntitySaved.getShipping().getShipping());
        List<OrderDetailsResponse> orderDetailsResponseList = new ArrayList<>();
        for(OrderDetailEntity oDE : orderEntitySaved.getOrderDetails()) {
            OrderDetailsResponse orderDetailsResponse = new OrderDetailsResponse();
            orderDetailsResponse.setPrice(oDE.getPrice());
            orderDetailsResponse.setQuantity(oDE.getQuantity());
            orderDetailsResponse.setProduct(modelMapper.map(oDE.getProduct(), ProductResponseDetail.class));
            orderDetailsResponseList.add(orderDetailsResponse);
        }
        response.setOrderDetails(orderDetailsResponseList);
        return response;
    }

    @Override
    public List<OrderResponse> getAllOrderByIdUser(UUID id) {
        List<OrderEntity> orderEntities = orderJpaRepository.getOrderEntityByUserId(id);

        // Ordenar las ordenes de forma descendente
        orderEntities.sort(Comparator.comparing(OrderEntity::getDate).reversed());
        List<OrderResponse> responses = new ArrayList<>();
        for(OrderEntity oE : orderEntities) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(oE.getId());
            orderResponse.setStatus(oE.getStatus().getStatus());
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
            if(oE.getFormat_payment().equals("credit_card")) {
                orderResponse.setFormat_payment("Tarjeta de credito");
            } else if(oE.getFormat_payment().equals("debit_card")) {
                orderResponse.setFormat_payment("Tarjeta de debito");
            } else if (oE.getFormat_payment().equals("account_money")) {
                orderResponse.setFormat_payment("Dinero en cuenta");
            } else {
                orderResponse.setFormat_payment(oE.getFormat_payment());
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
        return responses;
    }
}
