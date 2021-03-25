package com.whatsub.honeybread.mgmtadmin.domain.user;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import com.whatsub.honeybread.mgmtadmin.domain.user.dto.UserModifyRequest;
import com.whatsub.honeybread.mgmtadmin.domain.user.dto.UserRequest;
import com.whatsub.honeybread.mgmtadmin.domain.user.dto.UserResponse;
import com.whatsub.honeybread.mgmtadmin.support.MgmtAdminSwaggerTags;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = HoneyBreadSwaggerTags.ALL)
@RequestMapping("users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(
            value = "유저 정보 조회",
            tags = MgmtAdminSwaggerTags.USER
    )
    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") long id) {
        UserResponse userResponse = UserResponse.createUserResponse(userService.findById(id));
        return ResponseEntity.ok(userResponse);
    }

    @ApiOperation(
            value = "유저 등록",
            tags = MgmtAdminSwaggerTags.USER
    )
    @PostMapping
    public ResponseEntity<Void> register(@Valid @RequestBody UserRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(
            value = "유저 수정",
            tags = MgmtAdminSwaggerTags.USER
    )
    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable("id") long id,
                                      @Valid @RequestBody UserModifyRequest userModifyRequest,
                                      BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        userService.update(id, userModifyRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(
            value = "유저 삭제",
            tags = MgmtAdminSwaggerTags.USER
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
