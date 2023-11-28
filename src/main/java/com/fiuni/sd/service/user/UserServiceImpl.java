// package com.fiuni.sd.service.user;

// import java.util.List;
// import java.util.ArrayList;
// import java.util.HashSet;

// import org.springframework.data.domain.Page;
// import org.springframework.stereotype.Service;
// import org.springframework.data.domain.Pageable;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cache.annotation.Cacheable;

// import com.fiuni.sd.dao.IUserDao;
// import com.fiuni.sd.dto.role.RoleDto;
// import com.fiuni.sd.dto.user.UserDto;
// import com.fiuni.sd.domain.Role;
// import com.fiuni.sd.domain.User;
// import com.fiuni.sd.dto.user.UserListDto;
// import com.fiuni.sd.exception.ResourceNotFoundException;
// import com.fiuni.sd.service.base.BaseServiceImpl;
// import com.fiuni.sd.utils.Setting;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.Optional;

// @Service
// public class UserServiceImpl {

// private final User user;

// @Autowired
// public User(User user) {
// this.userRepository = userRepository;
// }

// public Optional<User> getUserByEmail(String email) {
// return userRepository.findByEmail(email);
// }

// public User createUser(User user) {
// // Aquí podrías implementar la lógica de negocio antes de guardar al usuario
// en el repositorio
// // Por ejemplo, verificar la existencia de un usuario con el mismo email
// antes de guardar

// // Si deseas realizar alguna validación o transformación de datos antes de
// guardar, este sería el lugar adecuado

// return userRepository.save(user);
// }

// // Puedes agregar más métodos según las necesidades de tu aplicación, como
// updateUser, deleteUser, etc.
// }
