package service;

import dto.Countresult;

public interface Ground {

		public final double pi = Math.PI;
		//地网或垂直接地体顶部埋深(米)
		public final double h = 0.8;
		//接地体水平等效直径(米)
		public final double bc = 0.05;
		//接地体垂直等效直径(米)
		public final double br = 0.05;
		
		/**
		 * 防雷接地设计接口
		 * @param p		(上层)土壤电阻率
		 * @param H		上层土壤深度(0即表示均匀土壤)
		 * @param p1		下层土壤电阻率
		 * @param S		建筑占地面积
		 * @param Rk		工频电阻要求值
		 * @param type	建筑类型
		 * @param city	周围环境
		 * @return cs		数据实体类
		 */
		public Countresult design(Double p, Double H, Double p1, Double S, Double Rk, Integer type, boolean city);
}
