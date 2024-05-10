package back.ciriu.controllers;

import back.ciriu.models.Request.OrderDataRequest;
import back.ciriu.services.MercadoPagoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class MercadoPagoController {


    @Autowired
    private MercadoPagoService mercadoPagoService;

    @PostMapping("/create_preference")
    public ResponseEntity<Preference> createPreference(@RequestBody OrderDataRequest request, @RequestHeader("X-Idempotency-Key") String idempotencyKey) throws MPException, MPApiException {
        return ResponseEntity.ok(mercadoPagoService.createPreference(request, idempotencyKey));
    }

//    @PostMapping("/webhook")
//    public ResponseEntity<String> viewNotification(@RequestHeader HttpHeaders headers,
//                                                    @RequestParam("data.id") String dataId,
//                                                    @RequestHeader("x-request-id") String xRequestId,
//                                                    @RequestHeader("x-signature") String xSignature) {
//        if (mercadoPagoService.verifyWebhook(dataId, xRequestId, xSignature)) {
//            System.out.println("HMAC verification passed");
//            return ResponseEntity.ok("HMAC verification passed");
//        } else {
//            System.out.println("HMAC verification failed");
//            return ResponseEntity.ok("HMAC verification failed");
//        }
//    }
    @PostMapping("/webhook")
    public ResponseEntity<Void> webhookNotification(@RequestParam("data.id")String dataId) throws JsonProcessingException {
        return mercadoPagoService.webhookNotification(dataId);
    }
}
