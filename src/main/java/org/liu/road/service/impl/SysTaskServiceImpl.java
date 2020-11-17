package org.liu.road.service.impl;

import org.liu.road.common.enums.FinishEnum;
import org.liu.road.common.enums.TaskEnum;
import org.liu.road.entity.SysTaskModel;
import org.liu.road.dao.SysTaskMapper;
import org.liu.road.service.SysTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhuan
 * @since 2020-11-17
 */
@Service
public class SysTaskServiceImpl extends ServiceImpl<SysTaskMapper, SysTaskModel> implements SysTaskService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysTaskModel initCollectionDistrictRankTask() {
        SysTaskModel sysTaskModel = new SysTaskModel();
        sysTaskModel.setTaskName(TaskEnum.DISTRICTDATA.getDesc());
        sysTaskModel.setTaskCode(TaskEnum.DISTRICTDATA.getCode());
        sysTaskModel.setIsFinish(FinishEnum.INIT.getCode());
        sysTaskModel.setRetryCount(0);
        sysTaskModel.setTaskStartTime(new Date());
        this.save(sysTaskModel);
        return sysTaskModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishTask(SysTaskModel sysTaskModel) {
        sysTaskModel.setIsFinish(FinishEnum.FINISHED.getCode());
        sysTaskModel.setTaskEndTime(new Date());
        this.updateById(sysTaskModel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysTaskModel initCollectionRoadRank1DataTask() {
        SysTaskModel sysTaskModel = new SysTaskModel();
        sysTaskModel.setTaskName(TaskEnum.ROADTYPE1DATA.getDesc());
        sysTaskModel.setTaskCode(TaskEnum.ROADTYPE1DATA.getCode());
        sysTaskModel.setIsFinish(FinishEnum.INIT.getCode());
        sysTaskModel.setRetryCount(0);
        sysTaskModel.setTaskStartTime(new Date());
        this.save(sysTaskModel);
        return sysTaskModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysTaskModel initCollectionRoadRank11DataTask() {
        SysTaskModel sysTaskModel = new SysTaskModel();
        sysTaskModel.setTaskName(TaskEnum.ROADTYPE11DATA.getDesc());
        sysTaskModel.setTaskCode(TaskEnum.ROADTYPE11DATA.getCode());
        sysTaskModel.setIsFinish(FinishEnum.INIT.getCode());
        sysTaskModel.setRetryCount(0);
        sysTaskModel.setTaskStartTime(new Date());
        this.save(sysTaskModel);
        return sysTaskModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void progressing(SysTaskModel sysTaskModel) {
        sysTaskModel.setIsFinish(FinishEnum.PROGRESSING.getCode());
        this.updateById(sysTaskModel);
    }
}
