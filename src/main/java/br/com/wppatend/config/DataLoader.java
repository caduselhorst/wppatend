package br.com.wppatend.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.error.MissingEnvironmentVariableException;

import br.com.wppatend.entities.Permission;
import br.com.wppatend.entities.Role;
import br.com.wppatend.entities.User;
import br.com.wppatend.repositories.PermissionRepository;
import br.com.wppatend.repositories.RoleRepository;
import br.com.wppatend.repositories.UserRepository;

@Component
public class DataLoader implements ApplicationRunner {
	
	private RoleRepository roleRepository;
	private PermissionRepository permissionRepository;
	private UserRepository userRepository;
	
	
	private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    public DataLoader(RoleRepository roleRepository,
    		PermissionRepository permissionRepository,
    		UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }
    
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		logger.info("Server date: " + new Date());
    	
    	if(!args.containsOption("config.app")) {
    		logger.error(String.format("Parâmetro [%1$s] ausente", "api.pessoafisica"));
    		throw new Exception("Parâmetro de sistema ausente na inicialização", 
    				new MissingEnvironmentVariableException("Parametro [config.app] não informado."));
    	}
    	
    	if(roleRepository.findById(Long.parseLong("1")).equals(Optional.empty())) {
			
			Permission permRole = new Permission();
			permRole.setId(Long.parseLong("1"));
			permRole.setName("Acesso cadastro de funções");
			
			Permission permUser = new Permission();
			permUser.setId(Long.parseLong("2"));
			permUser.setName("Acesso cadastro de usuários");
			
			Permission pRole = permissionRepository.save(permRole);
			Permission pUser = permissionRepository.save(permUser);
			
			Role rAdmin = new Role();
			rAdmin.setNome("Administrador");
			rAdmin.setPermissions(new ArrayList<>());
			rAdmin.getPermissions().add(pRole);
			rAdmin.getPermissions().add(pUser);
			
			Role rOper = new Role();
			rOper.setNome("Operação");
						
			Role r = roleRepository.save(rAdmin);
			roleRepository.save(rOper);
			
			List<Role> roles = new ArrayList<>();
			roles.add(r);
			
			User admin = new User();
			admin.setName("Usuário administrador");
			admin.setPassword(new BCryptPasswordEncoder().encode("..suporte"));
			admin.setRoles(roles);
			admin.setUsername("admin");
			
			userRepository.save(admin);
		} else {
			logger.info("Initial data has previosly loaded");
		}  	
    }

}
