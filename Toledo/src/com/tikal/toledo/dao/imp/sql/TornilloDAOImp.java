package com.tikal.toledo.dao.imp.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.database.sql.Conexion;
import com.tikal.toledo.model.Tornillo;
import com.tikal.toledo.util.Parseador;

public class TornilloDAOImp implements TornilloDAO {

	@Autowired
	Conexion conn;

	@Override
	public String guardar(Tornillo t) {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		Tornillo aux= this.cargar(t.getId());
		if(aux==null){
			//INSERT INTO `t_tornillo` (`id`, `descuento`, `existencia`, `ganancia`, `impuesto`, `marca`, `maximo`, `minimo`, `mayoreo`, `medidas`, `nombre`, `precioCredito`, `precioMayoreo`, `precioMostrador`, `precioReferencia`, `proveedor`, `tipo`) VALUES ('1', '1', '100000', '1', '1', 'Toledo', '1', '1', '1', '1', 'Tornillo de Prueba', '1', '1', '1', '1', '', '1');
		String query = "INSERT INTO t_tornillo (descuento, existencia, ganancia, impuesto, marca, maximo, minimo, mayoreo, medidas, nombre, precioCredito, PrecioMayoreo, precioMostrador, precioReferencia, proveedor, tipo, clave) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement statementCreate = conn.c.prepareStatement(query)) {
			statementCreate.setFloat(1, t.getDescuento());
			statementCreate.setInt(2, t.getExistencia());
			statementCreate.setFloat(3, t.getGanancia());
			statementCreate.setFloat(4, t.getImpuesto());
			String marca=t.getMarca();
			if(marca==null){
				marca="";
			}
			statementCreate.setString(5, marca);
			statementCreate.setInt(6, t.getMaximo());
			statementCreate.setInt(7, t.getMinimo());
			String mayoreo="0";
			if(t.getMayoreo()!=null){
				mayoreo= t.getMayoreo();
			}
			statementCreate.setInt(8, Integer.parseInt(mayoreo));
			statementCreate.setString(9, t.getMedidas());
			statementCreate.setString(10, t.getNombre());
			statementCreate.setFloat(11, t.getPrecioCredito());
			statementCreate.setFloat(12, t.getPrecioMayoreo());
			statementCreate.setFloat(13, t.getPrecioMostrador());
			statementCreate.setFloat(14, t.getPrecioReferencia());
			statementCreate.setString(15, t.getProveedor());
			statementCreate.setInt(16, 1);
			statementCreate.setString(17, t.getClave());
			statementCreate.executeUpdate();
			return "OK";
		} catch (SQLException e) {
			return e.getMessage();
		}
		}else{
			return this.update(t);
		}
	}

	@Override
	public void guardar(List<Tornillo> lista) {
		for(Tornillo t:lista){
			this.guardar(t);
		}
	}

	@Override
	public Tornillo cargar(Long id) {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String selectsql="Select * from t_tornillo where id="+id;
		try (ResultSet rs = conn.c.prepareStatement(selectsql).executeQuery()) {
			while(rs.next()){
				Tornillo t= new Tornillo();
				t.setClave(rs.getString("clave"));
				t.setDescuento(rs.getFloat("descuento"));
				t.setExistencia(rs.getInt("existencia"));
				t.setGanancia(rs.getFloat("ganancia"));
				t.setId(rs.getLong("id"));
				t.setImpuesto(rs.getFloat("impuesto"));
				t.setMarca(rs.getString("marca"));
				t.setMaximo(rs.getInt("maximo"));
				t.setMinimo(rs.getInt("minimo"));
				t.setMayoreo(rs.getString("mayoreo"));
				t.setMedidas(rs.getString("medidas"));
				t.setNombre(rs.getString("nombre"));
				t.setPrecioCredito(rs.getFloat("precioCredito"));
				t.setPrecioMayoreo(rs.getFloat("precioMayoreo"));
				t.setPrecioMostrador(rs.getFloat("precioMostrador"));
				t.setPrecioReferencia(rs.getFloat("precioReferencia"));
				t.setProveedor(rs.getString("proveedor"));
				t.setTipo(1);
				return t;
			}
		}catch(SQLException e){
			
		}
		return null;
	}

	@Override
	public List<Tornillo> buscar(String search) {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String selectsql="Select * from t_tornillo where nombre LIKE '%"+search+"%' or clave LIKE '%"+search+"%' or medidas LIKE '%"+search+"%'";
		try (ResultSet rs = conn.c.prepareStatement(selectsql).executeQuery()) {
			List<Tornillo> lista = new ArrayList<Tornillo>();
			while(rs.next()){
				lista.add(this.llenaTornillo(rs));
			}
			return lista;
		}catch(SQLException e){
			
		}
		return null;
	}

	@Override
	public Tornillo buscarNombre(Tornillo t) {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String selectsql="Select * from t_tornillo where nombre LIKE '%?%'";
		try (ResultSet rs = conn.c.prepareStatement(selectsql).executeQuery()) {
//	        out.print("Last 10 visits:\n");
//	        while (rs.next()) {
//	          String savedIp = rs.getString("user_ip");
//	          String timeStamp = rs.getString("timestamp");
//	          out.print("Time: " + timeStamp + " Addr: " + savedIp + "\n");
//	        }
//	      }	L
			List<Tornillo> lista = new ArrayList<Tornillo>();
			while(rs.next()){
				lista.add(this.llenaTornillo(rs));
				return this.llenaTornillo(rs);
			}
		}catch(SQLException e){
			
		}
		return null;
	}

	@Override
	public List<Tornillo> todos() {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String selectsql="Select * from t_tornillo";
		try (ResultSet rs = conn.c.prepareStatement(selectsql).executeQuery()) {
			List<Tornillo> lista = new ArrayList<Tornillo>();
			while(rs.next()){
				lista.add(this.llenaTornillo(rs));
			}
			return lista;
		}catch(SQLException e){
			
		}
		return null;
	}

	@Override
	public List<Tornillo> page(int p) throws SQLException {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		p=p-1;
		int offset= p*25;
		String selectsql="Select * from t_tornillo LIMIT 25 OFFSET "+offset;
		ResultSet rs = conn.c.prepareStatement(selectsql).executeQuery();
			List<Tornillo> lista = new ArrayList<Tornillo>();
			while(rs.next()){
				lista.add(this.llenaTornillo(rs));
			}
			return lista;
		
	}

	@Override
	public int total() {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String selectsql="Select COUNT(id) as total from t_tornillo";
		try (ResultSet rs = conn.c.prepareStatement(selectsql).executeQuery()) {
			while(rs.next()){
				return rs.getInt("total");
			}
		}catch(SQLException e){
			
		}
		
		return 0;
	}

	@Override
	public void alv() {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String selectsql="select * from t_tornillo where clave is null";
//		String selectsql="DELETE from t_tornillo where 1";
//		try (ResultSet rs = conn.c.prepareStatement(selectsql).executeQuery()) {
//		}catch(SQLException e){
//		}
		try (ResultSet rs = conn.c.prepareStatement(selectsql).executeQuery()) {
			while(rs.next()){
				Tornillo t= this.llenaTornillo(rs);
				t.setClave(Parseador.getClave(t.getNombre(), t.getMedidas()));
				this.guardar(t);
			}
		}catch(SQLException e){
		}
	}

	@Override
	public void formula(float impuesto, float descuento, float ganancia) {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Tornillo llenaTornillo(ResultSet rs) throws SQLException{
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		Tornillo t= new Tornillo();
		t.setClave(rs.getString("clave"));
		t.setDescuento(rs.getFloat("descuento"));
		t.setExistencia(rs.getInt("existencia"));
		t.setGanancia(rs.getFloat("ganancia"));
		t.setId(rs.getLong("id"));
		t.setImpuesto(rs.getFloat("impuesto"));
		t.setMarca(rs.getString("marca"));
		t.setMaximo(rs.getInt("maximo"));
		t.setMinimo(rs.getInt("minimo"));
		t.setMayoreo(rs.getString("mayoreo"));
		t.setMedidas(rs.getString("medidas"));
		t.setNombre(rs.getString("nombre"));
		t.setPrecioCredito(rs.getFloat("precioCredito"));
		t.setPrecioMayoreo(rs.getFloat("precioMayoreo"));
		t.setPrecioMostrador(rs.getFloat("precioMostrador"));
		t.setPrecioReferencia(rs.getFloat("precioReferencia"));
		t.setProveedor(rs.getString("proveedor"));
		t.setTipo(1);
		return t;
	}
	
	private String update(Tornillo t){
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String query = "UPDATE t_tornillo set descuento= ?,existencia =?,ganancia=?,impuesto=?,marca=?,maximo=?,minimo=?,mayoreo=?,medidas=?,nombre=?,precioCredito=?,precioMayoreo=?,precioMostrador=?,precioReferencia=?,proveedor=?,clave=? WHERE t_tornillo.id="+t.getId();
		try (PreparedStatement statementCreate = conn.c.prepareStatement(query)) {
			statementCreate.setFloat(1, t.getDescuento());
			statementCreate.setInt(2, t.getExistencia());
			statementCreate.setFloat(3, t.getGanancia());
			statementCreate.setFloat(4, t.getImpuesto());
			String marca=t.getMarca();
			if(marca==null){
				marca="";
			}
			statementCreate.setString(5, marca);
			statementCreate.setInt(6, t.getMaximo());
			statementCreate.setInt(7, t.getMinimo());
			statementCreate.setInt(8, Integer.parseInt(t.getMayoreo()));	
			statementCreate.setString(9, t.getMedidas());
			statementCreate.setString(10, t.getNombre());
			statementCreate.setFloat(11, t.getPrecioCredito());
			statementCreate.setFloat(12, t.getPrecioMayoreo());
			statementCreate.setFloat(13, t.getPrecioMostrador());
			statementCreate.setFloat(14, t.getPrecioReferencia());
			statementCreate.setString(15, t.getProveedor());
			statementCreate.setString(16,t.getClave());
			statementCreate.executeUpdate();
			return "ok";
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public void eliminar(Tornillo t) throws SQLException {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String selectsql="DELETE from t_tornillo where t_tornillo.id="+t.getId();
		PreparedStatement rs = conn.c.prepareStatement(selectsql);
		rs.executeUpdate();
	}

}
