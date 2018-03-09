package com.tikal.toledo.dao.imp.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;

import com.tikal.toledo.dao.ProductoDAO;
import com.tikal.toledo.database.sql.Conexion;
import com.tikal.toledo.model.Producto;

public class ProductoDAOImp implements ProductoDAO{

	@Autowired
	Conexion conn;
	
	@Override
	public String guardar(Producto p) throws SQLException {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		Producto aux= this.cargar(p.getId());
		if(aux==null){
			//INSERT INTO `t_tornillo` (`id`, `descuento`, `existencia`, `ganancia`, `impuesto`, `marca`, `maximo`, `minimo`, `mayoreo`, `medidas`, `nombre`, `precioCredito`, `precioMayoreo`, `precioMostrador`, `precioReferencia`, `proveedor`, `tipo`) VALUES ('1', '1', '100000', '1', '1', 'Toledo', '1', '1', '1', '1', 'Tornillo de Prueba', '1', '1', '1', '1', '', '1');
		String query = "INSERT INTO t_producto (descuento, existencia, ganancia, impuesto, marca, maximo, minimo, nombre, precioCredito, PrecioMayoreo, precioMostrador, precioReferencia, proveedor, tipo, clave) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statementCreate = conn.c.prepareStatement(query);
			statementCreate.setFloat(1, p.getDescuento());
			statementCreate.setFloat(2, p.getExistencia());
			statementCreate.setFloat(3, p.getGanancia());
			statementCreate.setFloat(4, p.getImpuesto());
			String marca=p.getMarca();
			if(marca==null){
				marca="";
			}
			statementCreate.setString(5, marca);
			statementCreate.setInt(6, p.getMaximo());
			statementCreate.setInt(7, p.getMinimo());
			statementCreate.setString(8, p.getNombre());
			statementCreate.setFloat(9, p.getPrecioCredito());
			statementCreate.setFloat(10, p.getPrecioMayoreo());
			statementCreate.setFloat(11, p.getPrecioMostrador());
			statementCreate.setFloat(12, p.getPrecioReferencia());
			statementCreate.setString(13, p.getProveedor());
			statementCreate.setInt(14, 1);
			statementCreate.setString(15, p.getClave());
			statementCreate.executeUpdate();
			return "ok";
		}else{
			return this.update(p);
		}
	}

	@Override
	public void guardar(List<Producto> lista) throws SQLException {
		for(Producto p:lista){
			this.guardar(p);
		}
		
	}

	@Override
	public Producto cargar(Long id) {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String selectsql="Select * from t_producto where id="+id;
		try (ResultSet rs = conn.c.prepareStatement(selectsql).executeQuery()) {
			while(rs.next()){
				Producto t= new Producto();
				t.setClave(rs.getString("clave"));
				t.setDescuento(rs.getFloat("descuento"));
				t.setExistencia(rs.getInt("existencia"));
				t.setGanancia(rs.getFloat("ganancia"));
				t.setId(rs.getLong("id"));
				t.setImpuesto(rs.getFloat("impuesto"));
				t.setMarca(rs.getString("marca"));
				t.setMaximo(rs.getInt("maximo"));
				t.setMinimo(rs.getInt("minimo"));
				t.setNombre(rs.getString("nombre"));
				t.setPrecioCredito(rs.getFloat("precioCredito"));
				t.setPrecioMayoreo(rs.getFloat("precioMayoreo"));
				t.setPrecioMostrador(rs.getFloat("precioMostrador"));
				t.setPrecioReferencia(rs.getFloat("precioReferencia"));
				t.setProveedor(rs.getString("proveedor"));
				t.setTipo(0);
				return t;
			}
		}catch(SQLException e){
			
		}
		return null;
	}

	@Override
	public List<Producto> buscar(String search) {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String selectsql="Select * from t_producto where nombre LIKE '%"+search+"%' or clave LIKE '%"+search+"%'";
		try (ResultSet rs = conn.c.prepareStatement(selectsql).executeQuery()) {
			List<Producto> lista = new ArrayList<Producto>();
			while(rs.next()){
				lista.add(this.llenaProducto(rs));
			}
			return lista;
		}catch(SQLException e){
			
		}
		return null;
	}

	@Override
	public List<Producto> todos() {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String selectsql="Select * from t_producto";
		try (ResultSet rs = conn.c.prepareStatement(selectsql).executeQuery()) {
			List<Producto> lista = new ArrayList<Producto>();
			while(rs.next()){
				lista.add(this.llenaProducto(rs));
			}
			return lista;
		}catch(SQLException e){
			
		}
		return null;
	}

	@Override
	public List<Producto> todos(int page) throws SQLException {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		page=page-1;
		int offset= page*25;
		String selectsql="Select * from t_producto LIMIT 25 OFFSET "+offset;
		ResultSet rs;
			rs = conn.c.prepareStatement(selectsql).executeQuery();
			List<Producto> lista = new ArrayList<Producto>();
			while(rs.next()){
				lista.add(this.llenaProducto(rs));
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
		String selectsql="Select COUNT(id) as total from t_producto";
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
		String selectsql="DELETE from t_producto where 1";
		try (ResultSet rs = conn.c.prepareStatement(selectsql).executeQuery()) {
		}catch(SQLException e){
		}
	}

	@Override
	public void formula(float impuesto, float descuento, float ganancia) {
		// TODO Auto-generated method stub
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private Producto llenaProducto(ResultSet rs) throws SQLException{
		Producto t= new Producto();
		t.setClave(rs.getString("clave"));
		t.setDescuento(rs.getFloat("descuento"));
		t.setExistencia(rs.getInt("existencia"));
		t.setGanancia(rs.getFloat("ganancia"));
		t.setId(rs.getLong("id"));
		t.setImpuesto(rs.getFloat("impuesto"));
		t.setMarca(rs.getString("marca"));
		t.setMaximo(rs.getInt("maximo"));
		t.setMinimo(rs.getInt("minimo"));
		t.setNombre(rs.getString("nombre"));
		t.setPrecioCredito(rs.getFloat("precioCredito"));
		t.setPrecioMayoreo(rs.getFloat("precioMayoreo"));
		t.setPrecioMostrador(rs.getFloat("precioMostrador"));
		t.setPrecioReferencia(rs.getFloat("precioReferencia"));
		t.setProveedor(rs.getString("proveedor"));
		t.setTipo(0);
		return t;
	}

	private String update(Producto t){
		String query = "UPDATE t_producto set descuento= ?,existencia =?,ganancia=?,impuesto=?,marca=?,maximo=?,minimo=?,nombre=?,precioCredito=?, precioMayoreo=?,precioMostrador=?,precioReferencia=?,proveedor=?, clave=? WHERE id="+t.getId();
		try (PreparedStatement statementCreate = conn.c.prepareStatement(query)) {
			statementCreate.setFloat(1, t.getDescuento());
			statementCreate.setFloat(2, t.getExistencia());
			statementCreate.setFloat(3, t.getGanancia());
			statementCreate.setFloat(4, t.getImpuesto());
			String marca=t.getMarca();
			if(marca==null){
				marca="";
			}
			statementCreate.setString(5, marca);
			statementCreate.setInt(6, t.getMaximo());
			statementCreate.setInt(7, t.getMinimo());
			statementCreate.setString(8, t.getNombre());
			statementCreate.setFloat(9, t.getPrecioCredito());
			statementCreate.setFloat(10, t.getPrecioMayoreo());
			statementCreate.setFloat(11, t.getPrecioMostrador());
			statementCreate.setFloat(12, t.getPrecioReferencia());
			statementCreate.setString(13, t.getProveedor());
			statementCreate.setString(14, t.getClave());
			statementCreate.executeUpdate();
			return "ok";
		} catch (SQLException e) {
			return e.getMessage();
		}
	}

	@Override
	public void eliminar(Producto p) throws SQLException {
		try{
		if(conn.c.isClosed()){
				conn.reconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String selectsql="DELETE from t_producto where t_producto.id="+p.getId();
		PreparedStatement ps = conn.c.prepareStatement(selectsql);
		ps.executeUpdate();
	}

}
