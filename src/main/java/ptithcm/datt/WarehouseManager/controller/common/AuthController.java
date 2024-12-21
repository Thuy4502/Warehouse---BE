package ptithcm.datt.WarehouseManager.controller.common;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ptithcm.datt.WarehouseManager.model.Account;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.Staff;
import ptithcm.datt.WarehouseManager.response.ApiResponse;
import ptithcm.datt.WarehouseManager.response.AuthenticationResponse;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.request.AccountRequest;
import ptithcm.datt.WarehouseManager.request.AuthenticationRequest;
import ptithcm.datt.WarehouseManager.request.ChangePasswordRequest;
import ptithcm.datt.WarehouseManager.service.AccountService;
import ptithcm.datt.WarehouseManager.service.AuthService;
import ptithcm.datt.WarehouseManager.service.MailService;
import ptithcm.datt.WarehouseManager.service.StaffService;

@RestController
@RequestMapping("v1/api")
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    AccountService accountService;

    @Autowired
    StaffService staffService;

    @Autowired
    MailService mailService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerAccount(@RequestBody AccountRequest account) {
        ApiResponse res = new ApiResponse();
        HttpStatus httpStatus = null;
        try {
            Account savedAccount = authService.createAccount(account);
            if(savedAccount.getAccountId() > 0) {
                res.setCode(HttpStatus.CREATED.value());
                res.setMessage("Customer is created successfully for account: " + account.getUsername());
                res.setStatus(HttpStatus.CREATED);
                httpStatus = HttpStatus.CREATED;
            }
        }
        catch (Exception e) {
            System.out.println("error" + e.getMessage());
            res.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setMessage("An exception occured from server with exception: " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(res, httpStatus);

    }

    @PutMapping("/update/{staffId}")
    public ResponseEntity<EntityResponse> updateStaff(@PathVariable Long staffId, @RequestBody Staff staffRequest) {
        try {
            Staff updateStaff = staffService.updateStaff(staffId, staffRequest);
            EntityResponse<Staff> response = new EntityResponse<>();
            response.setData(updateStaff);
            response.setMessage("Staff updated successfully");
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            EntityResponse<Book> response = new EntityResponse<>();
            response.setData(null);
            response.setMessage("Error updating staff: " + e.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) throws Exception {
        AuthenticationResponse response = new AuthenticationResponse();

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            response.setMessage("Tên đăng nhập không được bỏ trống!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            response.setMessage("Mật khẩu không được bỏ trống!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        boolean accountExists = authService.checkIfAccountExists(request.getUsername());
        if (!accountExists) {
            response.setMessage("Tài khoản không tồn tại!");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        try {
            AuthenticationResponse result = authService.authenticate(request);
            return ResponseEntity.ok(result);
        } catch (BadCredentialsException e) {
            response.setMessage("Mật khẩu không chính xác!");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            response.setMessage("Đã xảy ra lỗi, vui lòng thử lại sau!");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/profile")
    public ResponseEntity<EntityResponse<Staff>> getProfile(@RequestHeader("Authorization") String jwt) {
        EntityResponse<Staff> res = new EntityResponse<>();
        if (jwt == null) {
            throw new RuntimeException("JWT token is missing");
        }

        Account account = accountService.findUserProfileByJwt(jwt);
        if (account == null) {
            res.setCode(HttpStatus.UNAUTHORIZED.value());
            res.setStatus(HttpStatus.UNAUTHORIZED);
            res.setMessage("Invalid JWT token");
            return new ResponseEntity<>(res, res.getStatus());
        }

        Staff staff = staffService.findStaffByAccountId(account.getAccountId());
        if (staff == null) {
            res.setCode(HttpStatus.NOT_FOUND.value());
            res.setStatus(HttpStatus.NOT_FOUND);
            res.setMessage("Staff not found for the given account");
            return new ResponseEntity<>(res, res.getStatus());
        }

        res.setData(staff);
        res.setCode(HttpStatus.OK.value());
        res.setStatus(HttpStatus.OK);
        res.setMessage("Get staff successfully");
        return new ResponseEntity<>(res, res.getStatus());
    }

    @PutMapping("/change/password")
    public ResponseEntity<ApiResponse> changePasswordByJwt(@RequestHeader("Authorization") String jwt, @RequestBody ChangePasswordRequest rq){
        ApiResponse res = new ApiResponse();
        try{
            Account update = accountService.changePassword(jwt,rq);
            res.setCode(HttpStatus.OK.value());
            res.setStatus(HttpStatus.OK);
            res.setMessage("Change password successfully");
        }catch (Exception e){
            res.setCode(HttpStatus.CONFLICT.value());
            res.setStatus(HttpStatus.CONFLICT);
            res.setMessage("Error changing password  " + e.getMessage());
        }
        return new ResponseEntity<>(res, res.getStatus());
    }

    @PutMapping("forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword( @RequestBody AccountRequest rq) throws Exception {
        ApiResponse res = new ApiResponse();

        String otp = authService.generateRandomPassword();
        String subject = "Xác Minh Địa Chỉ Email để đổi mật khẩu - Mật khẩu";
        String content = "<!DOCTYPE html>"
                + "<html lang=\"vi\">"
                + "<head>"
                + "    <meta charset=\"UTF-8\">"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "    <title>Khôi Phục Mật Khẩu</title>"
                + "    <style>"
                + "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
                + "        .container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; border: 1px solid #dddddd; border-radius: 5px; padding: 20px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
                + "        .header { text-align: center; margin-bottom: 20px; }"
                + "        .header h1 { color: #333333; font-size: 24px; margin: 0; }"
                + "        .content { font-size: 16px; color: #333333; line-height: 1.5; }"
                + "        .otp-code { display: block; font-size: 24px; font-weight: bold; color: #007bff; text-align: center; margin: 20px 0; }"
                + "        .footer { font-size: 14px; color: #888888; text-align: center; margin-top: 20px; }"
                + "        .footer a { color: #007bff; text-decoration: none; }"
                + "        .footer a:hover { text-decoration: underline; }"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <div class=\"container\">"
                + "        <div class=\"header\">"
                + "            <h1>Khôi Phục Mật Khẩu</h1>"
                + "        </div>"
                + "        <div class=\"content\">"
                + "            <p>Chào <strong>" + rq.getEmail() + "</strong>,</p>"
                + "            <p>Chúng tôi đã nhận được yêu cầu khôi phục mật khẩu từ bạn. Dưới đây là mật khẩu mới của bạn. Vui lòng đăng nhập với mã ở bên dưới để tiếp tục sử dụng</p>"
                + "            <span class=\"otp-code\">" + otp + "</span>"
                + "            <p>Nếu bạn gặp bất kỳ vấn đề nào hoặc cần hỗ trợ thêm, đừng ngần ngại liên hệ với chúng tôi qua địa chỉ email này hoặc gọi điện thoại cho chúng tôi tại <strong>0363000451</strong>.</p>"
                + "        </div>"
                + "        <div class=\"footer\">"
                + "            <p>Trân trọng,</p>"
                + "            <p><strong>WATCHSHOP</strong><br>"
                + "            <a href=\"mailto:sontrinh2507@gmail.com\">sontrinh2507@gmail.com</a><br>"
                + "            <a href=\"#\">WATCHSHOP</a></p>"
                + "        </div>"
                + "    </div>"
                + "</body>"
                + "</html>";

        boolean checkEmail = staffService.checkEmailExist(rq.getEmail());

        if (checkEmail) {
            mailService.sendMail(rq.getEmail(), subject, content);
            accountService.updatePassword(otp, rq.getEmail());
            res.setMessage("Check email successfully");
            res.setStatus(HttpStatus.OK);
            res.setCode(HttpStatus.OK.value());
        }else{
            res.setMessage("Email not exist");
            res.setStatus(HttpStatus.OK);
            res.setCode(HttpStatus.OK.value());
        }
        return new ResponseEntity<>(res, res.getStatus());
    }

}
