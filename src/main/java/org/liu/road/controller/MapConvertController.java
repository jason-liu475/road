package org.liu.road.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@Controller
public class MapConvertController {
	@Value("${system.http.map.host}")
	private String host;
	@Value("${system.http.map.path}")
	private String path;

	@ResponseBody
	@PostMapping(value="/upload")
	public List<String> upload(@RequestParam("file")MultipartFile[] files) throws Exception {
		List<String> filePaths = new ArrayList<>();
		if(ArrayUtil.isNotEmpty(files)){
			String prefix = "/tmp/" + System.currentTimeMillis();
			for(MultipartFile file : files){
				String fileName = file.getOriginalFilename();
				assert fileName != null;
				if(!fileName.contains("xls") && !fileName.contains("xlsx")){
					continue;
				}
				log.info("file name:{}",fileName);
				String filePath = saveFile(prefix,fileName,file);
				ExcelReader reader = ExcelUtil.getReader(filePath, 0);
				List<Map<String, Object>> data = reader.readAll();
				reader.close();
				int maxCol = data.get(0).keySet().size();
				log.info("maxCol:{}",maxCol);
				List<String> urls = readExcel(data);
				List<Map<String,String>> mapData = getMapData(urls);
				writeExcel(filePath,mapData,maxCol);
				filePaths.add(filePath);
				//writeResponse(filePath,out);
			}
		}
		return filePaths;
	}
	@RequestMapping(value="/download",method = RequestMethod.GET)
	public void download(@RequestParam("filePath")String filePath,HttpServletResponse response) throws Exception {
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		response.reset();
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/octet-stream");
		writeResponse(filePath,response);
	}
	private String saveFile(String prefix,String fileName,MultipartFile file) throws Exception{
		File dir = new File(prefix);
		if(!dir.exists()){
			dir.mkdirs();
		}
		InputStream inputStream = file.getInputStream();
		String filePath = prefix + "/" + fileName;
		try(OutputStream out = new FileOutputStream(filePath)){
			byte[] bs = new byte[1024];
			int len;
			while ((len = inputStream.read(bs)) != -1) {
				out.write(bs, 0, len);
			}
		}
		return filePath;
	}

	private List<String> readExcel(List<Map<String, Object>> data) throws IOException {
		List<String> urls = new ArrayList<>();
		for(Map<String,Object> map : data){
			String lat = String.valueOf(map.get("lat"));
			String lng = String.valueOf(map.get("lng"));
			urls.add(host + String.format(path,lng,lat));
		}
		return urls;
	}
	private List<Map<String,String>> getMapData(List<String> urls) {
		List<Map<String,String>> writeData = new ArrayList<>();
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
		return writeData;
	}
	private void writeExcel(String filePath,List<Map<String,String>> writeData,int maxCol) throws IOException {
		ExcelWriter writer = ExcelUtil.getWriter(filePath);
		int col2 = maxCol + 1,y = 0;
		writer.writeCellValue(maxCol,y,"x");
		writer.writeCellValue(col2,y,"y");
		for(Map<String,String> map : writeData){
			y++;
			if(Objects.equals(map.get("e"),"0")){
				writer.writeCellValue(maxCol,y,decode(map.get("x")));
				writer.writeCellValue(col2,y,decode(map.get("y")));
			}
		}
		writer.flush();
		writer.close();
	}
	public static String decode(String source) {
//		String sourceX = "MTEzLjAxNjEwNjQyMDI2";
//		String sourceY = "MTEzLjAxNjEwNjQyMDI2";
		log.info("before decode :{}",source);
		String result = Base64.decodeStr(source);
		log.info("after decode :{}",result);
		return result;
	}
	private void writeResponse(String filePath,HttpServletResponse response) throws Exception{
		ServletOutputStream outputStream = response.getOutputStream();
		FileInputStream fis = new FileInputStream(filePath);
		int len;
		byte[] bs = new byte[1024];
		while ((len = fis.read(bs)) != -1) {
			outputStream.write(bs,0,len);
		}
		fis.close();
		outputStream.flush();
		outputStream.close();
	}

}
