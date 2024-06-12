package back.ciriu.services.imp;

import back.ciriu.entities.DeliveryStatusEntity;
import back.ciriu.repositories.DeliveryStatusJpaRepository;
import back.ciriu.services.DeliveryStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryStatusServiceImp implements DeliveryStatusService {

    @Autowired
    private DeliveryStatusJpaRepository deliveryStatusJpaRepository;


    @Override
    public List<DeliveryStatusEntity> getAllDeliveryStatus() {
        return deliveryStatusJpaRepository.findAll();
    }
}
