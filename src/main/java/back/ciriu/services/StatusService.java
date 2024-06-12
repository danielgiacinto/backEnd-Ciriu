package back.ciriu.services;

import back.ciriu.entities.StatusEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StatusService {

    List<StatusEntity> getAllStatus();
}
