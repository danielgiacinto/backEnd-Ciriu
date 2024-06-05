package back.ciriu.services.imp;

import back.ciriu.entities.*;
import back.ciriu.models.Request.OrderDetailRequest;
import back.ciriu.models.Request.OrderRequest;
import back.ciriu.models.Response.*;
import back.ciriu.repositories.*;
import back.ciriu.services.OrderService;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private StatusJpaRepository statusJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ShippingJpaRepository shippingJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;
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
        order.setFormat_method(request.getFormat_method());
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
            sendMailOrder(orderEntitySaved);
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
        response.setFormat_method(orderEntitySaved.getFormat_method());
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

    private void sendMailOrder(OrderEntity orderEntitySaved) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(orderEntitySaved.getUser().getEmail());
            helper.setSubject("Gracias por tu compra en Ciriú !!");
            // Calcular el total
            BigDecimal total = orderEntitySaved.getOrderDetails().stream()
                    .map(detail -> detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // Procesar la plantilla Thymeleaf
            Context context = new Context();
            context.setVariable("user", orderEntitySaved.getUser().getName() + " " + orderEntitySaved.getUser().getLastname());
            context.setVariable("id", orderEntitySaved.getId_payment());
            context.setVariable("status", orderEntitySaved.getStatus().getStatus());
            context.setVariable("date", orderEntitySaved.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
            context.setVariable("shipping", orderEntitySaved.getShipping().getShipping());
            context.setVariable("address", orderEntitySaved.getUser().getAddress());
            context.setVariable("city", orderEntitySaved.getUser().getCity());
            context.setVariable("province", orderEntitySaved.getUser().getProvince().getProvince());
            context.setVariable("phone", orderEntitySaved.getUser().getPhone());

            List<OrderDetailsResponse> orderDetails = orderEntitySaved.getOrderDetails().stream().map(oDe -> {
                OrderDetailsResponse orderDetailsResponse = new OrderDetailsResponse();
                orderDetailsResponse.setPrice(oDe.getPrice());
                orderDetailsResponse.setQuantity(oDe.getQuantity());
                orderDetailsResponse.setProduct(modelMapper.map(oDe.getProduct(), ProductResponseDetail.class));
                return orderDetailsResponse;
            }).toList();

            context.setVariable("orderDetails", orderDetails);
            context.setVariable("total", total);
            String contenidoHtml = templateEngine.process("emailOrder", context);
            helper.setText(contenidoHtml, true);
            javaMailSender.send(message);
        }catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }

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
        return responses;
    }

    @Override
    public ReportResponse consultReport(LocalDateTime fromDate, LocalDateTime toDate) {
        List<OrderEntity> orders;
        HashMap<String, BigDecimal> methodPayments = new HashMap<>();
        HashMap<String, Integer> productCount = new HashMap<>();
        BigDecimal totalEfectivo = new BigDecimal(0);
        BigDecimal totalTarjetaDebito = new BigDecimal(0);
        BigDecimal totalTarjetaCredito = new BigDecimal(0);
        BigDecimal totalDineroCuenta = new BigDecimal(0);
        ReportResponse response = new ReportResponse();
        if(fromDate != null && toDate != null && fromDate.isBefore(toDate)){
            StatusEntity status = new StatusEntity(1L, "Aprobado");
            orders = orderJpaRepository.findByDateBetweenAndStatus(fromDate, toDate, status);
        } else {
            throw new RuntimeException("Debe ingresar fechas validas para hacer la consulta de reportes");
        }
        // Total de ordenes por fechas
        response.setTotalOrders(orders.size());
        BigDecimal totalSales = new BigDecimal(0);
        for(OrderEntity o : orders){
            for(OrderDetailEntity od : o.getOrderDetails()){
                BigDecimal quantity = new BigDecimal(od.getQuantity());
                if(o.getFormat_payment().equals("pagofacil") || o.getFormat_payment().equals("rapipago")){
                    totalEfectivo = totalEfectivo.add(quantity.multiply(od.getPrice()));
                } else if(o.getFormat_payment().equals("credit_card")){
                    totalTarjetaCredito = totalTarjetaCredito.add(quantity.multiply(od.getPrice()));
                } else if(o.getFormat_payment().equals("account_money")){
                    totalDineroCuenta = totalDineroCuenta.add(quantity.multiply(od.getPrice()));
                } else if (o.getFormat_payment().equals("debit_card")) {
                    totalTarjetaDebito = totalTarjetaDebito.add(quantity.multiply(od.getPrice()));
                }
                totalSales = totalSales.add(quantity.multiply(od.getPrice()));

                String code = od.getProduct().getCode();
                productCount.put(code, productCount.getOrDefault(code, 0) + od.getQuantity());
            }
        }
        // Monto total de ventas
        response.setSales(totalSales);
        // Valores de los metodos de pago
        methodPayments.put("Efectivo", totalEfectivo);
        methodPayments.put("Tarjeta de Credito", totalTarjetaCredito);
        methodPayments.put("Dinero en cuenta", totalDineroCuenta);
        methodPayments.put("Tarjeta de Debito", totalTarjetaDebito);
        response.setMethodpayment(methodPayments);

        // Encuentra el producto más vendido
        String codeProduct = null;
        int mostQuantity = 0;
        for (Map.Entry<String, Integer> entry : productCount.entrySet()) {
            if (codeProduct == null || entry.getValue() > mostQuantity) {
                codeProduct = entry.getKey();
                mostQuantity = entry.getValue();
            }
        }
        // Establece el producto más vendido en la respuesta
        ProductTopResponse productTopResponse = new ProductTopResponse(codeProduct, mostQuantity);
        response.setProductTop(productTopResponse);
        return response;
    }

    @Override
    public Map<String, List<BigDecimal>> consultReportBar(Integer year) {
        LocalDateTime fromDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(year, 12, 31, 23, 59);
        StatusEntity status = new StatusEntity(1L, "Aprobado");
        List<OrderEntity> orders = orderJpaRepository.findByDateBetweenAndStatus(fromDate, toDate, status);
        Map<String, Map<Month, BigDecimal>> salesByCategory = new HashMap<>();

        // Lista de categorías (asegúrate de tener estas categorías disponibles)
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        List<String> categories = new ArrayList<>();
        for(CategoryEntity c : categoryEntities){
            categories.add(c.getCategory());
        }


        // Inicializa el mapa con las categorías y meses
        for (String category : categories) {
            Map<Month, BigDecimal> monthlySales = new EnumMap<>(Month.class);
            for (Month month : Month.values()) {
                monthlySales.put(month, BigDecimal.ZERO);
            }
            salesByCategory.put(category, monthlySales);
        }

        // Suma las ventas totales para cada categoría y mes
        for (OrderEntity order : orders) {
            LocalDateTime orderDate = order.getDate();
            Month month = orderDate.getMonth();

            for (OrderDetailEntity detail : order.getOrderDetails()) {
                String category = detail.getProduct().getCategoryEntity().getCategory();
                BigDecimal quantity = new BigDecimal(detail.getQuantity());
                BigDecimal subtotal = quantity.multiply(detail.getPrice());

                if (salesByCategory.containsKey(category)) {
                    BigDecimal totalSales = salesByCategory.get(category).get(month);
                    totalSales = totalSales.add(subtotal);
                    salesByCategory.get(category).put(month, totalSales);
                }
            }
        }

        // Prepara la estructura de datos para devolver
        Map<String, List<BigDecimal>> result = new HashMap<>();
        for (String category : categories) {
            List<BigDecimal> data = new ArrayList<>();
            Map<Month, BigDecimal> monthlySales = salesByCategory.get(category);
            for (Month month : Month.values()) {
                data.add(monthlySales.get(month));
            }
            result.put(category, data);
        }

        return result;
    }

}
