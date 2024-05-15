package back.ciriu.services;

import back.ciriu.models.Request.OrderDataRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MercadoPagoService {

    Preference createPreference(OrderDataRequest orderData, String idempotencyKey) throws MPException, MPApiException;

    ResponseEntity<Void> webhookNotification(String dataId) throws JsonProcessingException;
}
