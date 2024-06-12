package back.ciriu.services.imp;

import back.ciriu.entities.StatusEntity;
import back.ciriu.repositories.StatusJpaRepository;
import back.ciriu.services.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceImp implements StatusService {

    @Autowired
    private StatusJpaRepository statusJpaRepository;
    @Override
    public List<StatusEntity> getAllStatus() {
        return statusJpaRepository.findAll();
    }
}
