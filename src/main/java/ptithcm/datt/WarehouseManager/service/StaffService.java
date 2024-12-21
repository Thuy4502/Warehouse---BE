package ptithcm.datt.WarehouseManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.model.Account;
import ptithcm.datt.WarehouseManager.model.Staff;
import ptithcm.datt.WarehouseManager.repository.AccountRepository;
import ptithcm.datt.WarehouseManager.repository.StaffRepository;

import java.util.List;

@Service
public class StaffService {
    @Autowired
    StaffRepository staffRepository;

    @Autowired
    AccountRepository accountRepository;

    public Staff createStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    public  Staff findStaffByAccountId(Long accountId) {
        return staffRepository.findStaffByUserId(accountId);
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Staff updateStaff(Long staffId, Staff staffRequest) {
        Staff existingStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        Account account = accountRepository.findByStaff(existingStaff)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if(staffRequest.getIsEnable() == false) {
            account.setStatus("inactive");
            accountRepository.save(account);
        }
        else {
            account.setStatus("active");
            accountRepository.save(account);
        }
        if (staffRequest.getStaffName() != null && !staffRequest.getStaffName().equals(existingStaff.getStaffName())) {
            existingStaff.setStaffName(staffRequest.getStaffName());
        }

        if (staffRequest.getAddress() != null && !staffRequest.getAddress().equals(existingStaff.getAddress())) {
            existingStaff.setAddress(staffRequest.getAddress());
        }

        if (staffRequest.getPhoneNumber() != null && !staffRequest.getPhoneNumber().equals(existingStaff.getPhoneNumber())) {
            existingStaff.setPhoneNumber(staffRequest.getPhoneNumber());
        }

        if(staffRequest.getEmail() != null && !existingStaff.getEmail().equals(existingStaff.getEmail())) {
            existingStaff.setEmail(staffRequest.getEmail());
        }

        if(staffRequest.getDob() != null && !existingStaff.getDob().equals(existingStaff.getDob())) {
            existingStaff.setDob(staffRequest.getDob());
        }

        if (staffRequest.getImg() != null) {
            existingStaff.setImg(staffRequest.getImg());
        }

        if (staffRequest.getIsEnable() != null && !staffRequest.getIsEnable().equals(existingStaff.getIsEnable())) {
            existingStaff.setIsEnable(staffRequest.getIsEnable());
        }

        return staffRepository.save(existingStaff);
    }

    public Staff changeStaffStatus(Long staffId, Staff staffRequest) {
        Staff existingStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        Account account = accountRepository.findByStaff(existingStaff)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if(staffRequest.getIsEnable() == false) {
            account.setStatus("inactive");
            accountRepository.save(account);
        }
        else {
            account.setStatus("active");
            accountRepository.save(account);
        }
        if (staffRequest.getIsEnable() != null && !staffRequest.getIsEnable().equals(existingStaff.getIsEnable())) {
            existingStaff.setIsEnable(staffRequest.getIsEnable());
        }
        return staffRepository.save(existingStaff);
    }

    public boolean checkEmailExist(String email) {
        Staff user = staffRepository.findByEmail(email);
        if(user != null){
            return true;
        }
        return false;
    }


}
