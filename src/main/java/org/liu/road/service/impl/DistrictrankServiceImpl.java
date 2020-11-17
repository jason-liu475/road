package org.liu.road.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.liu.road.entity.DistrictrankModel;
import org.liu.road.dao.DistrictrankMapper;
import org.liu.road.entity.SysTaskModel;
import org.liu.road.service.DistrictrankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.liu.road.service.SysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhuan
 * @since 2020-11-17
 */
@Slf4j
@Service
public class DistrictrankServiceImpl extends ServiceImpl<DistrictrankMapper, DistrictrankModel> implements DistrictrankService {
    @Autowired
    private SysTaskService sysTaskService;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${system.http.road.host}")
    private String host;
    @Value("${system.http.road.districtrank}")
    private String districtRankUri;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void collectionDistrictRankData(SysTaskModel sysTaskModel) {
        sysTaskService.progressing(sysTaskModel);
        fundCollectionDistrictRankData();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retryCollectionDistrictRankData(SysTaskModel sysTaskModel, int retryCount) {
        sysTaskModel.setRetryCount(retryCount);
        sysTaskService.updateById(sysTaskModel);
        fundCollectionDistrictRankData();
    }


    private void fundCollectionDistrictRankData() {
        //1.获取数据
        String jsonString = getDistrictRankData();
        //2.解析数据
        List<DistrictrankModel> districtRankModels = analysisDistrictRankData(jsonString);
        //3.入库
        this.saveBatch(districtRankModels);
    }
    private String getDistrictRankData() {
        String url = host + districtRankUri;
        log.info("url:{}", url);
        String jsonString = restTemplate.getForObject(url, String.class);
        log.info("result:{}", jsonString);
        return jsonString;
    }
    private List<DistrictrankModel> analysisDistrictRankData(String jsonString) {
        List<DistrictrankModel> districtRankModels = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        if(CollectionUtils.isNotEmpty(list)){
            Date now = new Date();
            DistrictrankModel districtrankModel = null;
            for(Object json : list){
                JSONObject obj = (JSONObject)json;
                districtrankModel = new DistrictrankModel();
                districtrankModel.setCenter(obj.getString("center"));
                districtrankModel.setCitycode(obj.getString("citycode"));
                districtrankModel.setCityname(obj.getString("cityname"));
                districtrankModel.setDistrictCode(obj.getString("district_code"));
                districtrankModel.setDistrictName(obj.getString("district_name"));
                districtrankModel.setIndex(obj.getString("index"));
                districtrankModel.setIndexLevel(obj.getString("index_level"));
                districtrankModel.setLength(obj.getString("length"));
                districtrankModel.setRoadType(obj.getString("road_type"));
                districtrankModel.setSpeed(obj.getString("speed"));
                districtrankModel.setTimeHuman(obj.getString("timeHuman"));
                districtrankModel.setTimestamp(obj.getString("timestamp"));
                districtrankModel.setCreateTime(now);
                districtRankModels.add(districtrankModel);
            }
        }
        return districtRankModels;
    }
}
