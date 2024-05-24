package back.ciriu.services.imp;

import back.ciriu.models.Request.OrderDataRequest;
import back.ciriu.models.Request.OrderDetailRequest;
import back.ciriu.models.Request.OrderItemsRequest;
import back.ciriu.models.Request.OrderRequest;
import back.ciriu.services.MercadoPagoService;

import back.ciriu.services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.preference.*;
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
                    .description("None")
                    .pictureUrl("None")
                    .categoryId("None")
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
                        .success("http://localhost:4200/products")
                        .pending("http://localhost:4200/contact")
                        .failure("http://localhost:4200/about")
                        .build();

        PreferenceShipmentsRequest shipments = PreferenceShipmentsRequest.builder()
                .mode(orderData.getShippmentMode())
                .build();

        // Construye la preferencia
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .payer(payer)
                .backUrls(backUrls)
                .shipments(shipments)
                .autoReturn("approved")
                .notificationUrl("https://d221-200-91-50-188.ngrok-free.app/webhook")
                .build();

        // Crea la preferencia
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);
        return preference;
    }

    @Override
    public ResponseEntity<Void> webhookNotification(String dataId) throws JsonProcessingException {
        System.out.println("El id es: " + dataId);

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

                orderDetail.setCode(code);
                orderDetail.setQuantity(Integer.valueOf(quantity));
                orderDetail.setPrice(price);

                orderDetailRequest.add(orderDetail);
            }
            orderRequest.setOrderDetails(orderDetailRequest);
            orderService.createOrder(orderRequest);
        } else {
            System.out.println("Error al obtener información del pago");
        }
        return null;
    }

}
