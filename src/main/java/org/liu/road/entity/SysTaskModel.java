package org.liu.road.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuhuan
 * @since 2020-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_task")
public class SysTaskModel extends Model<SysTaskModel> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String taskName;

    private String taskCode;

    /**
     *  0: 1: 2:
     */
    private Integer isFinish;

    private Integer retryCount;

    /**
     * 任务开始时间
     */
    private Date taskStartTime;

    /**
     * 任务结束时间
     */
    private Date taskEndTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
