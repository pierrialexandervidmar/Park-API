package com.sistema.parkapi.web.dto.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.sistema.parkapi.entity.Usuario;
import com.sistema.parkapi.web.dto.UsuarioCreateDto;
import com.sistema.parkapi.web.dto.UsuarioResponseDto;

public class UsuarioMapper {
	
	public static Usuario toUsuario(UsuarioCreateDto createDto) {
		return new ModelMapper().map(createDto, Usuario.class);
	}
	
	
	public static UsuarioResponseDto toDto(Usuario usuario) {
		String role = usuario.getRole().name().substring("ROLE_".length());
		PropertyMap<Usuario, UsuarioResponseDto>()
		return new ModelMapper().map(usuario, UsuarioResponseDto.class);
	}
}
