package org.liu.road.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
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
@TableName("roadrank")
public class RoadrankModel extends Model<RoadrankModel> {

    private static final long serialVersionUID = 1L;

    private String citycode;

    private String districtType;

    private String id;
    @TableField(value = "`index`")
    private String index;

    private String indexLevel;

    private String length;

    private String links;

    private String location;

    private String nameadd;

    private String roadType;

    private String roadname;

    private String roadsegid;

    private String semantic;

    private String speed;

    private String time;

    private String yongduLength;

    private Date createTime;

    private String collectType;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
