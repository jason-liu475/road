package org.liu.road.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.io.Serializable;

/**
 * 任务 枚举类
 */
public enum TaskEnum implements IEnum {
	DISTRICTDATA("TASK_01","获取实时拥堵行政区排行数据"),
	ROADTYPE1DATA("TASK_02","获取实时拥堵高速/快速路排行数据"),
	ROADTYPE11DATA("TASK_03","获取实时拥堵普通道路数据");
	private final String code;
    private final String desc;
	public String getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
	/**
	 * @param code
	 * @param desc
	 */
	TaskEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	@Override
	public Serializable getValue() {
		return this.code;
	}
}
