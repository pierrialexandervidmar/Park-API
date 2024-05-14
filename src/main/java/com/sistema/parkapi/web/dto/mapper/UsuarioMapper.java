package com.sistema.parkapi.web.dto.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.sistema.parkapi.entity.Usuario;
import com.sistema.parkapi.web.dto.UsuarioCreateDto;
import com.sistema.parkapi.web.dto.UsuarioResponseDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A classe {@code UsuarioMapper} fornece métodos estáticos para mapear entre objetos relacionados com usuário
 * e DTOs (Data Transfer Objects).
 * <p>
 * Esta classe facilita a conversão entre a entidade {@code Usuario} e seus respectivos objetos de transferência de dados,
 * simplificando as operações de transformação de dados entre as camadas de serviço e apresentação.
 * </p>
 *
 * @author Pierri Alexander Vidmar
 */
public class UsuarioMapper {

	/**
	 * Converte um objeto {@code UsuarioCreateDto} em um objeto {@code Usuario}.
	 *
	 * @param createDto o objeto {@code UsuarioCreateDto} a ser convertido
	 * @return um novo objeto {@code Usuario} convertido a partir do {@code UsuarioCreateDto} fornecido
	 */
	public static Usuario toUsuario(UsuarioCreateDto createDto) {
		return new ModelMapper().map(createDto, Usuario.class);
	}

	/**
	 * Converte um objeto {@code Usuario} em um objeto {@code UsuarioResponseDto}.
	 *
	 * @param usuario o objeto {@code Usuario} a ser convertido
	 * @return um novo objeto {@code UsuarioResponseDto} contendo os dados mapeados do {@code Usuario} fornecido
	 */
	public static UsuarioResponseDto toDto(Usuario usuario) {
		// Extrai o papel (role) do usuário e remove o prefixo "ROLE_"
		String role = usuario.getRole().name().substring("ROLE_".length());

		// Configuração personalizada para mapear a propriedade role
		PropertyMap<Usuario, UsuarioResponseDto> props = new PropertyMap<Usuario,UsuarioResponseDto>() {
			@Override
			protected void configure() {
				map().setRole(role);
			}
		};

		// Cria um novo ModelMapper e adiciona as configurações personalizadas
		ModelMapper mapper = new ModelMapper();
		mapper.addMappings(props); 

		// Retorna o objeto UsuarioResponseDto mapeado
		return mapper.map(usuario, UsuarioResponseDto.class);
	}

	public static List<UsuarioResponseDto> toListDto(List<Usuario> usuarios) {
		return usuarios.stream().map(user -> toDto(user)).collect(Collectors.toList());
	}
}
