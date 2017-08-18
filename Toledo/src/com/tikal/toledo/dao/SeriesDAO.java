package com.tikal.toledo.dao;

public interface SeriesDAO {
	int getSerieVenta();
	int getSerieFactura();
	void incSerieVenta();
	void incSerieFactura();
	void crear();
}
