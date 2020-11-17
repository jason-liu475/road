package org.liu.road.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("districtrank")
public class DistrictrankModel extends Model<DistrictrankModel> {

    private static final long serialVersionUID = 1L;

    private String center;

    private String citycode;

    private String cityname;

    private String districtCode;

    private String districtName;
    @TableField(value = "`index`")
    private String index;

    private String indexLevel;

    private String length;

    private String roadType;

    private String speed;

    @TableField("timeHuman")
    private String timeHuman;

    private String timestamp;

    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
