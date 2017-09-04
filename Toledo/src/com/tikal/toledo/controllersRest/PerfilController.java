package com.tikal.toledo.controllersRest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tikal.toledo.model.Perfil;
import com.tikal.toledo.security.PerfilDAO;
import com.tikal.toledo.security.UsuarioDAO;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.Util;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

	@Autowired
	PerfilDAO perfilimp;

	@Autowired
	UsuarioDAO usuarioimp;

	@RequestMapping(value = { "/crear" }, method = RequestMethod.POST, consumes = "Application/Json")
	public void crearPerfil(HttpServletRequest request, HttpServletResponse response, @RequestBody String json)
			throws IOException {
		if (ServicioSesion.verificarPermiso(request, usuarioimp, perfilimp, 5)) {
			AsignadorDeCharset.asignar(request, response);
			Perfil perfil = (Perfil) JsonConvertidor.fromJson(json, Perfil.class);
			perfilimp.crearPerfil(perfil);
		} else {
			response.sendError(403);
		}
	}

	@RequestMapping(value = { "/consultarTodos" }, method = RequestMethod.GET, produces = "application/json")
	public void consultarUsuarios(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (ServicioSesion.verificarPermiso(request, usuarioimp, perfilimp, 6)) {
			AsignadorDeCharset.asignar(request, response);
			this.consultar(response);
		}else{
			response.sendError(403);
		}
	}

	@RequestMapping(value = { "/actualiza" }, method = RequestMethod.POST, consumes = "Application/Json")
	public void actualizarUsuario(HttpServletRequest request, HttpServletResponse response, @RequestBody String json)
			throws IOException {
		if (ServicioSesion.verificarPermiso(request, usuarioimp, perfilimp, 6)) {
			AsignadorDeCharset.asignar(request, response);
			Perfil perfil = (Perfil) JsonConvertidor.fromJson(json, Perfil.class);
			Perfil aux = perfilimp.consultarPerfilPorId(perfil.getId());
			String nombrePerfilViejo = aux.getTipo();
			perfilimp.actulizaPerfil(perfil);
			usuarioimp.actualizarUsuarios(nombrePerfilViejo, perfil.getTipo());
		} else {
			response.sendError(403);
		}

	}

	@RequestMapping(value = { "/elimina" }, method = RequestMethod.POST, consumes = "Application/Json")
	public void eliminarUsuario(HttpServletRequest request, HttpServletResponse response, @RequestBody String json)
			throws IOException {
		if(Util.verificarPermiso(request, usuarioimp, perfilimp, 5,6)){
		AsignadorDeCharset.asignar(request, response);
		Perfil perfil = (Perfil) JsonConvertidor.fromJson(json, Perfil.class);
		perfilimp.eliminarPerfil(perfil.getTipo());
		}else{
			response.sendError(403);
		}
	}
	
	@RequestMapping(value = { "/consultarTodosU" }, method = RequestMethod.GET, produces = "application/json")
	public void consultarPerfiles(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (Util.verificarPermiso(request, usuarioimp, perfilimp, 5,6)) {
			AsignadorDeCharset.asignar(request, response);
			this.consultar(response);
		}else{
			response.sendError(403);
		}
	}
	
	private void consultar(HttpServletResponse response) throws IOException{
		List<Perfil> lista = perfilimp.consultarPerfiles();
		response.getWriter().println(JsonConvertidor.toJson(lista));
	}

}
