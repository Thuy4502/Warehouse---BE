package ptithcm.datt.WarehouseManager.service;

import com.google.api.client.util.DateTime;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import jdk.jshell.spi.ExecutionControl;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.config.SecurityConfig;
import ptithcm.datt.WarehouseManager.dto.request.AccountRequest;
import ptithcm.datt.WarehouseManager.dto.request.AuthenticationRequest;
import ptithcm.datt.WarehouseManager.dto.request.IntrospectRequest;
import ptithcm.datt.WarehouseManager.dto.request.StaffRequest;
import ptithcm.datt.WarehouseManager.dto.response.AuthenticationResponse;
import ptithcm.datt.WarehouseManager.dto.response.IntrospectResponse;
import ptithcm.datt.WarehouseManager.model.Account;
import ptithcm.datt.WarehouseManager.model.Role;
import ptithcm.datt.WarehouseManager.model.Staff;
import ptithcm.datt.WarehouseManager.repository.AccountRepository;
import ptithcm.datt.WarehouseManager.repository.RoleRepository;
import ptithcm.datt.WarehouseManager.request.ChangePasswordRequest;

import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MailService emailService;
    @NonFinal
    protected static final String SIGNER_KEY = "CYG0G6yaS0rd/FExfwMHVIeyPmwv/FZphommJCN8CwCVFv/IwJIAWiMcPxWzYkF6";
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    StaffService staffService;
    @Autowired
    SecurityConfig securityConfig;

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    public Account createAccount(AccountRequest accountDTO) {
        Role role = roleRepository.findById(accountDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Account account = new Account();
        account.setEmail(accountDTO.getEmail());
        account.setUsername(accountDTO.getUsername());
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        account.setRole(role);

        Staff newStaff = new Staff();
        newStaff.setStaffName(accountDTO.getUsername());
        newStaff.setEmail(accountDTO.getEmail());
        Staff savedStaff = staffService.createStaff(newStaff);
        account.setStaff(savedStaff);

        account.setCreateAt(LocalDateTime.now());
        return accountRepository.save(account);
    }


    public List<Account> addStaff(List<StaffRequest> staffRequests) throws MessagingException {
        List<Account> accounts = new ArrayList<>();

        for (StaffRequest staffRequest : staffRequests) {
            Role role = roleRepository.findByRoleName(staffRequest.getRoleName())
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            Account account = new Account();
            account.setCreateAt(LocalDateTime.now());
            account.setUsername(staffRequest.getUsername());
            account.setEmail(staffRequest.getEmail());
            account.setStatus("active");

            String randomPassword = generateRandomPassword();
            account.setPassword(passwordEncoder.encode(randomPassword));
            account.setRole(role);

            Staff newStaff = new Staff();
            newStaff.setStaffName(staffRequest.getStaffName());
            newStaff.setPhone_number(staffRequest.getPhoneNumber());
            newStaff.setEmail(staffRequest.getEmail());
            newStaff.setImg(staffRequest.getPicture());
            newStaff.setAddress(staffRequest.getAddress());
            newStaff.setDob(staffRequest.getDob());
            newStaff.setHiredDate(LocalDateTime.now());
            newStaff.setIsEnable(true);

            Staff savedStaff = staffService.createStaff(newStaff);
            account.setStaff(savedStaff);

            Account savedAccount = accountRepository.save(account);
            accounts.add(savedAccount);

            // Send email with unencoded password
            String subject = "Welcome to Warehouse - Account Information";
            String content = "<!DOCTYPE html>"
                    + "<html lang=\"vi\">"
                    + "<head>"
                    + "    <meta charset=\"UTF-8\">"
                    + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                    + "    <title>Thông Tin Tài Khoản Nhân Viên</title>"
                    + "    <style>"
                    + "        body { font-family: Arial, sans-serif; background-color: #f4f4f4; }"
                    + "        .container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
                    + "        .header { text-align: center; margin-bottom: 20px; }"
                    + "        .header h1 { color: #333333; font-size: 24px; }"
                    + "        .content { font-size: 16px; color: #333333; line-height: 1.5; }"
                    + "        .account-info { margin: 20px 0; font-weight: bold; text-align: center; color: #007bff; }"
                    + "        .footer { font-size: 14px; color: #888888; text-align: center; margin-top: 20px; }"
                    + "        .footer a { color: #007bff; text-decoration: none; }"
                    + "        .footer a:hover { text-decoration: underline; }"
                    + "    </style>"
                    + "</head>"
                    + "<body>"
                    + "    <div class=\"container\">"
                    + "        <div class=\"header\">"
                    + "            <h1>Thông Tin Tài Khoản Nhân Viên</h1>"
                    + "        </div>"
                    + "        <div class=\"content\">"
                    + "            <p>Chào <strong>" + staffRequest.getEmail() + "</strong>,</p>"
                    + "            <p>Tài khoản của bạn đã được tạo thành công. Dưới đây là thông tin đăng nhập:</p>"
                    + "            <div class=\"account-info\">"
                    + "                <p><strong>Tên đăng nhập:</strong> " + staffRequest.getUsername() + "</p>"
                    + "                <p><strong>Mật khẩu:</strong> " + randomPassword + "</p>"
                    + "            </div>"
                    + "            <p>Vui lòng đổi mật khẩu sau khi đăng nhập lần đầu tiên.</p>"
                    + "        </div>"
                    + "        <div class=\"footer\">"
                    + "            <p>Trân trọng,</p>"
                    + "            <p><strong>WAREHOUSE</strong><br>"
                    + "            <a href=\"mailto:thithuytran74@gmail.com\">thithuytran74@gmail.com</a><br>"
                    + "            <a href=\"#\">CLOTHING STORE</a></p>"
                    + "        </div>"
                    + "    </div>"
                    + "</body>"
                    + "</html>";

            sendMail(staffRequest.getEmail(), subject, content);
        }

        return accounts;
    }




    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + request.getUsername()));

        passwordEncoder = new BCryptPasswordEncoder(10);
        boolean autheticated =  passwordEncoder.matches(request.getPassword(), account.getPassword());
        if(!autheticated) {
            throw new Exception("Unauthenticated");
        }
        String token = generateToken(request.getUsername());
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }

    private String generateToken(String username) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("datn.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("username", username)


                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            System.err.println("Cannot create token: " + e.getMessage());
            throw new RuntimeException("Cannot create token", e);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date exporyTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid(verified && exporyTime.after(new Date()))
                .build();

    }

    public void sendMail(String email, String subject, String content) throws MessagingException {
        emailService.sendMail(email, subject, content);
    }


}
