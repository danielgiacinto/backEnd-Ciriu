package back.ciriu.services;

import back.ciriu.entities.DeliveryStatusEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeliveryStatusService {

    List<DeliveryStatusEntity> getAllDeliveryStatus();
}
