package com.deepakrohan.demo;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication {

	@Bean("data")
	public List<Permission> getData() {
		List<Permission> permissions = new ArrayList<>();
		permissions.add(Permission.builder().projectid("1001").projectname("hello-world").privilege("read").build());
//		permissions.add(Permission.builder().projectid("1001").projectname("hello-world").privilege("write").build());
//		permissions.add(Permission.builder().projectid("1003").projectname("pytorch").privilege("read").build());
//		permissions.add(Permission.builder().projectid("1003").projectname("pytorch").privilege("write").build());
//		permissions.add(Permission.builder().projectid("1004").projectname("tensorflow").privilege("read").build());
//		permissions.add(Permission.builder().projectid("1004").projectname("tensorflow").privilege("write").build());
		return permissions;
	}


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

@RestController
@RequestMapping(value ="/roles")
@Slf4j
class HelloController {

	@Autowired
	public HelloService service;

	@GetMapping()
	@ResponseBody
	public List<Permission> getRoles() {
		log.info("------Get Roles-------");
		return service.getKeys();
	}

	@PostMapping
	@ResponseBody
	public List<Permission> saveRoles(@RequestBody Permission input) {
		log.info("-------Save New Role, Value---------");
		return service.saveKeys(input);
	}

	@DeleteMapping(  "/{roleId}")
	@ResponseBody
	public List<Permission> deleteKey(@PathVariable String roleId) {
		log.info("--------Delete Req Called ------------");
		return service.removeKey(roleId);
	}

	@PostMapping("/{roleId}")
	public List<Permission> updateKey(@PathVariable String roleId, @RequestBody String val) {
		log.info("--------Update Req Called ------------");
		return service.updateKey(roleId, val);
	}

}

@Service
class HelloService {
	@Autowired
	public List<Permission> data;

	public List<Permission> getKeys() {
		return  data;
	}

	public List<Permission> saveKeys(Permission input) {
		data.add(input);
		return data;
	}

	public List<Permission> removeKey(String role) {
		data = data.stream()
				.filter(permission ->
				!permission.getProjectid().equals(role))
				.collect(Collectors.toList());

		return data;
	}

	public List<Permission> updateKey(String keyName, String val) {
		data.stream().filter(permission -> permission.getProjectid().equals(keyName)
		        && permission.getPrivilege().equals(val)
		        ).findFirst()
				.ifPresent(permission -> permission.setPrivilege(val));
		return data;
	}
}


@Data
@Builder
class Permission {
	private String projectid;
	private String projectname;
	private String privilege;
}
