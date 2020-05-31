package com.spring.server.service.interfaces;

import com.spring.server.model.dto.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * User functions
 *
 * @author diegotobalina
 */
public interface UserService {

    RegisterOutputDto register(RegisterInputDto registerInputDto) throws Exception;

    List<UserInfoOutputDto> findAll();

    void updatePassword(Principal principal, UpdateUserPasswordDto updateUserPasswordDto)
        throws Exception;

    void resetPasswordWithEmail(ResetPasswordWithEmailDto resetPasswordWithEmailDto);

    void sendResetPasswordEmail(SendResetPasswordEmailDto sendResetPasswordEmailDto)
        throws IOException, MessagingException;

    void sendVerifyEmailEmail(Principal principal) throws Exception;

    void verifyEmail(VerifyEmailDto verifyEmailDto);
}
