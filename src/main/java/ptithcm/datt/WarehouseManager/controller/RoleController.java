package ptithcm.datt.WarehouseManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.datt.WarehouseManager.model.Role;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("v1/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/getAll")
    public ResponseEntity<EntityResponse> getAllRole() {
        EntityResponse res = new EntityResponse<>();
        List<Role> roles = roleService.getAllRole();
        res.setData(roles);
        res.setCode(HttpStatus.OK.value());
        res.setStatus(HttpStatus.OK);
        res.setMessage("Get all role successfully");
        return new ResponseEntity<>(res,res.getStatus());
    }
}
