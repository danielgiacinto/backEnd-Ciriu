package back.ciriu.services.imp;

import back.ciriu.entities.GiftEntity;
import back.ciriu.entities.OrderEntity;
import back.ciriu.models.Request.OrderDataRequest;
import back.ciriu.models.Request.OrderDetailRequest;
import back.ciriu.models.Request.OrderItemsRequest;
import back.ciriu.models.Request.OrderRequest;
import back.ciriu.models.Response.OrderResponse;
import back.ciriu.repositories.GiftJpaRepository;
import back.ciriu.repositories.OrderJpaRepository;
import back.ciriu.services.MercadoPagoService;

import back.ciriu.services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.preference.*;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.mercadopago.MercadoPagoConfig;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MercadoPagoImp implements MercadoPagoService {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private GiftJpaRepository giftJpaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Preference createPreference(OrderDataRequest orderData, String idempotencyKey) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken(accessToken);
        System.out.println(idempotencyKey);
        // Construye los ítems de la preferencia
        List<PreferenceItemRequest> items = new ArrayList<>();
        for(OrderItemsRequest item: orderData.getItems()) {
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(item.getCode())
                    .title(item.getName())
                    .description(orderData.getGift().getBy() + ", " + orderData.getGift().getDestination())
                    .pictureUrl("")
                    .categoryId(orderData.getGift().getMessage())
                    .quantity(item.getQuantity())
                    .currencyId("ARS")
                    .unitPrice(new BigDecimal(String.valueOf(item.getPrice())))
                    .build();
            items.add(itemRequest);
        }
        // seteo el identificador en el nombre el id del usuario
        PreferencePayerRequest payer = PreferencePayerRequest.builder()
                .name(orderData.getIdUser())
                .surname(orderData.getShippmentMode())
                .build();

        // Construye las backUrls
        PreferenceBackUrlsRequest backUrls =
                PreferenceBackUrlsRequest.builder()
                        .success("http://localhost:4200/success")
                        .pending("http://localhost:4200/pending")
                        .failure("http://localhost:4200/about")
                        .build();

        PreferenceShipmentsRequest shipments = PreferenceShipmentsRequest.builder()
                .mode(orderData.getShippmentMode())
                .build();

        // Exlusion de metodo de pago en efectivo
        List<PreferencePaymentTypeRequest> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add(PreferencePaymentTypeRequest.builder().id("ticket").build());
        PreferencePaymentMethodsRequest paymentMethods =
                PreferencePaymentMethodsRequest.builder()
                        .excludedPaymentTypes(excludedPaymentTypes)
                        .installments(6)
                        .build();

        // Idempotency
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("X-Idempotency-Key", idempotencyKey);
        MPRequestOptions requestOptions = MPRequestOptions.builder()
                .accessToken(accessToken)
                .connectionRequestTimeout(2000)
                .connectionTimeout(2000)
                .socketTimeout(2000)
                .customHeaders(customHeaders)
                .build();

        // Construye la preferencia
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .paymentMethods(paymentMethods)
                .items(items)
                .payer(payer)
                .backUrls(backUrls)
                .shipments(shipments)
                .autoReturn("approved")
                .notificationUrl("https://b9a1-190-231-79-101.ngrok-free.app/webhook")
                .build();

        // Crea la preferencia
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest, requestOptions);
        return preference;
    }

    @Override
    public ResponseEntity<Void> webhookNotification(String dataId) throws JsonProcessingException {
        System.out.println("El id es: " + dataId);
        String byAndDestination = "";
        String by = "";
        String destination = "";
        String message = "";
        String apiUrl = "https://api.mercadopago.com/v1/payments/" + dataId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Realiza la solicitud HTTP GET a la API de Mercado Pago
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> paymentData = mapper.readValue(response.getBody(), Map.class);
        String status = (String) paymentData.get("status");

        // Verifica si la solicitud fue exitosa
        if (response.getStatusCode() == HttpStatus.OK && !Objects.equals(status, "rejected")) {
            Map<String, Object> additionalInfo = (Map<String, Object>) paymentData.get("additional_info");
            Map<String, Object> payer = (Map<String, Object>) additionalInfo.get("payer");
            List<Map<String, Object>> items = (List<Map<String, Object>>) additionalInfo.get("items");

            // crear la orden para la base de datos
            OrderRequest orderRequest = new OrderRequest();
            GiftEntity giftEntity = new GiftEntity();

            String id_user = (String) payer.get("first_name");
            String id_shipping = (String) payer.get("last_name");
            String format_payment = (String) paymentData.get("payment_type_id");
            String format_method = (String) paymentData.get("payment_method_id");
            if(status.equals("pending")) {
                orderRequest.setStatus(2L);
            } else if(status.equals("approved")) {
                orderRequest.setStatus(1L);
            } else if(status.equals("in_process")) {
                orderRequest.setStatus(4L);
            }
            orderRequest.setDate(LocalDateTime.now());
            orderRequest.setId_user(UUID.fromString(id_user));
            orderRequest.setId_payment(dataId);
            if(format_payment.equals("ticket")) {
                orderRequest.setFormat_payment(format_method);
                orderRequest.setFormat_method("cash");
            } else {
                orderRequest.setFormat_payment(format_payment);
                orderRequest.setFormat_method(format_method);
            }
            if(id_shipping.equals("Retiro en local")) {
                orderRequest.setId_shipping(2L);
            } else {
                orderRequest.setId_shipping(1L);
            }
            List<OrderDetailRequest> orderDetailRequest = new ArrayList<>();
            // Itera sobre los elementos de la lista de items
            for (Map<String, Object> item : items) {
                OrderDetailRequest orderDetail = new OrderDetailRequest();
                String code = (String) item.get("id");
                String quantity = (String) item.get("quantity");
                BigDecimal price = new BigDecimal(String.valueOf(item.get("unit_price")));
                // aca le saco la description para el gift
                byAndDestination = (String) item.get("description");
                by = byAndDestination.substring(0, byAndDestination.indexOf(",")).trim();
                destination = byAndDestination.substring(byAndDestination.indexOf(",") + 1).trim();
                message = (String) item.get("category_id");

                orderDetail.setCode(code);
                orderDetail.setQuantity(Integer.valueOf(quantity));
                orderDetail.setPrice(price);

                orderDetailRequest.add(orderDetail);
            }
            orderRequest.setOrderDetails(orderDetailRequest);
            OrderResponse orderR = orderService.createOrder(orderRequest);
            // creo el gift
            if(by != null && !by.isEmpty() && destination != null && !destination.isEmpty() && message != null && !message.isEmpty()) {
                giftEntity.setBy(by);
                giftEntity.setDestination(destination);
                giftEntity.setMessage(message);
                OrderEntity orderEntityGift = orderJpaRepository.getOrderEntityById(orderR.getId());
                giftEntity.setOrder(orderEntityGift);
                giftJpaRepository.save(giftEntity);
            }

        } else {
            System.out.println("Error al obtener información del pago");
        }
        return null;
    }

}
