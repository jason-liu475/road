package org.liu.road.service;

import org.liu.road.entity.SysTaskModel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhuan
 * @since 2020-11-17
 */
public interface SysTaskService extends IService<SysTaskModel> {

    SysTaskModel initCollectionDistrictRankTask();

    void finishTask(SysTaskModel sysTaskModel);

    SysTaskModel initCollectionRoadRank1DataTask();

    SysTaskModel initCollectionRoadRank11DataTask();

    void progressing(SysTaskModel sysTaskModel);
}
