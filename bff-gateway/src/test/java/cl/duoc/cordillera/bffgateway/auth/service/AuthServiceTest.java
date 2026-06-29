package cl.duoc.cordillera.bffgateway.auth.service;

import cl.duoc.cordillera.bffgateway.auth.dto.AuthResponseDTO;
import cl.duoc.cordillera.bffgateway.auth.dto.LoginRequestDTO;
import cl.duoc.cordillera.bffgateway.auth.entity.Usuario;
import cl.duoc.cordillera.bffgateway.auth.repository.UsuarioRepository;
import cl.duoc.cordillera.bffgateway.exception.CustomUnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthService authService;

    private Usuario mockGerente;
    private Usuario mockAdmin;

    @BeforeEach
    void setUp() {
        mockGerente = Usuario.builder()
                .id("USR-001")
                .usuario("a.gatica@cordillera.cl")
                .contrasena("gerencia2026")
                .nombre("A. Gatica")
                .rol("GERENTE_GENERAL")
                .area("Gerencia General")
                .build();

        mockAdmin = Usuario.builder()
                .id("USR-002")
                .usuario("admin.valdivia@cordillera.cl")
                .contrasena("admin123")
                .nombre("Admin Valdivia")
                .rol("ADMINISTRADOR")
                .area("Administración")
                .sucursalId(2)
                .build();
    }

    @Test
    void autenticar_comoGerenteGeneral_retornaTokenYRolCorrecto() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsuario("a.gatica@cordillera.cl");
        request.setContrasena("gerencia2026");

        when(usuarioRepository.findByUsuarioAndContrasena("a.gatica@cordillera.cl", "gerencia2026"))
                .thenReturn(Optional.of(mockGerente));

        AuthResponseDTO response = authService.autenticar(request);

        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getNombre()).isEqualTo("A. Gatica");
        assertThat(response.getRol()).isEqualTo("GERENTE_GENERAL");
        assertThat(response.getArea()).isEqualTo("Gerencia General");
    }

    @Test
    void autenticar_comoAdministrador_retornaTokenYRolCorrecto() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsuario("admin.valdivia@cordillera.cl");
        request.setContrasena("admin123");

        when(usuarioRepository.findByUsuarioAndContrasena("admin.valdivia@cordillera.cl", "admin123"))
                .thenReturn(Optional.of(mockAdmin));

        AuthResponseDTO response = authService.autenticar(request);

        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getRol()).isEqualTo("ADMINISTRADOR");
    }

    @Test
    void autenticar_cadaLlamadaGerenteGeneraTokenUnico() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsuario("a.gatica@cordillera.cl");
        request.setContrasena("gerencia2026");

        when(usuarioRepository.findByUsuarioAndContrasena("a.gatica@cordillera.cl", "gerencia2026"))
                .thenReturn(Optional.of(mockGerente));

        String token1 = authService.autenticar(request).getToken();
        String token2 = authService.autenticar(request).getToken();

        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    void autenticar_usuarioValidoConClaveIncorrecta_lanzaExcepcion() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsuario("a.gatica@cordillera.cl");
        request.setContrasena("claveEquivocada");

        when(usuarioRepository.findByUsuarioAndContrasena("a.gatica@cordillera.cl", "claveEquivocada"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.autenticar(request))
                .isInstanceOf(CustomUnauthorizedException.class)
                .hasMessageContaining("Credenciales inválidas");
    }

    @Test
    void autenticar_usuarioInexistente_lanzaExcepcion() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsuario("intruso@cordillera.cl");
        request.setContrasena("cualquierClave");

        when(usuarioRepository.findByUsuarioAndContrasena("intruso@cordillera.cl", "cualquierClave"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.autenticar(request))
                .isInstanceOf(CustomUnauthorizedException.class)
                .hasMessageContaining("Credenciales inválidas");
    }
}
