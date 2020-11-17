package org.liu.road.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.liu.road.common.enums.RoadTypeEnum;
import org.liu.road.entity.DistrictrankModel;
import org.liu.road.entity.RoadrankModel;
import org.liu.road.dao.RoadrankMapper;
import org.liu.road.entity.SysTaskModel;
import org.liu.road.service.RoadrankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.liu.road.service.SysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class RoadrankServiceImpl extends ServiceImpl<RoadrankMapper, RoadrankModel> implements RoadrankService {
    @Autowired
    private SysTaskService sysTaskService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${system.http.road.host}")
    private String host;
    @Value("${system.http.road.roadrank1}")
    private String roadRank1Uri;
    @Value("${system.http.road.roadrank11}")
    private String roadRank11Uri;
    @Override
    public void fundCollectionRoadRank1Data(SysTaskModel sysTaskModel) {
        sysTaskService.progressing(sysTaskModel);
        fundCollectionRoadRankData(RoadTypeEnum.TYPE1);
    }

    @Override
    public void retryCollectionRoadRank1Data(SysTaskModel sysTaskModel, int retryCount) {
        sysTaskModel.setRetryCount(retryCount);
        sysTaskService.updateById(sysTaskModel);
        fundCollectionRoadRankData(RoadTypeEnum.TYPE1);
    }

    @Override
    public void fundCollectionRoadRank11Data(SysTaskModel sysTaskModel) {
        sysTaskService.progressing(sysTaskModel);
        fundCollectionRoadRankData(RoadTypeEnum.TYPE11);
    }

    @Override
    public void retryCollectionRoadRank11Data(SysTaskModel sysTaskModel, int retryCount) {
        sysTaskModel.setRetryCount(retryCount);
        sysTaskService.updateById(sysTaskModel);
        fundCollectionRoadRankData(RoadTypeEnum.TYPE11);
    }

    private void fundCollectionRoadRankData(RoadTypeEnum roadType) {
        //1.获取数据
        String jsonString = getRoadRankData(roadType);
        //2.解析数据
        List<RoadrankModel> roadRankModels = analysisRoadRankData(jsonString,roadType);
        //3.入库
        this.saveBatch(roadRankModels);
    }
    private String getRoadRankData(RoadTypeEnum roadType) {
        String url = host + (roadType == RoadTypeEnum.TYPE1 ? roadRank1Uri : roadRank11Uri);
        log.info("url:{}", url);
        String jsonString = restTemplate.getForObject(url, String.class);
        log.info("result:{}", jsonString);
        return jsonString;
    }
    private List<RoadrankModel> analysisRoadRankData(String jsonString,RoadTypeEnum roadType) {
        List<RoadrankModel> roadRankModels = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        if(CollectionUtils.isNotEmpty(list)){
            Date now = new Date();
            RoadrankModel roadrankModel = null;
            for(Object json : list) {
                JSONObject obj = (JSONObject)json;
                roadrankModel = new RoadrankModel();
                roadrankModel.setCitycode(obj.getString("citycode"));
                roadrankModel.setDistrictType(obj.getString("district_type"));
                roadrankModel.setId(obj.getString("id"));
                roadrankModel.setIndex(obj.getString("index"));
                roadrankModel.setIndexLevel(obj.getString("index_level"));
                roadrankModel.setLength(obj.getString("length"));
                roadrankModel.setLinks(obj.getString("links"));
                roadrankModel.setLocation(obj.getString("location"));
                roadrankModel.setNameadd(obj.getString("nameadd"));
                roadrankModel.setRoadType(obj.getString("road_type"));
                roadrankModel.setRoadname(obj.getString("roadname"));
                roadrankModel.setRoadsegid(obj.getString("roadsegid"));
                roadrankModel.setSemantic(obj.getString("semantic"));
                roadrankModel.setSpeed(obj.getString("speed"));
                roadrankModel.setTime(obj.getString("time"));
                roadrankModel.setYongduLength(obj.getString("yongdu_length"));
                roadrankModel.setCreateTime(now);
                roadrankModel.setCollectType(roadType.getDesc());
                roadRankModels.add(roadrankModel);
            }
        }
        return roadRankModels;
    }
}
