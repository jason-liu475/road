package org.liu.road;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapConvertTest {
	static List<String> urls = new ArrayList<>();
	static String host = "http://api.map.baidu.com";
	static String path = "/ag/coord/convert?from=0&to=4&x=%s&y=%s";
	static String filePath = "C:\\Users\\liu\\Desktop\\【GPS】坐标927.xlsx";
	static int maxCol = 0;
	static List<Map<String,String>> writeData = new ArrayList<>();
	public static void main(String[] args) {
		readExcel();
		getMapData();
		writeExcel();		
	}
	public static void main1(String[] args) {
		String sourceX = "MTEzLjAxNjEwNjQyMDI2";
		String sourceY = "MTEzLjAxNjEwNjQyMDI2";
		log.info("before decode x:{},y:{}",sourceX,sourceY);
		String x = Base64.decodeStr(sourceX);
		String y = Base64.decodeStr(sourceY);
		log.info("after decode x:{},y:{}",x,y);
	}
	private static void readExcel() {
		ExcelReader reader = ExcelUtil.getReader(FileUtil.file(filePath), 0);
		List<Map<String, Object>> data = reader.readAll();
		maxCol = data.get(0).keySet().size();
		log.info("maxCol:{}",maxCol);
		for(Map<String,Object> map : data){
			String lat = String.valueOf(map.get("lat"));
			String lng = String.valueOf(map.get("lng"));
			urls.add(host + String.format(path,lng,lat));
		}
		reader.close();
	}
	private static void getMapData() {
		if(CollectionUtil.isNotEmpty(urls)) {
			for (String url : urls) {
				Map<String,String> map = new HashMap<>();
				String jsonStr = HttpUtil.get(url);
				log.info("url:{}\r\nresult:{}",url,jsonStr);
				JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
				if(Objects.equals(jsonObject.get("error",String.class),"0")){
					map.put("e","0");
					map.put("x",jsonObject.get("x",String.class));
					map.put("y",jsonObject.get("y",String.class));
				}else{
					map.put("e",jsonObject.get("error",String.class));
				}

				writeData.add(map);
			}
		}
	}
	private static void writeExcel() {
		ExcelWriter writer = ExcelUtil.getWriter(filePath);
		int col1 = maxCol,col2 = maxCol + 1,y = 0;
		writer.writeCellValue(col1,y,"x");
		writer.writeCellValue(col2,y,"y");
		for(Map<String,String> map : writeData){
			y++;
			if(Objects.equals(map.get("e"),"0")){
				writer.writeCellValue(col1,y,decode(map.get("x")));
				writer.writeCellValue(col2,y,decode(map.get("y")));
			}
		}
		writer.flush();
		writer.close();
	}
	public static String decode(String source) {
//		String sourceX = "MTEzLjAxNjEwNjQyMDI2";
//		String sourceY = "MTEzLjAxNjEwNjQyMDI2";
		log.debug("before decode :{}",source);
		String result = Base64.decodeStr(source);
		log.debug("after decode :{}",result);
		return result;
	}
}
