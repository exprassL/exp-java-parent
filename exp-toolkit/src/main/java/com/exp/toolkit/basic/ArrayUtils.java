package com.exp.toolkit.basic;

public final class ArrayUtils {
    
    /**
     * 判断指定元素与数组中哪一个下标的元素{@code equals}相等
     *
     * @param ta 数组
     * @param t   元素
     * @param <T> 数组元素类型，应该覆盖{@link Object#hashCode()}和{@link Object#equals(Object)}方法
     * @return  返回匹配元素在数组中的下标，或者数组中不包含匹配元素时返回-1
     */
    public static <T> int indexOf(T[] ta, T t) {
        int len = ta.length;
        for (int i = 0; i < len; i++) {
            if (ta[i].equals(t)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * 获取元素在数组中的下标
     *
     * @param ta 数组
     * @param t   元素
     * @param <T> 数组元素类型
     * @return 返回元素在数组中的下标，或者数组中包含此元素时返回-1
     */
    public static <T> int exactIndexOf(T[] ta, T t) {
        int len = ta.length;
        for (int i = 0; i < len; i++) {
            if (ta[i] == t) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * 获取元素在数组中的下标
     *
     * @param ia 数组
     * @param i  元素
     * @return  返回元素在数组中的下标，或者数组中包含此元素时返回-1
     */
    public static int indexOf(int[] ia, int i) {
        int len = ia.length;
        for (int j = 0; j < len; j++) {
            if (ia[j] == i) {
                return j;
            }
        }
        return -1;
    }
    
    /**
     * @see #indexOf(int[], int)
     */
    public static int indexOf(long[] la, long l) {
        int len = la.length;
        for (int i = 0; i < len; i++) {
            if (la[i] == l) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * @see #indexOf(int[], int)
     */
    public static int indexOf(float[] fa, float f) {
        int len = fa.length;
        for (int i = 0; i < len; i++) {
            if (fa[i] == f) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * @see #indexOf(int[], int)
     */
    public static int indexOf(double[] da, double d) {
        int len = da.length;
        for (int i = 0; i < len; i++) {
            if (da[i] == d) {
                return i;
            }
        }
        return -1;
    }
}
