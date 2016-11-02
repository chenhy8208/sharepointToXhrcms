package com.hongru.db.util;

import com.hongru.config.GlobalConfig;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Db{

	public InputStream getFileStreamFromDatabase(String directory, String filename) throws Exception{
		InputStream is = null;
		Class.forName(GlobalConfig.DRIVER_NAME);
		try(
				Connection connShare = DriverManager.getConnection(importConfig.getShare_url(), importConfig.getShare_username(), importConfig.getShare_password());
		) {
			//读取
			if (connShare != null)
			{
				PreparedStatement pstmt = null;

				try {
					pstmt = connShare.prepareStatement("select * from AllDocStreams where id=(select id from AllDocs where DirName=? and LeafName=?)");
					pstmt.setString(1, directory);
					pstmt.setString(2, filename);

					ResultSet rs = pstmt.executeQuery();
					if (rs != null) {
						if (rs.next()) {
							is = rs.getBinaryStream("content");
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {

					if (pstmt != null) {
						try {
							pstmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}


				}

			}
		}

		return is;
	}

	/**
	 * 导入的时间段
	 * @return
     */
	private String getDateUseDateLimit() {
		//10/11/2016 - 10/27/2016
		if (!importConfig.isAllIn()) {
			if (StringUtils.isNoneBlank(importConfig.getImport_time_area()) && StringUtils.indexOf(importConfig.getImport_time_area(),"-")>0) {
				String[] times = importConfig.getImport_time_area().split(" - ");
				return "and tp_Created between '"+ times[0] +"' and '"+ times[1]+"'";
			}
		}

		return "";
	}

	public void dataImport() throws Exception {
		if (importConfig == null){
			throw new RuntimeException("importConfig == null");
		}

		String sharePointUrl = importConfig.getShare_url();
		if (StringUtils.isBlank(sharePointUrl)) {
			throw new RuntimeException("sharePointUrl == null");
		}

		String[] spus = sharePointUrl.split(",");
		for (String spu:spus) {
			Class.forName(GlobalConfig.DRIVER_NAME);
			try(
					Connection connShare = DriverManager.getConnection(spu, importConfig.getShare_username(), importConfig.getShare_password());
					Connection connCms = DriverManager.getConnection(importConfig.getCms_url(), importConfig.getCms_username(), importConfig.getCms_password());
			) {
				//读取
				if (connShare != null)
				{
					PreparedStatement pstmt = null;
					Statement cmsStmt = null;

					Statement stmt = null;
					ResultSet rs = null;
					try {
						stmt = connShare.createStatement();
						String where = getDateUseDateLimit();
						String sql = "select * from AllUserData where datetime1 is not null "+ where +" order by [tp_Created] desc";
						System.out.println("sql = " + sql);
						rs = stmt.executeQuery(sql);

						cmsStmt = connCms.createStatement();

						while (rs.next()) {
							String title = rs.getString("nvarchar1");
							String content = replaceImgFilesUrl(rs.getString("ntext2"));
							Date date = rs.getDate("tp_Modified");
							String guid = rs.getString("tp_guid");

							ResultSet rsCount = cmsStmt.executeQuery("select count(*) from hr_u_article_ where Single1='"+ guid +"'" );
							if (rsCount!=null) {

								rsCount.next();
								if (rsCount.getInt(1) <= 0) {
									pstmt = connCms.prepareStatement("insert into hr_u_article_ (Title,UpdateTime,Content,CopyFrom,classid,IsStatus,Sort,State,IsDel,GroupId,SiteId,Single1) values (?,?,?,?,5,0,0,0,0,0,0,?)");
									pstmt.setString(1, title);
									pstmt.setDate(2, date);
									pstmt.setString(3, content);
									pstmt.setString(4, "sharepoint");
									pstmt.setString(5, guid);
									pstmt.executeUpdate();
								}

							}

						}

					} catch (SQLException e) {
						e.printStackTrace();
					} finally {

						if (pstmt != null) {
							try {
								pstmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}

						if (cmsStmt != null) {
							try {
								cmsStmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}

						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						try {
							stmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}

				}
			}
		}
	}

	/**
	 * 去除奇怪的空格
	 * @param content
	 * @return
     */
	private String replaceSpace(String content) {
		content = content.replace(asciiToString("8203"), "");
		return content;
	}

	private String asciiToString(String value)
	{
		StringBuffer sbu = new StringBuffer();
		String[] chars = value.split(",");
		for (int i = 0; i < chars.length; i++) {
			sbu.append((char) Integer.parseInt(chars[i]));
		}
		return sbu.toString();
	}

	/**
	 * 替换路径
	 * @param content
	 * @return
     */
	private String replaceImgFilesUrl(String content) {
		if (StringUtils.isBlank(content)) return content;

		//去除奇怪的空格
		content = replaceSpace(content);

		String regex = "(?:src|href)=\"(.*?)\"";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			String str = matcher.group();
			String url = matcher.group(1);
			if (!StringUtils.startsWith(url, "http")) {
				String newStr = null;
				try {
					newStr = str.replace(url, GlobalConfig.IMG_SERVER_DOMAIN + "?f=" + new BASE64Encoder().encode(url.getBytes("utf-8")));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				content = content.replace(str, newStr);
			}
		}

		return content;
	}

	private DBImportConfig importConfig;
	private Db(){}
	public Db(DBImportConfig importConfig) {
		this.importConfig = importConfig;
	}

}
