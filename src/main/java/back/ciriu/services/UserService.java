package back.ciriu.services;

import back.ciriu.models.Request.UserRequest;
import back.ciriu.models.Response.OrderResponse;
import back.ciriu.models.Response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {

    UserResponse getUser(UUID id);

    UserResponse updateInfo(UserRequest request, UUID id);

    Page<OrderResponse> getAllOrderByIdUser(Integer page, UUID id);
}
