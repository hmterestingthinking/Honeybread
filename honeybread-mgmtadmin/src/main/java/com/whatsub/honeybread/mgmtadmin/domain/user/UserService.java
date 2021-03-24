package com.whatsub.honeybread.mgmtadmin.domain.user;

import com.whatsub.honeybread.core.domain.user.User;
import com.whatsub.honeybread.core.domain.user.UserRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.user.dto.UserModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long register(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new HoneyBreadException(ErrorCode.DUPLICATE_USER_EMAIL);
        }
        userRepository.save(user);
        user.encodePassword(passwordEncoder.encode(user.getPassword()));
        return user.getId();
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new HoneyBreadException(ErrorCode.USER_NOT_FOUND));
    }

    public void update(String targetEmail, UserModifyRequest userModifyRequest) {
        User findUser = findByEmail(targetEmail);
        findUser.encodePassword(passwordEncoder.encode(userModifyRequest.getPassword()));
        findUser.changePhoneNumber(userModifyRequest.getPhoneNumber());
        findUser.changeMarketingAgreement(userModifyRequest.isMarketingAgreement());
        findUser.changeSmsAgreement(userModifyRequest.isSmsAgreement());
    }
}