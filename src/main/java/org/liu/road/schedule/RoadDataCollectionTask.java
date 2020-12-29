package org.liu.road.schedule;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import org.liu.road.entity.SysTaskModel;
import org.liu.road.service.DistrictrankService;
import org.liu.road.service.RoadrankService;
import org.liu.road.service.SysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author liu
 */
@Slf4j
@Component
public class RoadDataCollectionTask {
	@Autowired
	private SysTaskService sysTaskService;
	@Autowired
	private DistrictrankService districtrankService;
	@Autowired
	private RoadrankService roadrankService;

//	@Scheduled(fixedRate = 5 * 60 * 1000)
	public void collectionDistrictRankData(){
		int retryCount = 0;
		SysTaskModel sysTaskModel = null;
		try {
			sysTaskModel = sysTaskService.initCollectionDistrictRankTask();
			districtrankService.collectionDistrictRankData(sysTaskModel);
		}catch (Exception e){
			log.error(e.getMessage());
			retryCount++;
			districtrankService.retryCollectionDistrictRankData(sysTaskModel,retryCount);
		}finally {
			log.info("任务相关数据：{}", JSONObject.toJSONString(sysTaskModel));
			sysTaskService.finishTask(sysTaskModel);
		}
	}
//	@Scheduled(fixedRate = 5 * 60 * 1000)
	public void collectionRoadRank1Data(){
		int retryCount = 0;
		SysTaskModel sysTaskModel = null;
		try {
			sysTaskModel = sysTaskService.initCollectionRoadRank1DataTask();
			roadrankService.fundCollectionRoadRank1Data(sysTaskModel);
		}catch (Exception e){
			log.error(e.getMessage());
			retryCount++;
			roadrankService.retryCollectionRoadRank1Data(sysTaskModel,retryCount);
		}finally {
			log.info("任务相关数据：{}", JSONObject.toJSONString(sysTaskModel));
			sysTaskService.finishTask(sysTaskModel);
		}
	}
//	@Scheduled(fixedRate = 5 * 60 * 1000)
	public void collectionRoadRank11Data(){
		int retryCount = 0;
		SysTaskModel sysTaskModel = null;
		try {
			sysTaskModel = sysTaskService.initCollectionRoadRank11DataTask();
			roadrankService.fundCollectionRoadRank11Data(sysTaskModel);
		}catch (Exception e){
			log.error(e.getMessage());
			retryCount++;
			roadrankService.retryCollectionRoadRank11Data(sysTaskModel,retryCount);
		}finally {
			log.info("任务相关数据：{}", JSONObject.toJSONString(sysTaskModel));
			sysTaskService.finishTask(sysTaskModel);
		}
	}
}
