package org.liu.road.service;

import org.liu.road.entity.RoadrankModel;
import com.baomidou.mybatisplus.extension.service.IService;
import org.liu.road.entity.SysTaskModel;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhuan
 * @since 2020-11-17
 */
public interface RoadrankService extends IService<RoadrankModel> {

    void fundCollectionRoadRank1Data(SysTaskModel sysTaskModel);

    void retryCollectionRoadRank1Data(SysTaskModel sysTaskModel, int retryCount);

    void fundCollectionRoadRank11Data(SysTaskModel sysTaskModel);

    void retryCollectionRoadRank11Data(SysTaskModel sysTaskModel, int retryCount);
}
