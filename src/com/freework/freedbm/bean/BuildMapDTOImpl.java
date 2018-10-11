package com.freework.freedbm.bean;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.freework.freedbm.BaseDTO;
import com.freework.freedbm.dao.jdbcm.map.dto.MapDTO;
import com.freework.freedbm.dao.jdbcm.map.dto.MapDTOCfg;
import com.freework.freedbm.dao.jdbcm.map.dto.MapDTOUtil;
import com.freework.freedbm.util.Tool;
public class BuildMapDTOImpl implements BuildMapDTO {
	private DataSource dataSource=null;
	
	@Resource()
	public void setDataSource(DataSource dataSource) {
		this.dataSource=dataSource;
	}
	
	
	public MapDTOCfg getMapDTOCfg(String tableName){
		Connection conn=null;
		try{		
			conn=dataSource.getConnection();
			MapDTOCfg mapDTOCfg=MapDTOUtil.getTableInfo(conn, tableName);
			if(mapDTOCfg==null)
				return null;
			return mapDTOCfg;//this.mapDTO(request, mapDTOCfg);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
	}
	public MapDTO mapToDTO(String tableName,Map<String,?> map){
		Connection conn=null;
		try{		
			conn=dataSource.getConnection();

			MapDTO dto=(MapDTO) MapDTOUtil.mapToDto(conn, tableName,map);
			if(dto==null)
				return null;
			return dto;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	public MapDTO mapToUpdate(String tableName,Map<String,?> map){
			Connection conn=null;
			try{		
				conn=dataSource.getConnection();

				MapDTO dto=(MapDTO) MapDTOUtil.mapToUpdateDto(conn, tableName,map);
				if(dto==null)
					return null;
				return dto;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}finally{
				if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		
		
	}
	
	public MapDTO getMapDTO(String tableName){
		Connection conn=null;
		try{		
			conn=dataSource.getConnection();

			MapDTOCfg mapDTOCfg=MapDTOUtil.getTableInfo(conn, tableName);
			if(mapDTOCfg==null)
				return null;
			MapDTO dto=new MapDTO(mapDTOCfg);
			return dto;//this.mapDTO(request, mapDTOCfg);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	@Override
	public MapDTO getMapDTO(String tableName,HttpServletRequest request ) {
		Connection conn=null;
		try{		
			conn=dataSource.getConnection();

			MapDTOCfg mapDTOCfg=MapDTOUtil.getTableInfo(conn, tableName);
			if(mapDTOCfg==null)
				return null;
			MapDTO dto=new MapDTO(mapDTOCfg);
			Tool.requestToManagerDTO(dto, request);
			return dto;//this.mapDTO(request, mapDTOCfg);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
