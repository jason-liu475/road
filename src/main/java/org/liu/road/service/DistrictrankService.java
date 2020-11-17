package org.liu.road.service;

import org.liu.road.entity.DistrictrankModel;
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
public interface DistrictrankService extends IService<DistrictrankModel> {

    void collectionDistrictRankData(SysTaskModel sysTaskModel);

    void retryCollectionDistrictRankData(SysTaskModel sysTaskModel, int retryCount);
}
