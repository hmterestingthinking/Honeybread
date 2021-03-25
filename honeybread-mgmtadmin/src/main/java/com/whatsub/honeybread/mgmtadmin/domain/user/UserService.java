package com.whatsub.honeybread.mgmtadmin.domain.user;

import com.whatsub.honeybread.core.domain.user.User;
import com.whatsub.honeybread.core.domain.user.UserRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.user.dto.UserModifyRequest;
import com.whatsub.honeybread.mgmtadmin.domain.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long register(UserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new HoneyBreadException(ErrorCode.DUPLICATE_USER_EMAIL);
        }
        User user = userRequest.toUser();
        user.encodePassword(user.getPassword(), passwordEncoder);
        userRepository.save(user);
        return user.getId();
    }

    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new HoneyBreadException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void update(long id, UserModifyRequest userModifyRequest) {
        User findUser = findById(id);
        User modifyUser = userModifyRequest.toUser(findUser.getEmail());
        modifyUser.encodePassword(modifyUser.getPassword(), passwordEncoder);
        findUser.update(modifyUser);
    }

    @Transactional
    public void delete(long id) {
        userRepository.delete(findById(id));
    }
}