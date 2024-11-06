package ptithcm.datt.WarehouseManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.config.SecurityConfig;
import ptithcm.datt.WarehouseManager.model.Account;
import ptithcm.datt.WarehouseManager.repository.AccountRepository;
import ptithcm.datt.WarehouseManager.request.ChangePasswordRequest;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private AccountRepository accountRepository;

    protected static final String SIGNER_KEY = "CYG0G6yaS0rd/FExfwMHVIeyPmwv/FZphommJCN8CwCVFv/IwJIAWiMcPxWzYkF6";
    @Autowired
    PasswordEncoder passwordEncoder;

    public Account findUserProfileByJwt(String jwt) {
        String username = securityConfig.getUsernameFromJwtToken(jwt);
        System.out.println("Tên làaaaaaaaaaaaaaaaaaaaaaaaaaaaaa  " + username);
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        return accountOptional.orElseThrow(() ->
                new RuntimeException("User not found with name: " + username)
        );
    }

    public Account changePassword(String jwt, ChangePasswordRequest rq) throws Exception {
        Account user = findUserProfileByJwt(jwt);
        if(passwordEncoder.matches(rq.getPassword(),user.getPassword())){
            user.setPassword(passwordEncoder.encode(rq.getNewPassword()));
            user.setUpdateAt(LocalDateTime.now());
            return accountRepository.save(user);
        }
        throw new Exception("Password is incorrect");
    }


}
