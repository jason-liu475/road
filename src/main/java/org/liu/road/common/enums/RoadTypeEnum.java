package org.liu.road.common.enums;


/**
 * 道路类型 高速/快速路 普通道路
 */
public enum RoadTypeEnum {
	TYPE1("高速/快速路"),
	TYPE11("普通道路");
    private final String desc;
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc
	 */
	RoadTypeEnum(String desc) {
		this.desc = desc;
	}
}
