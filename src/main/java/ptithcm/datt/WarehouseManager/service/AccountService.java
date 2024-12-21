package ptithcm.datt.WarehouseManager.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.config.SecurityConfig;
import ptithcm.datt.WarehouseManager.model.Account;
import ptithcm.datt.WarehouseManager.model.Role;
import ptithcm.datt.WarehouseManager.model.Staff;
import ptithcm.datt.WarehouseManager.repository.AccountRepository;
import ptithcm.datt.WarehouseManager.repository.RoleRepository;
import ptithcm.datt.WarehouseManager.repository.StaffRepository;
import ptithcm.datt.WarehouseManager.request.ChangePasswordRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private StaffRepository staffRepository;

    protected static final String SIGNER_KEY = "CYG0G6yaS0rd/FExfwMHVIeyPmwv/FZphommJCN8CwCVFv/IwJIAWiMcPxWzYkF6";
    @Autowired
    PasswordEncoder passwordEncoder;

    public Account findUserProfileByJwt(String jwt) {
        String username = securityConfig.getUsernameFromJwtToken(jwt);
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

    @Transactional
    public Account updatePassword(String passWord, String email ) throws Exception {
        Staff staff = staffRepository.findByEmail(email);
        Account update = findById(staff.getAccount().getAccountId());
        update.setPassword(passwordEncoder.encode(passWord));
        update.setUpdateAt(LocalDateTime.now());
        Account save = accountRepository.save(update);
        if(save != null){
            return save;
        }
        throw new Exception("Update password fail");

    }

    public Account findById(Long id) throws Exception {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            return account.get();
        }
        throw new Exception("Not found account by id " + id);
    }






}
