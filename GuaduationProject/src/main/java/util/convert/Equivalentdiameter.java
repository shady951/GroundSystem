package util.convert;

/**
 * 接地极等效直径计算模块
 * @author tc
 */
public class Equivalentdiameter {

//	public Equivalentdiameter() {
//	}
	
	public static double getEequivalentdiameter(int index, Double b) {
		Double d = b;
		/*
		 * 序号为 
		 * 1：圆钢
		 * 2：钢管
		 * 3：扁钢
		 * 4：等边角钢
		 * 5：不等边角钢
		 */
		switch(index) {
		case 3:	d = b / 2;
			break;
		case 4:	d = 0.84 * b;
			break;
		}
		return d;
	}
	
	public static double getEequivalentdiameter(int index, Double b, Double b1) {
		Double d = null;
		Double N = b * b1 * (Math.pow(b, 2) + Math.pow(b1, 2));
		d = 0.71 * Math.pow(N, 0.25);
		return d;
	}

}
