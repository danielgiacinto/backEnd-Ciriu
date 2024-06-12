package back.ciriu.controllers;

import back.ciriu.entities.DeliveryStatusEntity;
import back.ciriu.services.DeliveryStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("delivery")
public class DeliveryStatusController {

    @Autowired
    private DeliveryStatusService deliveryStatusService;

    @GetMapping("")
    public ResponseEntity<List<DeliveryStatusEntity>> getAll() {
        return ResponseEntity.ok(deliveryStatusService.getAllDeliveryStatus());
    }
}
